package banking;

public class Account implements java.io.Serializable{
    private long id;
    private long customerId;
    private long count;

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setCustomerId(long id) {
        this.customerId = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCount(long c) {
        this.count = c;
    }
    public long getCount() {
        return count;
    }
}
