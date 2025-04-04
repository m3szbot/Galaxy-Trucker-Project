package it.polimi.ingsw.Bank;

import it.polimi.ingsw.Application.GameType;

/**
 * Bank class used to keep count of game inventory
 *
 * @author Boti
 */

public class Bank {
    // Goods: red yellow green blue
    private int[] goodsNumber;
    private int creditsNumber;
    private int batteriesNumber;
    private int astronautsNumber;
    private int purpleAliensNumber;
    private int brownAliensNumber;

    /**
     * Constructor
     * Aliens' number depends on game type
     *
     * @param gameType Type of game
     */
    public Bank(GameType gameType) {
        int[] maxGoods = {12, 17, 13, 14};
        int maxCredits = 150;
        int maxBatteries = 40;
        int maxAstronauts = 42;
        int maxPurpleAliens = 4;
        int maxBrownAliens = 4;
        if (gameType == GameType.TestGame) {
            maxPurpleAliens = 0;
            maxBrownAliens = 0;
        }
        // set instance attributes
        this.goodsNumber = new int[maxGoods.length];
        System.arraycopy(maxGoods, 0, this.goodsNumber, 0, maxGoods.length);
        this.creditsNumber = maxCredits;
        this.batteriesNumber = maxBatteries;
        this.astronautsNumber = maxAstronauts;
        this.purpleAliensNumber = maxPurpleAliens;
        this.brownAliensNumber = maxBrownAliens;
    }

    /**
     * Remove goods from bank inventory
     *
     * @param goods Array of the 4 good types containing the quantities to remove
     * @return returned Array of quantity violations of each good type - change to exceptions
     */
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

    /**
     * Remove credits from bank inventory
     *
     * @param credits number of credits to remove from bank inventory
     * @return false if quantity violated - change to exception
     */
    public boolean removeCredits(int credits) {
        if (this.creditsNumber - credits < 0)
            return false;
        this.creditsNumber -= credits;
        return true;
    }

    /**
     * Remove batteries from bank inventory
     *
     * @param batteries number of batteries to remove from bank inventory
     * @return false if quantity violated - exception
     */
    public boolean removeBatteries(int batteries) {
        if (this.batteriesNumber - batteries < 0)
            return false;
        this.batteriesNumber -= batteries;
        return true;
    }

    /**
     * Remove astronauts from bank inventory
     *
     * @param astronauts number of astronauts to remove from bank inventory
     * @return false if quantity violated - exception
     */
    public boolean removeAstronauts(int astronauts) {
        if (this.astronautsNumber - astronauts < 0)
            return false;
        this.astronautsNumber -= astronauts;
        return true;
    }

    /**
     * Remove purple aliens from bank inventory
     *
     * @param purpleAliens number of purple aliens to remove from bank inventory
     * @return false if quantity violated - exception
     */
    public boolean removePurpleAliens(int purpleAliens) {
        if (this.purpleAliensNumber - purpleAliens < 0)
            return false;
        this.purpleAliensNumber -= purpleAliens;
        return true;
    }

    /**
     * Remove brown aliens from bank inventory
     *
     * @param brownAliens number of brown aliens to remove from bank inventory
     * @return false if quantity violated
     */
    public boolean removeBrownAliens(int brownAliens) {
        if (this.brownAliensNumber - brownAliens < 0)
            return false;
        this.brownAliensNumber -= brownAliens;
        return true;
    }

    /**
     * Add goods to bank inventory
     *
     * @param goods Array of number of goods to add to bank inventory
     */
    public void addGoods(int[] goods) {
        for (int i = 0; i < 4; i++) {
            this.goodsNumber[i] += goods[i];
        }
    }

    /**
     * Add credits to bank inventory
     *
     * @param credits Number of credits to add to bank inventory
     */
    public void addCredits(int credits) {
        this.creditsNumber += credits;
    }

    /**
     * Add batteries to bank inventory
     *
     * @param batteries Number of batteries to add to bank inventory
     */
    public void addBatteries(int batteries) {
        this.batteriesNumber += batteries;
    }

    /**
     * Add astronauts to bank inventory
     *
     * @param astronauts Number of astronauts to add to bank inventory
     */
    public void addAstronauts(int astronauts) {
        this.astronautsNumber += astronauts;
    }

    /**
     * Add purple aliens to bank inventory
     *
     * @param purpleAliens Number of purpleAliens to add to bank inventory
     */
    public void addPurpleAliens(int purpleAliens) {
        this.purpleAliensNumber += purpleAliens;
    }

    /**
     * Add brown aliens to bank inventory
     *
     * @param brownAliens Number of brownAliens to add to bank inventory
     */
    public void addBrownAliens(int brownAliens) {
        this.brownAliensNumber += brownAliens;
    }
}

