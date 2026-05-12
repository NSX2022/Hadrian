package utils;

public enum Constants {
    MAX_PACKET_BYTES(568),
    MIN_CERTAINTY(100),
    HIGH_CERTAINTY(256),

    ;

    private final int value;

    Constants(int value) {
        this.value = value;    // store it
    }

    public int value() {       // expose it
        return value;
    }
}