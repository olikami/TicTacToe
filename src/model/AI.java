package model;

import java.util.Arrays;
import java.util.Random;

public class AI {


    public int depthLimiter = 100;

    private int depth;

    public AI() {

    }



    public static void main(String[] args) {

        AI ai = new AI();
        Board board = (new Board(new int[]{0, 0, 0, 1,2, 0, 0, 2, 1}));


        System.out.println("My Turn:");

        int[] nextmoves = ai.scoreWay(1, 1, board.getBoardAsArray(), -1, true);

        int j = -9999999;
        int position = 0;
        for (int i = 0; i < nextmoves.length; i++) {
            if (nextmoves[i] >= j && board.getBoardAsArray()[i] == 0) {
                j = nextmoves[i];
                position = i;
            }

        }
        if (board.getBoardAsArray()[position] == 0)
            board.getBoardAsArray()[position] = 1;

        System.out.println(board.toString());

    }

    public int getNextMove(Board board, int player) throws NoSuchFieldError {


        int[] nextmoves = scoreWay(player, player, board.getBoardAsArray(), -1, true);


        int possibilities[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int counter = 0;
        int j = -999;
        int position = 0;
        for (int i = 0; i < nextmoves.length; i++) {
            if (nextmoves[i] >= j && board.getBoardAsArray()[i] == 0) {
                if(j == nextmoves[i]) {
                    possibilities[counter] = i;
                    counter++;
                }else{
                    Arrays.fill(possibilities,-1);
                }
                j = nextmoves[i];
                position = i;

            }

        }
        counter = 0;
        for(int i : possibilities){
            if (i ==-1)
                break;
            counter++;
        }
        if(counter>0){
            Random r = new Random();
            position = possibilities[r.nextInt(counter)];

        }
        if (board.getBoardAsArray()[position] == 0)
            return position;
        else throw new NoSuchFieldError("No empty field @ " + position);


    }

    public int[] scoreWay(int Player/* 0=false 1= p1 2=p2*/, int PlayerTurn, int[] currentBoardStatic, int initialmove, boolean firstLevel) throws NumberFormatException {

/*
        //The depth in the following iteration is:
        if (depth == 1)
            System.out.print(depth);
        else if (depth == 0)
            System.out.print(depth);
        else if (depth == -1)
            System.out.print(depth);
        else
            System.out.print(depth);
        System.out.print("⇩ ");

*/


        int[] currentBoard = new int[currentBoardStatic.length];
        for (int i = 0; i < currentBoardStatic.length; i++) {
            currentBoard[i] = currentBoardStatic[i];
        }

        if (Player == 0 || Player > 2)
            throw new NumberFormatException("oh oh, the player is not specified correctly. possible players are: 1, 2");


        for (int i = 0; i < 9; i++) {
            if (currentBoard[i] == 0) {
                currentBoard[i] = PlayerTurn;
                //printBoard(currentBoard);
                currentBoard[i] = 0;

            }
        }

        //loop through the possible next moves
        //save the depth recursive in a list. if there is a break in this loop it gets deleted, otherwise it joins the original list.
        int[] recursiveList = {-999, -999, -999, -999, -999, -999, -999, -999, -999};

        for (int i = 0; i < 9; i++) {

            //see if the current position is free to make a move
            if (currentBoard[i] == 0) {

                if (i == 4 && depth == 0)
                    System.out.println(" lool");
                if (i == 8 && depth == 0)
                    System.out.println(" lool");

                if (initialmove == -1) {
                    initialmove = i;

                    currentBoard[i] = PlayerTurn;
                    //printBoard(currentBoard);
                    currentBoard[i] = 0;


                }
                //if int is like player int, the given player wins, if 3 its a draw if false, the other player wins, if null, it couldnt evaluate, so we do a recursive method call
                /*boolean evaluateNextMove()*/
                currentBoard[i] = PlayerTurn;


                int winner = evaluateWinner(currentBoard)[0];

                if (winner == 0) {

                    if (depthLimiter <= depth) {
                        recursiveList[firstLevel ? initialmove : i] = 0;

                    } else {
                        depth++;
                        int[] deeperList = scoreWay(Player, PlayerTurn == 1 ? 2 : 1, currentBoard, i, false);
                        depth--;

                        if (depth == 2)
                            System.out.print(depth);

                        Boolean win = false;
                        Boolean lose = false;
                        Boolean draw = false;

                        //update the real recursive List

                        //MAX: take the biggest positive turn

                        int iwin = 0;
                        int ilose = 0;

                        int possibilitiesCounter = 0;
                        for (int iii : deeperList) {
                            if (iii == -999) continue;
                            possibilitiesCounter++;
                            if (iii > 0) {
                                win = true;
                                iwin++;

                            }
                            if (iii < 0) {
                                lose = true;
                                ilose++;
                            }
                            if (iii == 0)
                                draw = true;
                        }

                        if (PlayerTurn == Player) {

                            if (iwin == possibilitiesCounter) {
                                Arrays.fill(recursiveList, -999);
                                recursiveList[firstLevel ? initialmove : i] = 1;
                                break;

                            } else if (win)
                                recursiveList[firstLevel ? initialmove : i] = 1;
                            else if (draw)
                                recursiveList[firstLevel ? initialmove : i] = 0;
                            else if (lose) {
                                recursiveList[firstLevel ? initialmove : i] = -999;
                                if (i == 4 && depth == 0)
                                    System.out.println(" lool");
                            }

                            if (i == 2 && depth == 2 && currentBoard[4]==1 && currentBoard[1]==2)
                                System.out.println(" lool");

                            if (i == 8 && depth == 2 && currentBoard[4]==1 && currentBoard[0]==2)
                                System.out.println(" lool");

                            if (i == 8 && depth == 4 && currentBoard[4]==1 && currentBoard[1]==2 && currentBoard[2]==1&&currentBoard[3]==2)
                                System.out.println(" lool");

                        } else {
                            if (ilose == possibilitiesCounter) {
                                Arrays.fill(recursiveList, -999);
                                recursiveList[firstLevel ? initialmove : i] = -1;
                                break;

                            } else if (lose)
                                recursiveList[firstLevel ? initialmove : i] = -1;


                            else if (draw)
                                recursiveList[firstLevel ? initialmove : i] = 0;
                            else if (win)
                                recursiveList[firstLevel ? initialmove : i] = -999;

                            if (i == 1 && depth == 1 && currentBoard[4]==1 )
                                System.out.println(" lool" + recursiveList);

                            //this shouldnt be a win because 1 can win horizontally
                            if (i == 3 && depth == 3 && currentBoard[4]==1 && currentBoard[1]==2 && currentBoard[2]==1)
                                System.out.println(" lool");

                            //falsch should be a immediate loss
                            if (i == 8 && depth == 3 && currentBoard[4]==1 && currentBoard[1]==2 && currentBoard[2]==1)
                                System.out.println(" lool");


                        }


                    }

                } else if (winner == Player) {

                    for (int i4 = 0; i4 < 9; i4++) {
                        //if (recursiveList[i4] != 0)
                            recursiveList[i4] = -999;

                    }

                    //Arrays.fill(recursiveList,-999);
                    recursiveList[firstLevel ? initialmove : i] = 1; //1d/depth<=1?1:depth-1;
                    currentBoard[i] = 0;
                    if (firstLevel) {
                        System.out.println("^^lose/win: " + recursiveList[i]);
                        initialmove = -1;

                    }
                    break;

                } else if (winner == (Player == 1 ? 2 : 1)) {

                    for (int i4 = 0; i4 < 9; i4++) {
                        //if (recursiveList[i4] != 0)
                            recursiveList[i4] = -999;

                    }

                    //Arrays.fill(recursiveList,-999);
                    recursiveList[firstLevel ? initialmove : i] = -1; //1d/depth<=1?1:depth-1;
                    currentBoard[i] = 0;
                    if (firstLevel) {
                        System.out.println("^^lose/win: " + recursiveList[i]);
                        initialmove = -1;

                    }

                    break;
                } else if (winner == 3) {
                    recursiveList[firstLevel ? initialmove : i] = 0;


                }
                if (firstLevel) {
                    System.out.println("lose/win without break: " + recursiveList[initialmove]);
                    initialmove = -1;

                }

                currentBoard[i] = 0;


            } else {
            }
        }


        if (depth == 2)
            System.out.print(depth);
        return recursiveList;
    }


    public int[] evaluateWinner(int[] currentBoard) {


        int[][] board = {
                {currentBoard[0], currentBoard[1], currentBoard[2]},
                {currentBoard[3], currentBoard[4], currentBoard[5]},
                {currentBoard[6], currentBoard[7], currentBoard[8]}};

        int n = 3;

        for (int j = 0; j < n; j++) {
            //check end conditions

            //check row
            for (int i = 0; i < n; i++) {
                if (board[j][i] != board[j][0] || board[j][i] == 0)
                    break;
                if (i == n - 1) {
                    //report win for board[j][i]
                    return new int[]{board[j][i], 1, j};
                }
            }

            //check col
            for (int i = 0; i < n; i++) {
                if (board[i][j] != board[0][j] || board[i][j] == 0)
                    break;
                if (i == n - 1) {
                    //report win for board[0][j]
                    return new int[]{board[0][j], 2, j};

                }
            }

            //check diago
            if (j == 0) {
                //we're on a diagonal
                for (int i = 0; i < n; i++) {
                    if (board[i][i] != board[0][0] || board[i][i] == 0)
                        break;
                    if (i == n - 1) {
                        //report win for board[0][0]
                        return new int[]{board[0][0], 3, 1};

                    }
                }
            }
            //check anti diago
            if (j == 2) {
                for (int i = 0; i < n; i++) {
                    if (board[i][(n - 1) - i] != board[2][0] || board[i][(n - 1) - i] == 0)
                        break;
                    if (i == n - 1) {
                        //report win for board[2][2]
                        return new int[]{board[2][0], 4, 1};

                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            if (currentBoard[i] == 0) {
                return new int[]{0, 0, 0};
            }
        }

        return new int[]{3, 5, 0};
    }


}
