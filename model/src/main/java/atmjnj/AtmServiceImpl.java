/*
@John McKeogh
 */
package atmjnj;

import atmjnj.components.Atm;
import atmjnj.components.AtmRepository;
import atmjnj.exceptions.InvalidAmountException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmServiceImpl implements AtmService {

    private static int ATM_ID = 1;
    private AtmRepository atmRepos;
    private Atm atm;

    public AtmServiceImpl(AtmRepository atmRepos) {
        this.atmRepos = atmRepos;
        atm = atmRepos.findById(ATM_ID);
    }

    @Override
    public Map<Object, Long> dispense(long amount) {
        Map<Object, Long> notes = getLeastNumberOfNotes(amount);

        for (Map.Entry<Object, Long> entry : notes.entrySet()) {
            long note = (long) entry.getKey();
            atm.updateCount((int) note, entry.getValue());
        }
        atmRepos.save(atm);

        return notes;
    }

    private Map<Object, Long> getLeastNumberOfNotes(long amount) {
        int deno[] = {5, 10, 20, 50};
        int noOfNotes[] = {(int) atm.getFive(), (int) atm.getTen(), (int) atm.getTwenty(), (int) atm.getFifty()};
        long totalRequested = amount;

        ArrayList<Long> notes = new ArrayList<>();

        //get smallest available note
        long smallestNote = 0;
        for (int i = 0; i < noOfNotes.length; i++) {
            if (noOfNotes[i] > 0) {
                smallestNote = deno[i];
                break;
            }
        }

        //create a list of notes to dispense
        for (int i = deno.length - 1; i >= 0; i--) {
            while (amount >= deno[i] && noOfNotes[i] > 0) {
                amount -= deno[i];
                noOfNotes[i]--;
                notes.add(new Long(deno[i]));
            }
        }

        Map<Object, Long> notesToDispense = new HashMap<>();
        notesToDispense = notes.stream().
                collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        long amountCanDispense = 0;

        // calculate amount available to dispense
        for (Map.Entry<Object, Long> entry : notesToDispense.entrySet()) {
            long note = (long) entry.getKey();
            amountCanDispense = amountCanDispense + ((int) note * entry.getValue());
        }

        if (amountCanDispense != totalRequested) {
            throw new InvalidAmountException(smallestNote);
        }
        return notesToDispense;
    }

    public long getBalance() {
        return (5 * atm.getFive() + 10 * atm.getTen() + 20 * atm.getTwenty() + 50 * atm.getFifty());
    }

}
