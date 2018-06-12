package atmjnj;

import atmjnj.components.Account;
import atmjnj.components.AccountRepository;
import atmjnj.components.AtmRepository;
import atmjnj.exceptions.AccountNotFoundException;
import atmjnj.exceptions.IncorrectPinException;
import atmjnj.exceptions.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    public static int ATM_ID = 1;
    private AccountRepository accountRepos;
    private AtmRepository atmRepos;
    private AtmService atmService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepos, AtmRepository atmRepos) {
        this.accountRepos = accountRepos;
        this.atmRepos = atmRepos;
        this.atmService = new AtmServiceImpl(this.atmRepos);
    }

    @Override
    public Account getAccount(long accountNumber, long pin) {
        Account curr = this.accountRepos.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (curr.getPin() != pin) {
            throw new IncorrectPinException(accountNumber);
        }
        return curr;
    }

    @Override
    public Long getBalance(long accountNumber, long pin) {
        return getAccount(accountNumber, pin).getBalance();
    }

    @Override
    public Long getMaxWithDrawal(long accountNumber, long pin) {
        Account ac = getAccount(accountNumber, pin);
        return ac.getBalance() + ac.getOverdraft();
    }

    @Override
    public Map<Object, Long> dispense(long accountNumber, long pin, long amount) {
        Account ac = getAccount(accountNumber, pin);
        long funds = ac.getBalance() + ac.getOverdraft();
        if (funds < amount) {
            throw new InsufficientFundsException("account " + accountNumber);
        }

        if (atmService.getBalance() < amount) {
            throw new InsufficientFundsException("ATM");
        }

        Map<Object, Long> notesToDispense = atmService.dispense(amount);
        ac.dispense(amount);
        accountRepos.save(ac);

        return notesToDispense;
    }
}
