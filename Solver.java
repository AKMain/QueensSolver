import java.util.ArrayList;

public class Solver {
    static int queenNum;
    static int sideLength;
    static int [][] colourMap;
    static ArrayList<Pair> [] PossiblesSpotsPerColour;
    boolean [] solvedQ;
    Solver PastState;
    int [][] SolutionMap;
    int guessIndex;

    public Solver(int length,int [][] colours){
        guessIndex=0;
        queenNum=1;
        sideLength = length;
        colourMap = colours;
        SolutionMap = new int[length][length];

        PossiblesSpotsPerColour = new ArrayList[sideLength];


        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                SolutionMap[i][j]=0;
            }
        }
        solvedQ = new boolean[sideLength];
        for (int i = 0; i < length; i++) {
            solvedQ[i]=false;
            PossiblesSpotsPerColour[i] = new ArrayList<>();
        }

        PastState = null;


        fillInSpots();
    }//first ctor

    public Solver(Solver pastObj,int guessIndex){
        PastState = pastObj;
        SolutionMap = new int[sideLength][sideLength];
        this.guessIndex = guessIndex;

        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                SolutionMap[i][j]=pastObj.SolutionMap[i][j];
            }
        }

        solvedQ = new boolean[sideLength];
        for (int i = 0; i < sideLength; i++) {
            solvedQ[i]=pastObj.solvedQ[i];
            PossiblesSpotsPerColour[i] = new ArrayList<>();
        }

        fillInSpots();


        doGuess(firstSmallestRow());

        //now we take the guess at the guess index and go from there.

    }//subsequent ctor

    public void setGuessIndex(int n){
        guessIndex=n;
    }

    public void fillInSpots (){
        for (int i = 0; i < sideLength; i++) {
            PossiblesSpotsPerColour[i].clear();
        }


        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                if (SolutionMap[i][j]==0 && !solvedQ[colourMap[i][j]]){
                    PossiblesSpotsPerColour[colourMap[i][j]].add(new Pair(i,j));
                }
            }
        }

    }

    public void autoFiller(){
        boolean succes;

        do{

            succes = false;
            for (int i = 0; i < sideLength; i++) {

                if (PossiblesSpotsPerColour[i].size()==1){

                    Pair temp = PossiblesSpotsPerColour[i].get(0);
                    greyingOutTiles(temp.getX(),temp.getY());

                    succes=true;
                    break;
                }
            }


        }while(succes && LegalMoveCheck());

    }
    public void greyingOutTiles(int x,int y){

        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j <= 1 ; j+=2) {
                if (x+i >= 0 && x+i < sideLength && y+j >= 0 && y+j < sideLength)
                SolutionMap[x+i][y+j]=-1;
            }
        }
        for (int i = 0; i < sideLength; i++) {
            SolutionMap[i][y]=-1;
            SolutionMap[x][i]=-1;
        }
        SolutionMap[x][y]=queenNum;
        solvedQ[colourMap[x][y]]=true;
        queenNum++;
        fillInSpots();
    }

    public int firstSmallestRow(){
        //always guess the guess move on the one with the fewist options or the one that comes up earliest.
        int index =0;
        int smallestRow=sideLength+1;

        for (int i = 0; i < sideLength; i++) {
            if (PossiblesSpotsPerColour[i].size()<smallestRow && !solvedQ[i]){
                smallestRow=PossiblesSpotsPerColour[i].size();
                index=i;
            }
        }
        return index;
    }

    public void doGuess(int smallestRow){

        if (guessIndex < PossiblesSpotsPerColour[smallestRow].size()){

            Pair temp = PossiblesSpotsPerColour[smallestRow].get(guessIndex);
            greyingOutTiles(temp.getX(),temp.getY());
            fillInSpots();

        }

    }

    public boolean fullySolvedQ(){
        boolean allSolv = true;
        for (int i = 0; i < sideLength; i++) {
            allSolv = allSolv && solvedQ[i];
        }
        return allSolv;
    }

    public int [][] getSolutionMap(){
        return SolutionMap;
    }

    public Solver getPastState(){
        return PastState;
    }

    public void incrementGuessIndex(){
        guessIndex++;
    }

    public boolean LegalMoveCheck(){

        for (int i = 0; i < sideLength; i++) {
            if (PossiblesSpotsPerColour[i].size()<=0 && !solvedQ[i]){
                return false;
            }
        }

        return true;
    }

    public int possibleNumberOfguesses(){
        return PossiblesSpotsPerColour[firstSmallestRow()].size();
    }

    public int getQueenNum(){
        return queenNum;
    }
    public int getGuessIndex(){
        return guessIndex;
    }
    public void DeBugDump(){
        System.out.println("\nColour");
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {

                System.out.print(colourMap[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("\nSolutions");
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {

                if (SolutionMap[i][j]==0){
                    System.out.print(0+" ");
                }else if (SolutionMap[i][j]==-1){
                    System.out.print("x ");
                }else{
                    System.out.print(1+" ");
                }
            }
            System.out.println();
        }
        System.out.println("GuessIndex: "+guessIndex+"\tsmalles row: "+PossiblesSpotsPerColour[firstSmallestRow()]+"\tCol index: "+firstSmallestRow());
        for (int i = 0; i < sideLength; i++) {
            System.out.println(PossiblesSpotsPerColour[i]);
        }
        System.out.println();

    }


}
