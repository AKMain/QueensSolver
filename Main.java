/**
 * Test Case
 * 0 0 3 0 1
 * 0 1 1 1 1
 * 1 2 1 4 1
 * 0 4 0 4 1
 * 0 2 0 3 2
 * 4 0 4 2 3
 * 2 1 3 1 3
 * 2 2 2 2 4
 * 3 2 3 2 5
 * 2 3 4 4 5
 * -10
 */

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class Main {

    public static void loading() {
        try {
            Robot robot = new Robot();

            int x = 655;
            int y = 280;

            Rectangle pixelRect = new Rectangle(x, y, 1, 1);

            while (true) {
                BufferedImage image = robot.createScreenCapture(pixelRect);
                if (-16777216 == image.getRGB(0, 0) ) {
                    return;
                }
            }
        } catch (AWTException e) {
            System.err.println("AWTException occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int boardSize(){
        int blackBarCounter = -1;


        try {


            Robot robot = new Robot();

            Rectangle screenRect = new Rectangle(650, 290, 410, 1);

            BufferedImage image = robot.createScreenCapture(screenRect);

            for (int i = 0; i < 410; i++) {
                int pixelRGB = image.getRGB(i, 0);

                if (pixelRGB == -16777216 || pixelRGB == -13686471){
                    blackBarCounter++;
                    i+=4;
                }

            }

        }catch (Exception e){
            System.out.println("boardSize error");
        }

        return blackBarCounter;
    }

    public static void boardColours (int [][] board,int boardLen){
        HashMap<Integer, Integer> RGBToInt = new HashMap<>();
        int curMaxNum = 0;
        int topX = 665, topY = 275;
        int sizePerBox = 400 / boardLen;

        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(topX, topY, sizePerBox * boardLen, sizePerBox * boardLen);
            BufferedImage image = robot.createScreenCapture(screenRect);

            for (int i = 0; i < boardLen; i++) {
                for (int j = 0; j < boardLen; j++) {
                    int pixelRGB = image.getRGB(sizePerBox * j, sizePerBox * i);

                    if (!RGBToInt.containsKey(pixelRGB)){
                        RGBToInt.put(pixelRGB, curMaxNum);
                        curMaxNum++;
                    }
                    board[i][j] = RGBToInt.get(pixelRGB);
                }
            }
        } catch (Exception e){
            System.out.println("boardColours error");
        }
    }

    public static void clickBoard(int [][] solutionMap,int boardLen){


        int topX=665,topY=275;
        int sizePerBox = 400/boardLen;


        try{
            Robot robot = new Robot();

            for (int i = 0; i < boardLen; i++) {
                for (int j = 0; j < boardLen; j++) {
                    if (solutionMap[i][j] > 0) {

                        robot.mouseMove(topX+j*sizePerBox, topY+i*sizePerBox);
                        for (int k = 0; k < 2; k++) {
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(5);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(2);
                        }

                    }
                }
            }
        }catch (Exception e) {
            System.err.println("clickBoard error");
        }


    }


    public static void main(String[] args) {

        //add idle stage which starts the game before waiting for input
        loading();

        long start = System.nanoTime();

        int boardLen = boardSize();

        int [][] board = new int[boardLen][boardLen];

        boardColours(board,boardLen);

        Solver OGState = new Solver(boardLen,board);
        OGState.autoFiller();

        Solver recur = OGState;

        int guess =0;

        while(!recur.fullySolvedQ()){
            recur = new Solver(recur,guess);
            recur.autoFiller();

            if (!recur.fullySolvedQ() && !recur.LegalMoveCheck()){

                do{
                    guess = recur.getGuessIndex()+1;

                    recur = recur.getPastState();
                    recur.fillInSpots();

                }while(guess>=recur.possibleNumberOfguesses());

            }else{
                guess=0;
            }

        }
        //4 milliseconds to solve the game

        clickBoard(recur.getSolutionMap(),boardLen);

        System.out.println((System.nanoTime()-start)/1000000);

    }

}