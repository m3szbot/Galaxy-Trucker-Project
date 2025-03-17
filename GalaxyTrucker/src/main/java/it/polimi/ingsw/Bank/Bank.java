package it.polimi.ingsw.Bank;

public class Bank {
    // constants (physical quantity)
    // red yellow green blue
    public final int[] maxGoods = {12, 17, 13, 14};
    public final int maxCredits = 150;
    public final int maxBatteries = 40;
    public final int maxAstronauts = 42;
    public final int maxPurpleAliens = 4;
    public final int maxBrownAliens = 4;

    // attributes
    private int[] goodsNumber;
    private int creditsNumber;
    private int batteriesNumber;
    private int astronautsNumber;
    private int purpleAliensNumber;
    private int brownAliensNumber;

    // constructor
    // attributes are not final to keep count of the quantities in the bank
    // pass aliens=0 if no aliens are used
    public Bank(int[] maxGoods, int maxCredits, int maxBatteries, int maxAstronauts,
                int maxPurpleAliens, int maxBrownAliens) {
        this.goodsNumber = new int[maxGoods.length];
        System.arraycopy(maxGoods, 0, this.goodsNumber, 0, maxGoods.length);
        this.creditsNumber = maxCredits;
        this.batteriesNumber = maxBatteries;
        this.astronautsNumber = maxAstronauts;
        this.purpleAliensNumber = maxPurpleAliens;
        this.brownAliensNumber = maxBrownAliens;
    }

    // removers
    public boolean[] removeGoods(int[] goods) {
        boolean[] returned = {true, true, true, true};
        for (int i = 0; i < 4; i++) {
            if (this.goodsNumber[i] - goods[i] < 0)
                returned[i] = false;
            else
                this.goodsNumber[i] -= goods[i];
        }
        return returned;
    }

    public boolean removeCredits(int credits) {
        if (this.creditsNumber - credits < 0)
            return false;
        this.creditsNumber -= credits;
        return true;
    }

    public boolean removeBatteries(int batteries) {
        if (this.batteriesNumber - batteries < 0)
            return false;
        this.batteriesNumber -= batteries;
        return true;
    }

    public boolean removeAstronauts(int astronauts) {
        if (this.astronautsNumber - astronauts < 0)
            return false;
        this.astronautsNumber -= astronauts;
        return true;
    }

    public boolean removePurpleAliens(int purpleAliens) {
        if (this.purpleAliensNumber - purpleAliens < 0)
            return false;
        this.purpleAliensNumber -= purpleAliens;
        return true;
    }

    public boolean removeBrownAliens(int brownAliens) {
        if (this.brownAliensNumber - brownAliens < 0)
            return false;
        this.brownAliensNumber -= brownAliens;
        return true;
    }

    // adders
    public void addGoods(int[] goods) {
        for (int i = 0; i < 4; i++) {
            this.goodsNumber[i] += goods[i];
        }
    }

    public void addCredits(int credits) {
        this.creditsNumber += credits;
    }

    public void addBatteries(int batteries) {
        this.batteriesNumber += batteries;
    }

    public void addAstronauts(int astronauts) {
        this.astronautsNumber += astronauts;
    }

    public void addPurpleAliens(int purpleAliens) {
        this.purpleAliensNumber += purpleAliens;
    }

    public void addBrownAliens(int brownAliens) {
        this.brownAliensNumber += brownAliens;
    }
}

