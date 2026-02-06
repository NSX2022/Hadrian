package networking;
import utils.Primes;

public class Receiver {
    //Whether or not to listen on the port
    private boolean open = false;



    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
