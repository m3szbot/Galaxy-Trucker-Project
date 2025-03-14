package it.polimi.ingsw.components;

/**
 * Class that represents storage component
 * @author Ludo
 */

public class Storage extends Component{

    private boolean type;
    private int numberOfMaximumElements;
    private int[] goods = new int[4];

    private String getComponentName(){
        return "Storage";
    }
    public boolean getType(){
        return type;
    }
    public int getNumberOfMaximumElements(){
        return numberOfMaximumElements;
    }
    public int[] getGoods(){
        return goods;
    }
    public void removeGoods(int[] newGoods){
        int i;
        for(i=0; i< goods.length; i++){
            goods[i]=goods[i]-newGoods[i];
        }
    }
    public void addGoods(int[] newGoods){
        int i;
        for(i=0; i< goods.length; i++){
            goods[i]=goods[i]+newGoods[i];
        }
    }
    public Storage(SideType[] sides, boolean type, int numberOfMaximumElements, int goods){

        super(sides);
        int i;
        this.type = type;
        this.numberOfMaximumElements = numberOfMaximumElements;
    }
}
