package atmjnj;

import atmjnj.components.Account;
import atmjnj.components.AccountRepository;
import atmjnj.components.Atm;
import atmjnj.components.AtmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John McKeogh
 */
// tag::code[]
@RestController
@RequestMapping("/{accountNumber}")
class AtmRestController {

    // private final BookmarkRepository bookmarkRepository;
    private final AccountRepository accountRepository;
    private final AtmRepository atmRepository;
    private final Atm atm;
    private final int ATM_ID = 1;
    private final AccountService accountService;
    private final AtmService atmService;

    @Autowired
    AtmRestController(AtmRepository atmRepository, AccountRepository accountRepository) {
        this.atmRepository = atmRepository;
        this.accountRepository = accountRepository;
        this.atm = this.atmRepository.findById(ATM_ID);
        this.accountService = new AccountServiceImpl(accountRepository, atmRepository);
        this.atmService = new AtmServiceImpl(atmRepository);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Account readAccount(@PathVariable long accountNumber,
                        @RequestParam(value = "pin", required = true) long pin) {
        return this.accountService.getAccount(accountNumber, pin);
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> getAccountBalance(@PathVariable long accountNumber,
                                               @RequestParam(value = "pin", required = true) long pin) {
        HashMap<String, Long> map = new HashMap<>();
        map.put("balance", this.accountService.getBalance(accountNumber, pin));
        return map;

    }

    @GetMapping("/withdrawlimit")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> getWithDrawLimit(@PathVariable long accountNumber,
                                              @RequestParam(value = "pin", required = true) long pin) {
        HashMap<String, Long> map = new HashMap<>();
        map.put("limit", this.accountService.getMaxWithDrawal(accountNumber, pin));
        return map;

    }


    @PostMapping("/dispense/{amount}")
    @ResponseStatus(HttpStatus.CREATED)
    Map<Object, Long> dispense(@PathVariable long accountNumber, @PathVariable long amount,
                               @RequestParam(value = "pin", required = true) long pin) {
        return this.accountService.dispense(accountNumber, pin, amount);
    }

}


// end::code[]
