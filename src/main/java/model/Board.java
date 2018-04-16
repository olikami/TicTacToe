package model;

import java.util.Arrays;

public class Board {

    //the board class.
    //will create a new board on create and
    //can do various stuff like prettyprint the current board
    private int[] i= new int[9];
    private Boolean locked = false;
    private int winner =0;

    // 0 = 1-4 row, column, diag, antidiag 5= tie. 1 =  0-3 row/column line.
    int[] winnerStroke= {0,0};
    public Board(){
       Arrays.fill(i,0);
    }
    Board(int[] list){

        for(int i = 0; i < list.length;i++)
            this.i[i]=list[i];
    }
    public int[] getBoardAsArray(){
        return this.i;
    }
    public void populateBoard(int position,int player) throws NumberFormatException,NoSuchFieldError{
        if(player!=1&&player!=2) throw new NumberFormatException("There is no player '"+player+"'. Possible Players are: 1,2");
        if(this.i[position]!=0) throw new NoSuchFieldError("Field "+position+1+" is already occupied.");
        else if(!Arrays.toString(this.i).contains("0")) throw new NoSuchFieldError("The Board is full!");

        this.i[position]=player;

        if(!Arrays.toString(this.i).contains("0"))
            this.locked = true;
    }

    public boolean PlaceIsEmpty(int i){
        return this.i[i]==0;
    }

    public Boolean isLocked() {
        return locked;
    }

    public int getWinner(){
        AI ai = new AI();
        if(ai.evaluateWinner(i)[0]!=0) {
            this.locked = true;
            winnerStroke[0]=ai.evaluateWinner(i)[1];
            winnerStroke[1]=ai.evaluateWinner(i)[2];
        }
        return ai.evaluateWinner(i)[0];
    }

    public int[] getWinnerStroke(){
        return winnerStroke;
    }

    @Override
    public String toString(){

        int[] currentBoard = this.i;
        return
                (currentBoard[0] + " | " + currentBoard[1] + " | " + currentBoard[2])+"\n"+
                        ("----------")+"\n"+
                        (currentBoard[3] + " | " + currentBoard[4] + " | " + currentBoard[5])+"\n"+
                        ("----------")+"\n"+
                        (currentBoard[6] + " | " + currentBoard[7] + " | " + currentBoard[8]);


    }
}
