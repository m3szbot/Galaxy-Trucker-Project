package it.polimi.ingsw.Controller.Cards;

/**
 * Class that is used to initialize a card following the builder
 * pattern. Every class representing a card takes it as an input
 * for initialization. Used to create a card with recursive calls like
 * cardBuilder.buildCardLevel(cardLevel).eccetera.getBuiltCard();
 *
 * @author carlo
 */

public class CardBuilder {

    private Card builtCard;
    private ElementType blowType, requirementType, lossType;
    private int daysLost, gainedCredits, requirementNumber, cardLevel, lossNumber;
    private int[] goods, planet1, planet2, planet3, planet4;
    private Blow[] blows;
    private String cardName;
    private String imagePath;


    public CardBuilder buildCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
        return this;
    }

    public CardBuilder buildCardName(String cardName) {
        this.cardName = cardName;
        return this;
    }

    public CardBuilder buildBlowType(ElementType blowType) {
        this.blowType = blowType;
        return this;
    }


    public CardBuilder buildRequirementType(ElementType requirementType) {
        this.requirementType = requirementType;
        return this;
    }

    public CardBuilder buildLossType(ElementType lossType) {
        this.lossType = lossType;
        return this;
    }

    public CardBuilder buildDaysLost(int daysLost) {
        this.daysLost = daysLost;
        return this;
    }

    public CardBuilder buildGainedCredits(int gainedCredits) {
        this.gainedCredits = gainedCredits;
        return this;
    }

    public CardBuilder buildRequirementNumber(int requirementNumber) {
        this.requirementNumber = requirementNumber;
        return this;
    }

    public CardBuilder buildLossNumber(int lossNumber) {
        this.lossNumber = lossNumber;
        return this;
    }

    public CardBuilder buildGoods(int[] goods) {
        this.goods = goods;
        return this;
    }

    public CardBuilder buildBlows(Blow[] blows) {
        this.blows = blows;
        return this;
    }

    //If a planet is not present on the card, just pass the null value

    public CardBuilder buildPlanets(int[] planet1, int[] planet2, int[] planet3, int[] planet4) {
        this.planet1 = planet1;
        this.planet2 = planet2;
        this.planet3 = planet3;
        this.planet4 = planet4;
        return this;
    }

    public CardBuilder buildCardImage(String image) {
        this.imagePath = image;
        return this;
    }

    public Card getBuiltCard() {

        if (cardName.equals("SmallMeteorSwarm") || cardName.equals("BigMeteorSwarm")) {
            builtCard = new MeteorSwarm(this);
        } else if (cardName.equals("Pirates")) {
            builtCard = new Pirates(this);
        } else if (cardName.equals("CombatZone")) {
            builtCard = new CombatZone(this);
        } else if (cardName.equals("AbandonedShip")) {
            builtCard = new AbandonedShip(this);
        } else if (cardName.equals("AbandonedStation")) {
            builtCard = new AbandonedStation(this);
        } else if (cardName.equals("Epidemic")) {
            builtCard = new Epidemic(this);
        } else if (cardName.equals("OpenSpace")) {
            builtCard = new OpenSpace(this);
        } else if (cardName.equals("Planets")) {
            builtCard = new Planets(this);
        } else if (cardName.equals("Slavers")) {
            builtCard = new Slavers(this);
        } else if (cardName.equals("Smugglers")) {
            builtCard = new Smugglers(this);
        } else {
            builtCard = new StarDust(this);
        }

        return builtCard;

    }

    public ElementType getBlowType() {
        return blowType;
    }

    public ElementType getRequirementType() {
        return requirementType;
    }

    public ElementType getLossType() {
        return lossType;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getGainedCredits() {
        return gainedCredits;
    }

    public int getRequirementNumber() {
        return requirementNumber;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public int getLossNumber() {
        return lossNumber;
    }

    public int[] getGoods() {
        return goods;
    }

    public int[] getPlanet1() {
        return planet1;
    }

    public int[] getPlanet2() {
        return planet2;
    }

    public int[] getPlanet3() {
        return planet3;
    }

    public int[] getPlanet4() {
        return planet4;
    }

    public Blow[] getBlows() {
        return blows;
    }

    public String getCardName() {
        return cardName;
    }

    public String getImagePath() {
        return imagePath;
    }
}
