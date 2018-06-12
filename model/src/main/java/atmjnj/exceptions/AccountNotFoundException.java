package atmjnj.exceptions;


/**
 * @author John McKeogh
 */
// tag::code[]

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountNumber) {
        super("could not find account '" + accountNumber + "'.");
    }
}
// end::code[]
