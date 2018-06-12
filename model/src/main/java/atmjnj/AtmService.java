package atmjnj;

import java.util.Map;

public interface AtmService {
    Map<Object, Long> dispense(long amount);

    long getBalance();
}

