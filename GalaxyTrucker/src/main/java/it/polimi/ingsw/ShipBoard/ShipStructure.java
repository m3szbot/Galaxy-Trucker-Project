package it.polimi.ingsw.ShipBoard;


import java.awt.*;

public class ShipStructure{
    // Matrix representing the ship's component layout
    Component[][] structureMatrix;
    // Boolean matrix indicating valid positions for components
    private boolean matr[][];

    /**
     * Constructor for ShipStructure.
     * Initializes the ship's structure matrix and determines valid component placement
     * based on the game type.
     *
     * @param gameType The type of game being played.
     */
    public ShipStructure(GameType gameType) {
        if (gametype.equals(GameType.+GameTest)){
            structureMatrix = new Component[12][12];
            matr = new boolean[12][12];
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
        }
        else{
            structureMatrix = new Component[12][12];
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
        }
    }

    /**
     * Adds a component to the specified position in the structure matrix.
     *
     * @param component The component to add.
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     */
    public void addComponent(Component component, int x, int y) {
        if (matr[x][y] == true) {
            structureMatrix[x][y] = component;
            component.implement();
        }
    }

    /**
     * Removes a component from the specified position.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @param shipBoard The ShipBoard object that keeps track of destroyed components.
     */
    public void removeComponent(int x, int y, ShipBoard shipBoard) {
        if (matr[x][y] == true && structureMatrix[x][y] != null) {
            shipBoard.
            structureMatrix[x][y] = null;
        }

    }

    /**
     * Counts the number of external junctions in the ship structure.
     * External junctions occur when a component has a connection point
     * facing an empty space.
     *
     * @return The number of external junctions.
     */
    public int countExternalJunctions(){
        int externalJunctions = 0;
        for(int i = 1; i < 11; i++){
            for(int j = 1; j < 11; j++){
                if (structureMatrix[i][j] != null){
                    //va sistemato il fatto che qualora si volesse davvero usare un enum allora dovrebbe essere messo tipodiverso da vuoto e diverso da shield
                    if((structureMatrix[i][j].left != 0 && structureMatrix[i-1][j] == null){
                        externalJunctions++;
                    }
                    if((structureMatrix[i][j].right != 0 && structureMatrix[i+1][j] == null)){
                        externalJunctions++;
                    }
                    if((structureMatrix[i][j].front != 0 && structureMatrix[i][j-1] == null)){
                        externalJunctions++;
                    }
                    if((structureMatrix[i][j].back != 0 && structureMatrix[i][j+1] == null)){
                        externalJunctions++;
                    }
                }
            }
        }
        return externalJunctions;
    }
    //returna il numero di errori in modo che possano essere conteggiati da la shipBoard

    //questo metodo è sbagliato deve solo checkare e segnalare l'eroore perchè è il giocatore che va a rimuovere i compneenti che vuole per toglierlo
    /**
     * Checks for errors in the ship structure.
     * Errors include incorrect junctions between components.
     *
     * @param shipBoard The ShipBoard object that tracks errors.
     * @return The number of errors detected.
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
                            errors++;
                          //  removeComponent(i, j); i componenti non li deve rimuovere il gioco ma l'utente
                            flag = checkNotReachable(shipBoard);
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
     */
    public boolean checkNotReachable(ShipBoard shipBoard){
        boolean result = false;
        int flag = 1;
        boolean mat[][] = new boolean[12][12];
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
     */
    private void goDownChecking(int x, int y, boolean[][] mat){
        mat[x][y] = true;
        if(structureMatrix[x+1][y] != null ){
            if((structureMatrix[x][y].right == 1 && (structureMatrix[x+1][y].left == 1 || structureMatrix[x+1][y].left == 3)) || (structureMatrix[x][y].right == 2 && (structureMatrix[x+1][y].left == 2 || structureMatrix[x+1][y].left == 3))){
                goDownChecking(x+1, y, mat);
            }
            if((structureMatrix[x][y].left == 1 && (structureMatrix[x-1][y].right == 1 || structureMatrix[x-1][y].left == 3)) || (structureMatrix[x][y].left == 2 && (structureMatrix[x-1][y].right == 2 || structureMatrix[x-1][y].right == 3))){
                goDownChecking(x-1, y, mat);
            }
            if((structureMatrix[x][y].back == 1 && (structureMatrix[x][y+1].front == 1 || structureMatrix[x][y+1].front == 3)) || (structureMatrix[x][y].back == 2 && (structureMatrix[x][y+1].front == 2 || structureMatrix[x][y+1].front == 3))){
                goDownChecking(x, y+1, mat);
            }
            if((structureMatrix[x][y].front == 1 && (structureMatrix[x][y-1].back == 1 || structureMatrix[x][y-1].back == 3)) || (structureMatrix[x][y].front == 2 && (structureMatrix[x][y-1].back == 2 || structureMatrix[x][y-1].back == 3))){
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
     */
    private boolean checkCorrectJunctions(int x, int y){
        if (structureMatrix[x][y] != null) {
            if((structureMatrix[x][y].left == 1 && structureMatrix[x-1][y] != null && (structureMatrix[x-1][y].right != 1 && structureMatrix[x-1][y].right != 3)) || (structureMatrix[x][y].left == 2 && structureMatrix[x-1][y] != null && (structureMatrix[x-1][y].right != 1 && structureMatrix[x-1][y].right != 3)) ||
                    (structureMatrix[x][y].front == 2 && structureMatrix[x][y-1] != null && (structureMatrix[x][y-1].back != 1 && structureMatrix[x][y-1].back != 3)) || (structureMatrix[x][y].front == 1 && structureMatrix[x][y-1] != null && (structureMatrix[x][y-1].back != 2 && structureMatrix[x][y-1].back != 3)) ||
                    (structureMatrix[x][y].right == 1 && structureMatrix[x+1][y] != null && (structureMatrix[x+1][y].left != 1 && structureMatrix[x+1][y].left != 3)) || (structureMatrix[x][y].right == 2 && structureMatrix[x+1][y] != null && (structureMatrix[x+1][y].left != 1 && structureMatrix[x+1][y].left != 3)  ||
                    (structureMatrix[x][y].back == 2 && structureMatrix[x][y+1] != null && (structureMatrix[x][y+1].front != 1 && structureMatrix[x][y+1].front != 3)) || (structureMatrix[x][y].back == 1 && structureMatrix[x][y+1] != null && (structureMatrix[x][y+1].front != 2 && structureMatrix[x][y+1].front != 3)))){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds goods to a component at a given position.
     */
    public void addGoods(int x, int y, int[] goods){
        if(goods[0] != 0){
            structureMatrix[x][y].setRed(goods[0]);
        }
        if(goods[1] != 0){
            structureMatrix[x][y].setBlue(goods[1]);
        }
        if(goods[2] != 0){
            structureMatrix[x][y].setGreen(goods[2]);
        }
        if(goods[3] != 0){
            structureMatrix[x][y].setYellow(goods[2]);
        }
    }

}
