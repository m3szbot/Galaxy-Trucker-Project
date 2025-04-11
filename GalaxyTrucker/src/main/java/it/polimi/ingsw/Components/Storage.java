package it.polimi.ingsw.Components;

/**
 * Class that represents storage component
 *
 * @author Ludo
 */

public class Storage extends Component {

    private boolean isRed;
    private int numberOfMaximumElements;
    private int[] goods = new int[4];

    public String getComponentName() {
        return "Storage";
    }

    public boolean isRed() {
        return isRed;
    }

    //Todo

    public boolean isEmpty(){
        if(goods[0] == 0 && goods[1] == 0 && goods[2] == 0 && goods[3] == 0){
            return true;
        }
        return false;
    }

    //Todo

    public boolean isFull(){
        if(goods[0] + goods[1] + goods[2] + goods[3] == numberOfMaximumElements){
            return true;
        }
        return false;
    }

    public int getNumberOfMaximumElements() {
        return numberOfMaximumElements;
    }

    public int[] getGoods() {
        return goods;
    }

    public void removeGoods(int[] newGoods) {
        int i;
        for (i = 0; i < goods.length; i++) {
            goods[i] = goods[i] - newGoods[i];
        }
    }

    public void addGoods(int[] newGoods) {
        int i;
        for (i = 0; i < goods.length; i++) {
            goods[i] = goods[i] + newGoods[i];
        }
    }

    public Storage(SideType[] sides, boolean type, int numberOfMaximumElements, int goods) {

        super(sides);
        int i;
        this.isRed = type;
        this.numberOfMaximumElements = numberOfMaximumElements;
    }

    @Override
    public int getAvailableRedSlots(){
        if(isRed){
            //qua sta cosa va sistemata, forse devo fare get number of maximumElements e basta perchè quando va a zero come faccio a capire che è uno Storage
            return numberOfMaximumElements - goods[0];
        }
        return 0;
    }

    @Override
    public int  getAvailableBlueSlots(){
        if(isRed == false) {
            return numberOfMaximumElements - goods[1] - goods[2] - goods[3];
        }
        return 0;
    }
}
