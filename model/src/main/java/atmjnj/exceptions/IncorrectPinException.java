package atmjnj.exceptions;

/**
 * @author John McKeogh
 */
// tag::code[]
public class IncorrectPinException extends RuntimeException {
    public IncorrectPinException(long accountNumber) {
        super("sorry your pin was incorrect for " + accountNumber);
    }
}
// end::code[]
