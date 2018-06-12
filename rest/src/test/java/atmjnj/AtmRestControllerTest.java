package atmjnj;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author John McKeogh
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AtmRestControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private int accountNumber = 123456789;
    private int accountNumber2 = 987654321;
    private int pin = 1234;
    private int pin2 = 4321;
    private int balance = 800;
    private int overdraft = 200;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void accountNotFound() throws Exception {
        mockMvc.perform(get("/7777?pin=4444")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readAccount() throws Exception {
        readAccountData(balance);
    }

    @Test
    public void checkBalance() throws Exception {
        mockMvc.perform(get("/" + accountNumber + "/balance" + "?pin=" + pin)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(balance)));
    }

    @Test
    public void checkMaxWithDrawalLimit() throws Exception {
        mockMvc.perform(get("/" + accountNumber + "/withdrawlimit" + "?pin=" + pin)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", is(balance + overdraft)));
    }

    @Test
    public void checkBalancePinIncorrect() throws Exception {
        mockMvc.perform(get("/" + accountNumber + "/balance" + "?pin=9999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.error", is("sorry your pin was incorrect for " + accountNumber)));
    }

    @Test
    public void withDrawFromAccountandCheckNotes() throws Exception {
        int withdrawAmount = 285;
        mockMvc.perform(post("/" + accountNumber + "/dispense/" + withdrawAmount + "?pin=" + pin)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.5", is(1)))
                .andExpect(jsonPath("$.10", is(1)))
                .andExpect(jsonPath("$.20", is(1)))
                .andExpect(jsonPath("$.50", is(5)));
        int newBalance = this.balance - withdrawAmount;
        readAccountData(newBalance);
    }

    @Test
    public void withDrawInvalidAmountFromAccount() throws Exception {
        int withdrawAmount = 289;
        mockMvc.perform(post("/" + accountNumber + "/dispense/" + withdrawAmount + "?pin=" + pin))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.error", is("sorry the atm can only dispense in multiples of 5")));
        readAccountData(balance);
    }

    @Test
    public void withDrawFromAccountPinIncorrect() throws Exception {
        balance = balance - 100;
        mockMvc.perform(post("/" + accountNumber + "/dispense/" + 100 + "?pin=9999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.error", is("sorry your pin was incorrect for " + accountNumber)));
    }

    @Test
    public void withDrawFromNonExistentAccount() throws Exception {
        mockMvc.perform(post("/" + 666666 + "/dispense/100?pin=23455")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insuffficientFunds() throws Exception {
        mockMvc.perform(post("/" + accountNumber + "/dispense/" + 1200 + "?pin=" + pin))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.error", is("Sorry insufficient funds in account " + accountNumber)));
    }

    @Test
    public void withDrawMoreThanAtmHolds() throws Exception {
        //atm holds 2000 at any time
        // current account limit is 1000
        // account2 987654321 has a limit of 1230
        withDraw(accountNumber2, 1100, pin2);
        //only 900 left in atm
        mockMvc.perform(post("/" + accountNumber + "/dispense/" + 1000 + "?pin=" + pin))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.error", is("Sorry insufficient funds in ATM")));
    }

    private void withDraw(int account, int amount, int pin) throws Exception {
        mockMvc.perform(post("/" + account + "/dispense/" + amount + "?pin=" + pin)
                .contentType(contentType))
                .andExpect(status().isCreated());
    }

    private void readAccountData(int newBalance) throws Exception {
        mockMvc.perform(get("/" + accountNumber + "?pin=" + pin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.accountNumber", is(accountNumber)))
                .andExpect(jsonPath("$.pin", is(pin)))
                .andExpect(jsonPath("$.balance", is(newBalance)))
                .andExpect(jsonPath("$.overdraft", is(overdraft)));
    }

}
