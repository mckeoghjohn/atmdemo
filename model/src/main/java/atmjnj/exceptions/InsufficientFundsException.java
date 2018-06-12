package atmjnj.exceptions;

/**
 * @author John McKeogh
 */
// tag::code[]
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String input) {
        super("Sorry insufficient funds in " + input);
    }
}
// end::code[]
