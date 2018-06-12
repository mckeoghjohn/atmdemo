package atmjnj.components;

import javax.persistence.Entity;
import javax.persistence.Id;



//todo: revisit this class possibly restructure the lot
@Entity
public class Atm {

    @Id
    private int id;

    private long five;

    private long ten;

    private long twenty;

    private long fifty;

    private Atm() {
    } // JPA only

    public Atm(int id, long five, long ten, long twenty, long fifty) {
        this.five = five;
        this.ten = ten;
        this.twenty = twenty;
        this.fifty = fifty;
        this.id = id;
    }

    public long getFive() {
        return five;
    }

    public long getTen() {
        return ten;
    }

    public long getTwenty() {
        return twenty;
    }

    public long getFifty() {
        return fifty;
    }

    public void updateCount(int note, long value) {
        switch (note) {
            case 5:
                five = five - value;
                break;
            case 10:
                ten = ten - value;
                break;
            case 20:
                twenty = twenty - value;
                break;
            case 50:
                fifty = fifty - value;
                break;
        }
    }


    public int getId() {
        return id;
    }
}
