package it.polimi.ingsw.components;

/**
 * Class that represent the shield component
 * @author carlo
 */

public class Shield extends Component{

    private int coveredSide1, coveredSide2;

    public Shield(SideType[] sides){
        super(sides);
        setCoveredSides();
    }

    /**
     * private method that sets the two private attributes
     */

    private void setCoveredSides(){

       if(getFront() == SideType.Special){
           coveredSide1 = 0;

           if(getRight() == SideType.Special){
               coveredSide2 = 1;
           }
           else if(getBack() == SideType.Special){
               coveredSide2 = 2;
           }
           else if(getLeft() == SideType.Special){
               coveredSide2 = 3;
           }
       }
       else if(getRight() == SideType.Special){
           coveredSide1 = 1;

           if(getBack() == SideType.Special){
               coveredSide2 = 2;
           }
           else if(getLeft() == SideType.Special){
               coveredSide2 = 3;
           }
       }
       else if(getBack() == SideType.Special){
           coveredSide1 = 2;
           coveredSide2 = 3;
       }

    }

    public int getCoveredSide1() {
        return coveredSide1;
    }

    public int getCoveredSide2() {
        return coveredSide2;
    }

    @Override
    public String getComponentName(){
        return "Shield";
    }

    @Override
    public int[] getCoveredSides(){
        int[] sides = new int[4];
        int i;
        for(i = 0; i < sides.length; i++){
            if(i==coveredSide1 || i==coveredSide2){
                sides[i] = 1;
            }else{
                sides[i] = 0;
            }
        }
        return sides;
    }

}
