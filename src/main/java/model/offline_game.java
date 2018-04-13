package model;

public class offline_game {

    public Board board = new Board();

    public AI AI = new AI();


    public offline_game(){




/*

        if (scanner.nextInt()==2) {
            System.out.println(board.toString());
            System.out.println("Your Turn:");

            while ((num = scanner.nextInt()) >= 0) {

                if (board.getBoardAsArray()[num] == 0 && board.getBoardAsArray()[num] == 0) {
                    board.getBoardAsArray()[num] = 1;
                    printBoard(board.getBoardAsArray());

                    System.out.println("My Turn:");
                    depth = 0;
                    int[] nextmoves = scoreWay(2, 2, board.getBoardAsArray(), -1, true);

                    int j = -9999999;
                    int position = 0;
                    for (int i = 0; i < nextmoves.length; i++) {
                        if (nextmoves[i] >= j && board.getBoardAsArray()[i] == 0) {
                            j = nextmoves[i];
                            position = i;
                        }

                    }
                    if (board.getBoardAsArray()[position] == 0)
                        board.getBoardAsArray()[position] = 2;

                    printBoard(board.getBoardAsArray());

                    //System.out.println(" the best place would be in position: "+position +"  -overall possible turns: "+possibilities);

                } else {
                    System.out.println("This field is already used! Try again.");
                }
                int i = evaluateWinner(board.getBoardAsArray());
                if (i != 0) {
                    System.out.println("Player " + i + " is winner");
                    System.exit(0);


                } else
                    System.out.println("Your Turn:");
            }
        }
        else{
            System.out.println("My Turn:");
            depth = 0;
            int[] nextmoves = scoreWay(2, 2, board.getBoardAsArray(), -1, true);

            int j = -9999999;
            int position = 0;
            for (int i = 0; i < nextmoves.length; i++) {
                if (nextmoves[i] >= j && board.getBoardAsArray()[i] == 0) {
                    j = nextmoves[i];
                    position = i;
                }

            }
            if (board.getBoardAsArray()[position] == 0)
                board.getBoardAsArray()[position] = 2;

            printBoard(board.getBoardAsArray());
            System.out.println("Your Turn:");

            while ((num = scanner.nextInt()) >= 0) {


                if (board.getBoardAsArray()[num] == 0 && board.getBoardAsArray()[num] == 0) {
                    board.getBoardAsArray()[num] = 1;
                    printBoard(board.getBoardAsArray());

                    System.out.println("My Turn:");
                    depth = 0;
                    nextmoves = scoreWay(2, 2, board.getBoardAsArray(), -1, true);

                    j = -9999999;
                    position = 0;
                    for (int i = 0; i < nextmoves.length; i++) {
                        if (nextmoves[i] >= j && board.getBoardAsArray()[i] == 0) {
                            j = nextmoves[i];
                            position = i;
                        }

                    }
                    if (board.getBoardAsArray()[position] == 0)
                        board.getBoardAsArray()[position] = 2;

                    printBoard(board.getBoardAsArray());


                    //System.out.println(" the best place would be in position: "+position +"  -overall possible turns: "+possibilities);

                } else {
                    System.out.println("This field is already used! Try again.");
                }
                int i = evaluateWinner(board.getBoardAsArray());
                if (i != 0) {
                    System.out.println("Player " + i + " is winner");
                    System.exit(0);


                }

            }
        }


        {
            System.out.println("Number is negative! System Shutdown!");
            System.exit(1);
        }
*/

    }

    public int AiTurn(int player){
        int i = AI.getNextMove(board,player);
        board.populateBoard(i,player);
        return i;
    }

}
