package ch.graueenergie.energieclash.view.util.i2c;

public enum I2CAddress {
    I2C1(1),
    I2C2(1 << 1),
    I2C3(1 << 2),
    I2C4(1 << 3),
    I2C5(1 << 4),
    I2C6(1 << 5),
    I2C7(1 << 6),
    I2C8(1 << 7);
    private final int address;

    I2CAddress(int address) {
        this.address = address;
    }
    public int getAddress() {
        return address;
    }
}
