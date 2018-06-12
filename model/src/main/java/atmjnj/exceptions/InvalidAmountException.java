package atmjnj.exceptions;

/**
 * @author John McKeogh
 */
// tag::code[]
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(long multiples) {
        super("sorry the atm can only dispense in multiples of " + multiples);
    }
}
// end::code[]
