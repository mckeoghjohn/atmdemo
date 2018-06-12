package atmjnj.errors;

public class AtmResponseError {

    private String error;

    public AtmResponseError() {

    }

    public AtmResponseError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
