package it.polimi.ingsw.shipboard;


import it.polimi.ingsw.components.*;
import it.polimi.ingsw.components.Component;

import java.awt.*;

public class ShipStructure{
    // Matrix representing the ship's component layout
    Component[][] structureMatrix;
    // Boolean matrix indicating valid positions for components
    private boolean[][] matr;

    ShipBoard shipBoard;

    /**
     * Constructor for ShipStructure.
     * Initializes the ship's structure matrix and determines valid component placement
     * based on the game type.
     *
     * @param gameType The type of game being played.
     * @param shipBoard The ship board associated with the structure.
     * @author Giacomo
     */
    public ShipStructure(GameType gameType, ShipBoard shipBoard) {
        this.structureMatrix = new Component[12][12];
        this. matr = new boolean[12][12];
        this.shipBoard = shipBoard;
        // Initialize all positions as valid
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                matr[i][j] = true;
            }
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                structureMatrix[i][j] = null;
            }
        }

        if (gametype.equals(GameType.GameTest)){}
        else{
            // Set forbidden zones in the structure
            for(int i = 0; i < 12; i++){
                matr[0][i] = false;
                matr[1][i] = false;
                matr[2][i] = false;
                matr[10][i] = false;
                matr[11][i] = false;

            }
            for(int i = 0; i < 12; i++){
                matr[i][0] = false;
                matr[i][1] = false;
                matr[i][2] = false;
                matr[i][3] = false;
                matr[i][9] = false;
                matr[i][10] = false;
                matr[i][11] = false;
            }

            matr[3][4] = false;
            matr[4][4] = false;
            matr[3][5] = false;
            matr[6][4] = false;
            matr[6][8] = false;
            matr[8][4] = false;
            matr[9][4] = false;
            matr[9][5] = false;
        }
    }

    /**
     * Adds a component to the specified position in the structure matrix.
     *
     * @param component The component to add.
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @author Giacomo
     */
    public void addComponent(Component component, int x, int y) {
        x = x-1;
        y = y-1;
        if (matr[x][y] == true) {
            structureMatrix[x][y] = component;
        }
    }

    /**
     * Removes a component from the specified position.
     * Updates the shipBoard to reflect the destroyed components.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @author Giacomo
     */
    public void removeComponent(int x, int y) {
        boolean flag = true;

        if (matr[x][y] == true && structureMatrix[x][y] != null) {
            structureMatrix[x][y] = null;
            shipBoard.updateDestroyedComponents(1);
            while(flag){
                flag = checkNotReachable(this.shipBoard);
            }
        }
    }

    /**
     * Counts the number of external junctions in the ship structure.
     * External junctions occur when a component has a connection point
     * facing an empty space.
     *
     * @return The number of external junctions.
     * @author Giacomo
     */
    public int countExternalJunctions(){
        int externalJunctions = 0;
        for(int i = 1; i < 11; i++){
            for(int j = 1; j < 11; j++){
                if (structureMatrix[i][j] != null){
                    //va sistemato il fatto che qualora si volesse davvero usare un enum allora dovrebbe essere messo tipodiverso da vuoto e diverso da shield
                    if(!(structureMatrix[i][j].getLeft().equals(SideType.Smooth) && structureMatrix[i-1][j] == null)){
                        externalJunctions++;
                    }
                    if(!(structureMatrix[i][j].getRight().equals(SideType.Smooth) && structureMatrix[i+1][j] == null)){
                        externalJunctions++;
                    }
                    if(!(structureMatrix[i][j].getFront().equals(SideType.Smooth) && structureMatrix[i][j-1] == null)){
                        externalJunctions++;
                    }
                    if(!(structureMatrix[i][j].getBack().equals(SideType.Smooth) && structureMatrix[i][j+1] == null)){
                        externalJunctions++;
                    }
                }
            }
        }
        return externalJunctions;
    }

    /**
     * Scans the ship structure to identify errors related to incorrect junctions.
     * Errors are detected and counted but not automatically corrected.
     *
     * The function iterates through the structure matrix and:
     * 1. Verifies if components are correctly connected.
     * 2. Checks if the "Engine" component is incorrectly placed.
     * 3. Ensures "Cannon" components follow specific placement rules.
     *
     * @param shipBoard The ShipBoard object that tracks errors.
     * @return The total number of detected errors.
     * @author Giacomo
     */
    public int checkErrors(ShipBoard shipBoard){
        boolean flag = true;
        int errors = 0;
        while(flag){
            flag = false;
            for (int i =1; i < 12; i++) {
                for (int j = 1; j < 12; j++) {
                    if (structureMatrix[i][j] != null) {
                        if(checkCorrectJunctions(i,j)){
                            System.out.println("Component"+ i+" "+j+" is not well connected");
                            errors++;
                            //  removeComponent(i, j); i componenti non li deve rimuovere il gioco ma l'utente
                            flag = checkNotReachable(shipBoard);
                        }
                        if(structureMatrix[i][j].getComponentName().equals("Engine")){
                            boolean check = false;
                            for(int k = j+1; k < 12; k++){
                                if(structureMatrix[i][k] != null){
                                    check = true;
                                    errors++;
                                }
                            }
                            if(check){
                                System.out.println("Error, in component" + i +' '+ j);
                            }
                        }
                        if(structureMatrix[i][j].getComponentName().equals("Cannon")){
                            boolean check = false;
                            if(structureMatrix[i][j].getLeft().equals(SideType.Special)){
                                for(int k = i-1; k >= 0; k--){
                                    if(structureMatrix[k][j] != null){
                                        check = true;
                                        errors++;
                                    }
                                }
                                if(check){
                                    System.out.println("Error, in component" + i +' '+ j);
                                }
                            }
                            else if(structureMatrix[i][j].getRight().equals(SideType.Special)){
                                for(int k = i+1; k < 12; k++){
                                    if(structureMatrix[k][j] != null){
                                        check = true;
                                        errors++;
                                    }
                                }
                                if(check){
                                    System.out.println("Error, in component" + i +' '+ j);
                                }
                            }
                            else if(structureMatrix[i][j].getFront().equals(SideType.Special)){
                                for(int k = j-1; k >= 0; k--){
                                    if(structureMatrix[i][k] != null){
                                        check = true;
                                        errors++;
                                    }
                                }
                                if(check){
                                    System.out.println("Error, in component" + i +' '+ j);
                                }
                            }
                            else if(structureMatrix[i][j].getBack().equals(SideType.Special)){
                                for(int k = j+1; k < 12; k++){
                                    if(structureMatrix[k][j] != null){
                                        check = true;
                                        errors++;
                                    }
                                }
                                if(check){
                                    System.out.println("Error, in component" + i +' '+ j);
                                }
                            }
                        }
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Checks if any components are not connected to the main structure.
     * If a component is unreachable, it is removed.
     *
     * @param shipBoard The ShipBoard object that tracks destroyed components.
     * @return True if any unreachable components were found, false otherwise.
     * @author Giacomo
     */
    public boolean checkNotReachable(ShipBoard shipBoard){
        boolean result = false;
        int flag = 1;
        boolean[][] mat = new boolean[12][12];
        while(flag == 1) {
            flag = 0;
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    mat[i][j] = false;
                }
            }

            goDownChecking(6, 6, mat);
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    if (structureMatrix[i][j] != null) {
                        if (!mat[i][j]) {
                            flag = 1;
                            result = true;
                            removeComponent(i, j);
                            shipBoard.updateDestroyedComponents(1);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Recursively marks reachable components.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param mat The boolean matrix tracking visited positions.
     * @author Giacomo
     */
    private void goDownChecking(int x, int y, boolean[][] mat){
        mat[x][y] = true;
        if(structureMatrix[x+1][y] != null ){
            if((structureMatrix[x][y].getRight().equals(SideType.Single) && (structureMatrix[x+1][y].getLeft().equals(SideType.Single)|| structureMatrix[x+1][y].getLeft().equals(SideType.Universal))) || (structureMatrix[x][y].getRight().equals(SideType.Double) && (structureMatrix[x+1][y].getLeft().equals(SideType.Double) || structureMatrix[x+1][y].getLeft().equals(SideType.Universal)))){
                goDownChecking(x+1, y, mat);
            }
            if((structureMatrix[x][y].getLeft().equals(SideType.Single) && (structureMatrix[x-1][y].getRight().equals(SideType.Single) || structureMatrix[x-1][y].getRight().equals(SideType.Universal))) || (structureMatrix[x][y].getLeft().equals(SideType.Double) && (structureMatrix[x-1][y].getRight().equals(SideType.Double) || structureMatrix[x-1][y].getRight().equals(SideType.Universal)))){
                goDownChecking(x-1, y, mat);
            }
            if((structureMatrix[x][y].getBack().equals(SideType.Single) && (structureMatrix[x][y+1].getFront().equals(SideType.Single) || structureMatrix[x][y+1].getFront().equals(SideType.Universal))) || (structureMatrix[x][y].getBack().equals(SideType.Double) && (structureMatrix[x][y+1].getFront().equals(SideType.Double) || structureMatrix[x][y+1].getFront().equals(SideType.Universal)))){
                goDownChecking(x, y+1, mat);
            }
            if((structureMatrix[x][y].getFront().equals(SideType.Single) && (structureMatrix[x][y-1].getBack().equals(SideType.Single) || structureMatrix[x][y-1].getBack().equals(SideType.Universal))) || (structureMatrix[x][y].getFront().equals(SideType.Double) && (structureMatrix[x][y-1].getBack().equals(SideType.Double) || structureMatrix[x][y-1].getBack().equals(SideType.Universal)))){
                goDownChecking(x, y-1, mat);
            }
        }
    }

    /**
     * Checks if a component has correct junctions with its neighboring components.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @return True if the junctions are correct, false otherwise.
     * @author Giacomo
     */
    private boolean checkCorrectJunctions(int x, int y){
        if (structureMatrix[x][y] != null) {
            if((structureMatrix[x][y].getLeft().equals(SideType.Single) && structureMatrix[x-1][y] != null && (!structureMatrix[x-1][y].getRight().equals(SideType.Single) && !structureMatrix[x-1][y].getRight().equals(SideType.Universal))) || (structureMatrix[x][y].getLeft().equals(SideType.Double) && structureMatrix[x-1][y] != null && (!structureMatrix[x-1][y].getRight().equals(SideType.Single) && !structureMatrix[x-1][y].getRight().equals(SideType.Universal))) ||
                    (structureMatrix[x][y].getFront().equals(SideType.Double) && structureMatrix[x][y-1] != null && (!structureMatrix[x][y-1].getBack().equals(SideType.Single) && !structureMatrix[x][y-1].getBack().equals(SideType.Universal))) || (structureMatrix[x][y].getFront().equals(SideType.Single) && structureMatrix[x][y-1] != null && (!structureMatrix[x][y-1].getBack().equals(SideType.Double) && !structureMatrix[x][y-1].getBack().equals(SideType.Universal))) ||
                    (structureMatrix[x][y].getRight().equals(SideType.Single) && structureMatrix[x+1][y] != null && (!structureMatrix[x+1][y].getLeft().equals(SideType.Single) && !structureMatrix[x+1][y].getLeft().equals(SideType.Universal))) || (structureMatrix[x][y].getRight().equals(SideType.Double) && structureMatrix[x+1][y] != null && (!structureMatrix[x+1][y].getLeft().equals(SideType.Single) && !structureMatrix[x+1][y].getLeft().equals(SideType.Universal))  ||
                    (structureMatrix[x][y].getBack().equals(SideType.Double) && structureMatrix[x][y+1] != null && (!structureMatrix[x][y+1].getFront().equals(SideType.Single) && !structureMatrix[x][y+1].getFront().equals(SideType.Universal))) || (structureMatrix[x][y].getBack().equals(SideType.Single) && structureMatrix[x][y+1] != null && (!structureMatrix[x][y+1].getFront().equals(SideType.Double) && !structureMatrix[x][y+1].getFront().equals(SideType.Universal))))){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds goods to a component at a given position if it is a storage component.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @param goods An array representing the goods to be added.
     * @author Giacomo
     */
    public void addGoods(int x, int y, int[] goods){
        if(structureMatrix[x][y] != null && structureMatrix[x][y].getComponentName().equals("Storage")){
            x = x-1;
            y = y-1;
            checkSlots(goods, x, y);
            ((Storage) structureMatrix[x][y]).addGoods(goods);
        }
    }




    /**
     * Sets the crew type in a cabin component, ensuring compatibility with alien support components.
     *
     * @param crewType The type of crew to assign.
     * @param x The x-coordinate of the cabin.
     * @param y The y-coordinate of the cabin.
     * @author Giacomo
     */
    public void setCrewType(CrewType crewType, int x, int y){
        x = x-1;
        y = y-1;
        if(structureMatrix[x][y] != null && structureMatrix[x][y].getComponentName().equals("Cabin")){
            if(crewType.equals(CrewType.Brown)){
                if((structureMatrix[x-1][y].getComponentName().equals("AlienSupport") && !((AlienSupport)structureMatrix[x-1][y]).isPurple()) ||
                        (structureMatrix[x+1][y].getComponentName().equals("AlienSupport") && !((AlienSupport)structureMatrix[x+1][y]).isPurple()) ||
                        (structureMatrix[x][y-1].getComponentName().equals("AlienSupport") && !((AlienSupport)structureMatrix[x][y-1]).isPurple()) ||
                        (structureMatrix[x][y+1].getComponentName().equals("AlienSupport") && !((AlienSupport)structureMatrix[x][y+1]).isPurple())){
                    ((Cabin)structureMatrix[x][y]).setCrewType(crewType);
                }
                else{
                    System.out.println("CrewType not permitted");
                }
            }
            else{
                if((structureMatrix[x-1][y].getComponentName().equals("AlienSupport") && ((AlienSupport)structureMatrix[x-1][y]).isPurple()) ||
                        (structureMatrix[x+1][y].getComponentName().equals("AlienSupport") && ((AlienSupport)structureMatrix[x+1][y]).isPurple()) ||
                        (structureMatrix[x][y-1].getComponentName().equals("AlienSupport") && ((AlienSupport)structureMatrix[x][y-1]).isPurple()) ||
                        (structureMatrix[x][y+1].getComponentName().equals("AlienSupport") && ((AlienSupport)structureMatrix[x][y+1]).isPurple())){
                    ((Cabin)structureMatrix[x][y]).setCrewType(crewType);
                }
                else{
                    System.out.println("CrewType not permitted");
                }
            }
        }
        else{
            System.out.println("ERROR, this component is not a cabin");
        }
    }

    /**
     * Checks if the storage component at a given position has enough space for the goods.
     *
     * @param goods The array representing the goods to be checked.
     * @param x The x-coordinate of the storage component.
     * @param y The y-coordinate of the storage component.
     * @return True if the goods fit, false otherwise.
     * @author Giacomo
     */
    private boolean checkSlots(int[] goods, int x, int y){
        if(structureMatrix[x][y] != null && structureMatrix[x][y].getComponentName().equals("Storage")){
            if(((Storage)structureMatrix[x][y]).getIsRed()){
                if(goods[0] <= ((Storage)structureMatrix[x][y]).getNumberOfMaximumElements() - ((Storage)structureMatrix[x][y]).getGoods()[0]){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                if(goods[0] <= ((Storage)structureMatrix[x][y]).getNumberOfMaximumElements() - ((Storage)structureMatrix[x][y]).getGoods()[0]-  ((Storage)structureMatrix[x][y]).getGoods()[3] - ((Storage)structureMatrix[x][y]).getGoods()[2]){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }
}
