package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that represents storage component
 *
 * @author Ludo
 */

public class Storage extends Component {

    private boolean isRed;
    private int numberOfMaximumElements;
    private int[] goods = new int[4];

    public Storage() {
    }

    @JsonCreator
    public Storage(@JsonProperty("sides") SideType[] sides, @JsonProperty("isRed") boolean isRed, @JsonProperty("numberOfMaximumElements") int numberOfMaximumElements) {

        super(sides);
        this.isRed = isRed;
        this.numberOfMaximumElements = numberOfMaximumElements;
    }

    public String getComponentName() {
        return "Storage";
    }

    @Override
    public int getAvailableRedSlots() {
        if (isRed)
            return numberOfMaximumElements - goods[0] - goods[1] - goods[2] - goods[3];
        return 0;
    }

    @Override
    public int getAvailableBlueSlots() {
        if (!isRed) {
            return numberOfMaximumElements - goods[1] - goods[2] - goods[3];
        }
        return 0;
    }

    @Override
    public <T, E extends Exception> T accept(ComponentVisitor<T, E> componentVisitor) throws E {
        return componentVisitor.visitStorage(this);
    }

    public boolean isRed() {
        return isRed;
    }

    public boolean isEmpty() {
        if (goods[0] == 0 && goods[1] == 0 && goods[2] == 0 && goods[3] == 0) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (goods[0] + goods[1] + goods[2] + goods[3] == numberOfMaximumElements) {
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

}
