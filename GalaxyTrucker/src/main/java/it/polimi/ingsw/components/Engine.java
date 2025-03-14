package it.polimi.ingsw.components;

/**
 * Class that represents an Engine
 * @author carlo
 */

public class Engine extends Component{

   private boolean single;

   public Engine(SideType[] sides, boolean single){
       super(sides);
       this.single = single;
   }

    /**
     *
     * @return true if the engine is single
     */

    public boolean isSingle() {
        return single;
    }

    @Override
    public String getComponentName(){
       return "Engine";
    }
}
