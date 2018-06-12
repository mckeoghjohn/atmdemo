package atmjnj;

import atmjnj.components.Account;

import java.util.Map;


public interface AccountService {
    Account getAccount(long acccountNumber, long pin);

    Long getBalance(long accountNumber, long pin);

    Long getMaxWithDrawal(long accountNumber, long pin);

    Map<Object, Long> dispense(long accountNumber, long pin, long amount);

}
