package atmjnj.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice(annotations = RestController.class)
public class AtmRestControllerAdvice {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public AtmResponseError accountNotFoundException(AccountNotFoundException e) {
        return new AtmResponseError(e.getMessage());
    }

    @ExceptionHandler(IncorrectPinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AtmResponseError incorrectPinException(IncorrectPinException e) {
        return new AtmResponseError(e.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AtmResponseError insufficientFundsException(InsufficientFundsException e) {
        return new AtmResponseError(e.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AtmResponseError invalidAmountException(InvalidAmountException e) {
        return new AtmResponseError(e.getMessage());
    }


}
