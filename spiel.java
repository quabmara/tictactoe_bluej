//2023 informatik projekt
//Madelaine und Mara
import java.util.*;
class spiel extends Ereignisbehandlung
{
    //global variablen
    Dreieck dreieck;
    Kreis kreis;
    Rechteck background;
    Rechteck cursor;
    int [][] boardData; // 0-kreis, 1-dreieck, 5-empty
    boolean gameOver = false;
    Dreieck [] objects1;
    //text objects
    Text gameOverText = new Text();
    Text gameOverText2 = new Text();
    Text stats1 = new Text();
    Text stats2 = new Text();
    Text stats3 = new Text();
    //count variablen
    int gameCount = 0;
    int winCount = 0;
    int lostCount = 0;
    int player = 0; //0-kreis-player,1-dreieck-bot
    
    public spiel()
    {        
        //start
        gameText();
        reset();
    }
    
    void gameText() {
        Text title = new Text();
        title.TextSetzen("Tic Tac Toe");
        title.TextGrößeSetzen(40);
        title.PositionSetzen(450, 140);
        
        Text description1 = new Text();
        description1.TextSetzen("- Pfeiltasten um Cursor zu bewegen");
        description1.TextGrößeSetzen(20);
        description1.PositionSetzen(450, 180);
        
        Text description2 = new Text();
        description2.TextSetzen("- Leertaste um Symbol zu setzen");y
        description2.TextGrößeSetzen(20);
        description2.PositionSetzen(450, 210);
        
        Text description3 = new Text();
        description3.TextSetzen("- Leertaste auch für Reset");
        description3.TextGrößeSetzen(20);
        description3.PositionSetzen(450, 240);
        
        Text credits = new Text();
        credits.TextSetzen("Informatik 2023");
        credits.TextGrößeSetzen(15);
        credits.PositionSetzen(450, 400);
        
        //stats
        stats1.TextSetzen("Spiele: 0");
        stats1.TextGrößeSetzen(15);
        stats1.PositionSetzen(100, 90);
        
        stats2.TextSetzen("gewonnen: 0");
        stats2.TextGrößeSetzen(15);
        stats2.PositionSetzen(200, 90);
        
        stats3.TextSetzen("verloren: 0");
        stats3.TextGrößeSetzen(15);
        stats3.PositionSetzen(300, 90);
    }
    
    void reset() {
        //background
        drawBackground();
        gameOverText.SichtbarkeitSetzen(false);
        gameOverText2.SichtbarkeitSetzen(false);
        //cursor initialisieren
        cursor = new Rechteck();
        cursor.FarbeSetzen("weiss");
        cursor.GrößeSetzen(100, 100);
        cursor.PositionSetzen(100, 100);
        //boardData zuruecksetzen
        boardData = new int[][] { {5,5,5}, {5,5,5}, {5,5,5} };
        printBoard(); //(debug)
        gameOver = false;
        
        //new game
        gameCount += 1;
        if ((gameCount % 2) == 0) {
            randomBotMove();
        }
        stats1.TextSetzen("Spiele: " + gameCount);
    }
    
