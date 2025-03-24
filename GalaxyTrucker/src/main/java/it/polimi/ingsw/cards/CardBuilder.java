package it.polimi.ingsw.cards;

/**
 * Class that is used to initialize a card following the builder
 * pattern. Every class representing a card takes it as an input
 * for initialization. Used to create a card with recursive calls like
 * cardBuilder.buildCardLevel(cardLevel).eccetera.getBuiltCard();
 * @author carlo
 */

class CardBuilder {

    private Card builtCard;
    public ElementType blowType, requirementType, lossType;
    public int daysLost, gainedCredit, requirementNumber, cardLevel, lossNumber;
    public int[] goods, planet1, planet2, planet3, planet4;
    public Blow blows[];
    public String cardName;

    private CardBuilder(){}

    public CardBuilder buildCardLevel(int cardLevel){
        this.cardLevel = cardLevel;
        return this;
    }

    public CardBuilder buildCardName(String cardName){
        this.cardName = cardName;
        return this;
    }

    public CardBuilder buildBlowType(ElementType blowType){
        this.blowType = blowType;
        return this;
    }


    public CardBuilder buildRequirementType(ElementType requirementType){
        this.requirementType = requirementType;
        return this;
    }

    public CardBuilder buildLossType(ElementType lossType){
        this.lossType = lossType;
        return this;
    }

    public CardBuilder buildDaysLost(int daysLost){
        this.daysLost = daysLost;
        return this;
    }

    public CardBuilder buildGainedCredit(int gainedCredit){
        this.gainedCredit = gainedCredit;
        return this;
    }

    public CardBuilder buildRequirementNumber(int requirementNumber){
        this.requirementNumber = requirementNumber;
        return this;
    }

    public CardBuilder buildGoods(int[] goods){
        this.goods = goods;
        return this;
    }

    public CardBuilder buildBlows(Blow[] blows){
        this.blows = blows;
        return this;
    }

    public CardBuilder buildPlanets(int[] planet1, int[] planet2, int[] planet3, int[] planet4){
        this.planet1 = planet1;
        this.planet2 = planet2;
        this.planet3 = planet3;
        this.planet4 = planet4;
        return this;
    }

    public Card getBuiltCard(){

        if(cardName.equals("MeteorSwarm")){
            builtCard = new MeteorSwarm(this);
        }
        else if(cardName.equals("Pirates")){
            builtCard = new Pirates(this);
        }
        else if(cardName.equals("CombatZone")){
            builtCard = new CombatZone(this);
        }
        else if(cardName.equals("AbbandonedShip")){
            builtCard = new AbbandonedShip(this);
        }
        else if(cardName.equals("AbbandonedStation")){
            builtCard = new AbbandonedStation(this);
        }
        else if(cardName.equals("Epidemic")){
            builtCard = new Epidemic(this);
        }
        else if(cardName.equals("OpenSpace")){
            builtCard = new OpenSpace(this);
        }
        else if(cardName.equals("Planets")){
            builtCard = new Planets(this);
        }
        else if(cardName.equals("Sabotage")){
            builtCard = new Sabotage(this);
        }
        else if(cardName.equals("Slavers")){
            builtCard = new Slavers(this);
        }
        else if(cardName.equals("Smugglers")){
            builtCard = new Smugglers(this);
        }
        else{
            builtCard = new StarDust(this);
        }

        return builtCard;

    }

}
