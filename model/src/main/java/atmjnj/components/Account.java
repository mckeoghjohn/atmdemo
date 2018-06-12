package atmjnj.components;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {

    @Id
    private long accountNumber;

    private long pin;

    private long balance;

    private long overdraft;


    private Account() {
    } // JPA only

    public Account(final long accountNumber, final long pin, long balance, long overdraft) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.overdraft = overdraft;
    }

    public void dispense(long amount) {
        if (balance > amount) {
            balance = balance - amount;
        }
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public long getPin() {
        return pin;
    }

    public long getOverdraft() {
        return overdraft;
    }

}