    void printBoard() {
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 2; x++) {
                System.out.print(boardData[x][y]);
            }
            System.out.println("");
        }
    }
    
    void drawBoard() {
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 2; x++) {
                if (boardData[x][y] == 0) {
                    //mal nen kreis
                    kreis = new Kreis();
                    kreis.PositionSetzen((x + 1) * 100 + 50, (y+1) * 100 + 50);
                } else if (boardData[x][y] == 1) {
                    //mal nen dreieck
                    dreieck = new Dreieck();
                    dreieck.PositionSetzen((x + 1) * 100 + 50, (y+1) * 100 + 5);
                }
            }
        }
    }
    
    void drawBackground() {
        //background (schachfeldmaessig)
        int farbe = 0;
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 2; x++) {
                Rechteck feld = new Rechteck();
                if (farbe == 0) {
                    feld.FarbeSetzen("schwarz");
                    farbe = 1;
                } else {
                    feld.FarbeSetzen("grau");
                    farbe = 0;
                }
                feld.GrößeSetzen(100, 100);
                feld.PositionSetzen((x+1) * 100, (y+1) * 100);
            }
        }
    }
    
    @Override void TasteGedrückt (char taste) {
        if (taste == " ".charAt(0)) { //leertaste
            if (gameOver) {
                //spiel beginnen
                reset();
                gameOver = false;
            } else {
                //setze symbol
                int x = cursor.x / 100 - 1;
                int y = cursor.y / 100 - 1;
                if (boardData[x][y] == 5) {
                    boardData[x][y] = 0;
                    
                    drawBoard();
                    printBoard();
                    player = 0;
                    if (!checkForGameOver()) {
                        //zug fertig und kein Game Over -> bot setzt move
                        randomBotMove();
                        player = 1;
                        checkForGameOver();
                    }
                }
            }
        }
    }
    
    @Override void SonderTasteGedrückt(int code) { //pfeiltasten
        if(code == 37) {
            if (cursor.x > 110) {
                cursor.Verschieben(-100, 0);
            }
        }
        
        if(code == 38) {
            if (cursor.y > 110) {
                cursor.Verschieben(0, -100);
            }
        }
        
        if(code == 39) {
            if (cursor.x < 300) {
                cursor.Verschieben(100, 0);
            }
        }
        
        if(code == 40) {
            if (cursor.y < 300) {
                cursor.Verschieben(0, 100);
            }
        }
    }  
    
    boolean checkForGameOver(){
        //check columns / spalten
        for (int x = 0; x <= 2; x++) {
            if (boardData[x][0] != 5 && boardData[x][0] == boardData[x][1] && boardData[x][0] == boardData[x][2]) {
                gameOver = true;
            }
        }
        //check rows / reihen
        for (int y = 0; y <= 2; y++) {
            if (boardData[0][y] != 5 && boardData[0][y] == boardData[1][y] && boardData[0][y] == boardData[2][y]) {
                gameOver = true;
            }
        }
        //check diagonals
        if (boardData[1][1] != 5 && boardData[1][1] == boardData[0][0] && boardData[1][1] == boardData[2][2]) {
            gameOver = true;
        }
        if (boardData[1][1] != 5 && boardData[1][1] == boardData[2][0] && boardData[1][1] == boardData[0][2]) {
            gameOver = true;
        }
        
        //if no empty cells left -> tie
        boolean emptyCellsLeft = false;
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 2; x++) {
                if (boardData[x][y] == 5) {
                    emptyCellsLeft = true;
                    break;
                }
            }
            if (emptyCellsLeft) {
                break;
            }
        }
        
        if (gameOver == true) { 
            System.out.println("GAME OVER");
            gameOverText();
        } else if (!emptyCellsLeft ) {
            System.out.println("UNENTSCHIEDEN");
            tieText();
            gameOver = true; //weil wichtig fuer loop
        }
        return gameOver;
    }
    
    void gameOverText() {
        String text = "Bot hat gewonnen!";
        String farbe = "rot";
        if (player == 0) {
            text = "Du hast gewonnen!";
            farbe = "hellgruen";
            
            winCount += 1;
            stats2.TextSetzen("gewonnen: " + winCount);
        } else {
            lostCount += 1;
            stats3.TextSetzen("verloren: " + lostCount);
        }
        gameOverText.SichtbarkeitSetzen(true);
        gameOverText.TextSetzen("Game Over!");
        gameOverText.TextGrößeSetzen(50);
        gameOverText.PositionSetzen(450, 320);
        gameOverText.FarbeSetzen(farbe);
        
        gameOverText2.SichtbarkeitSetzen(true);
        gameOverText2.TextSetzen(text);
        gameOverText2.TextGrößeSetzen(25);
        gameOverText2.PositionSetzen(450, 360);
        gameOverText2.FarbeSetzen(farbe);
    }
    
    void tieText() {
        gameOverText.SichtbarkeitSetzen(true);
        gameOverText.TextSetzen("Unentschieden!");
        gameOverText.TextGrößeSetzen(40);
        gameOverText.PositionSetzen(450, 320);
        gameOverText.FarbeSetzen("blau");
    }
    
    void randomBotMove() {
        List<List<Integer>> array = new ArrayList<List<Integer>>();
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 2; x++) {
                if (boardData[x][y] == 5) {
                    List<Integer> currentCoordinates = new ArrayList<Integer>();
                    currentCoordinates.add(x);
                    currentCoordinates.add(y);
                    array.add(currentCoordinates);
                }
            }
        }
        
        System.out.println(array);
        Random rand = new Random();
        int randomInt = rand.nextInt(array.size()); //0 - (array.size() - 1) aber schon automatisch
        
        boardData[array.get(randomInt).get(0)][array.get(randomInt).get(1)] = 1;
        
        drawBoard();
        printBoard();
    }
}
