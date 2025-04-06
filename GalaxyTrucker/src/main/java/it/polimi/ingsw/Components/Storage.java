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


    }

    //Todo

    public boolean isFull(){


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
