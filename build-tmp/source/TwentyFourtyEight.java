import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TwentyFourtyEight extends PApplet {



private static final int NUM_ROWS = 4;
private static final int NUM_COLS = 4;
private static final int NUM_ORIGINAL_BOXES = 2; //number of original numbered boxes

private int score = 0;

private MSButton[][] buttons;

private ArrayList <MSButton> numberedSquares= new ArrayList <MSButton>();

public boolean gameStarted = true;
public boolean playGame = false;
public boolean gameIsOver = false;

public GameButton start = new GameButton(500/2 -100 , 700/2 - 35, "start");
public GameButton restart = new GameButton(500/2 - 100, 700/2 - 35, "restart");
public void setup ()
{
    
    textAlign(CENTER,CENTER);

    // make the manager
    Interactive.make( this );
    
    buttons = new MSButton[NUM_ROWS][NUM_COLS];

    for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
            buttons[r][c] = new MSButton(r,c);

    imageMode(CENTER);
    setFirstNums();
}

public void draw ()
{
    background(150,150,150);

    if(gameStarted){startScreen();}
    //else if(instructions){instructionsFunc();}
    else if(playGame){gamePlay();}
    else if(gameIsOver){endGame();}
}
public void mousePressed()
{
    if(gameStarted)
    {
        if(start.inButton())
        {
            gamePlay();
        }
    }
    
    else if(gameIsOver)
    {
        if(restart.inButton())
        {
            restartNumbers();
            setFirstNums();
            playGame = true;
            score = 0;
        }
    }
    
}
public void keyPressed()
{
    for(int c = 0; c<NUM_COLS; c++)
            for(int r = 0; r<NUM_ROWS; r++)
                buttons[r][c].setCombinedBoolean(false);

    if(key==CODED)
    {
        if(keyCode == LEFT && !areTheyStuckLeft())
        {
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeToLeft();
            addAnotherNumber();
        }
        else if(keyCode == RIGHT && !areTheyStuckRight())
        {
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeToRight();
            addAnotherNumber();
        }
        else if(keyCode == UP && !areTheyStuckUp())
        {
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeUp();
            addAnotherNumber();
        }
        else if(keyCode == DOWN && !areTheyStuckDown())
        {
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeDown();
            addAnotherNumber();
        }
    }
}

//classes
public class MSButton
{
    private int r, c, myValue;
    private float x,y, width, height;
    private boolean alreadyCombinedOnce, isStuck, up, down, left, right;
    
    public MSButton ( int rr, int cc )
    {
        width = 300/NUM_COLS;
        height = 300/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width + 100;
        y = r*height;
        myValue = 0;
        alreadyCombinedOnce = false;

        Interactive.add( this ); // register it with the manager
    }

    public int getValue(){return myValue;}

    public void setValue(int v){myValue = v;}

    public void setCombinedBoolean(boolean ss){alreadyCombinedOnce = ss;}

    public void draw () 
    {    
    	if(playGame)
    	{
	        noStroke();

	        if(myValue == 2){fill(236,226,216);}
	        else if(myValue == 4){fill(236,224,200);}
	        else if(myValue == 8){fill(242,177,123);}
	        else if(myValue == 16){fill(246,148,99);}
	        else if(myValue == 32){fill(243,126,90);}
	        else if(myValue == 64){fill(239,97,60);}
	        else if(myValue == 128){fill(239,205,112);}
	        else if(myValue == 256){fill(237,205,97);}
	        else if(myValue == 512){fill(236,200,80);}
	        else if(myValue == 1024){fill(239,197,63);}
	        else if(myValue == 2048){fill(240,192,49);}
	        else{fill(204,192,178);}

	        rect(x, y, width, height);

	        //noStroke();
	        
	        fill(185,173,159);
	        quad(x, y, x+4, y, x+4, y+height-4, x, y+height);
	        quad(x, y, x+width, y, x+width-4, y+4, x, y+4);
	        
	        quad(x+4, y+height-4, x+width, y+height-4, x+width, y+height, x, y+height);
	        quad(x+width-4, y+4, x+width, y, x+width, y+height, x+width-4, y+height);

	        if(myValue > 0)
	        {
	            if(myValue <=4){fill(118,106,92);}
	            else{fill(255);}
	            textSize(35);
	            text("" + myValue,x+width/2,y+height/2-3);            
	        }
	        else 
	        {
	            
	        }
	    }
    }

    public void swipeToRight()
    {
	        if(myValue > 0 && isValid(r,c+1) && buttons[r][c+1].getValue() == myValue && alreadyCombinedOnce == false)
	        {
	            alreadyCombinedOnce = true;
	            buttons[r][c+1].setValue(myValue + myValue);
	            score += myValue*2;
	            setValue(0);
	            buttons[r][c+1].setCombinedBoolean(true);
	            buttons[r][c+1].swipeToRight();
	        }
	        else if(myValue > 0 && isValid(r,c+1) && buttons[r][c+1].getValue() == 0)
	        {
	            buttons[r][c+1].setValue(myValue);
	            setValue(0);
	            buttons[r][c+1].swipeToRight();
	        }
    }

    public void swipeToLeft()
    {

    	if(myValue >0 && isValid(r,c-1) && buttons[r][c-1].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r][c-1].setValue(myValue + myValue);
            score += myValue*2;
            setValue(0);
            buttons[r][c-1].setCombinedBoolean(true);
            buttons[r][c-1].swipeToLeft();
        }
        else if(myValue > 0 && isValid(r,c-1) && buttons[r][c-1].getValue() == 0)
        {
            buttons[r][c-1].setValue(myValue);
            setValue(0);
            buttons[r][c-1].swipeToLeft();
        }
    }

    public void swipeUp()
    {
        if(myValue > 0 && isValid(r-1,c) && buttons[r-1][c].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r-1][c].setValue(myValue + myValue);
            score += myValue*2;
            setValue(0);
            buttons[r-1][c].setCombinedBoolean(true);
            buttons[r-1][c].swipeUp();
        }
        else if(myValue > 0 && isValid(r-1,c) && buttons[r-1][c].getValue() == 0)
        {
            buttons[r-1][c].setValue(myValue);
            setValue(0);
            buttons[r-1][c].swipeUp();
        }
    }

    public void swipeDown()
    {
        if(myValue > 0 && isValid(r+1,c) && buttons[r+1][c].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r+1][c].setValue(myValue + myValue);
            score += myValue*2;
            setValue(0);
            buttons[r+1][c].setCombinedBoolean(true);
            buttons[r+1][c].swipeDown();
        }
        else if(myValue > 0 && isValid(r+1,c) && buttons[r+1][c].getValue() == 0)
        {
            buttons[r+1][c].setValue(myValue);
            setValue(0);
            buttons[r+1][c].swipeDown();
        }
    }

    public void setOriginalValue()
    {
        if(Math.random() > 0.5f)
            myValue = 2;
    
        else
            myValue = 4;
    }
    
    public boolean isValid(int r, int c)
    {
        if(r<NUM_ROWS && r>=0 && c<NUM_COLS && c>=0) return true;
            
        return false;
    }

    public boolean isStuckLeft()
    {
        if(myValue == 0)	
        	return true;
        if(!isValid(r,c-1))
        	return true;
        if(buttons[r][c-1].getValue() != myValue && buttons[r][c-1].getValue() > 1)
        	return true;
        
        return false;
    }

    public boolean isStuckRight()
    {
    	if(myValue == 0)	
        	return true;
        if(!isValid(r,c+1))
        	return true;
        if(buttons[r][c+1].getValue() != myValue && buttons[r][c+1].getValue() > 1)
        	return true;
        
        return false;
    }

    public boolean isStuckUp()
    {
        if(myValue == 0)	
        	return true;
        if(!isValid(r-1,c))
        	return true;
        if(buttons[r-1][c].getValue() != myValue && buttons[r-1][c].getValue() > 1)
        	return true;
        
        return false;
        
    }

    public boolean isStuckDown()
    {
    	if(myValue == 0)	
        	return true;
        if(!isValid(r+1,c))
        	return true;
        if(buttons[r+1][c].getValue() != myValue && buttons[r+1][c].getValue() > 1)
        	return true;
        
        return false;
    }

    //     if(up && down && left && right)
    //     	return true;

    //     return false;
    // }
}

public class GameButton
{
    private int myX, myY, myColor, widthh, heightt;
    private String myType;

    GameButton(int x, int y, String type)
    {
        myX = x;
        myY = y;
        myType = type;
        myColor = 255;
        widthh = 70;
        heightt = 70;
    }

    public void show()
    {
        fill(myColor);
        rect(myX, myY, widthh, heightt, 10);
        fill(255,0,0);
        textSize(30);

       if(myType == "start")
        {
        	widthh = 210;
        	textSize(50);
        	text("S", myX + widthh/2, myY + heightt/2-7);
        }

        else if(myType == "restart")
        {
            widthh = 210;
            textSize(50);
            fill(66,134,235);
            text("Restart", myX + widthh/2, myY + heightt/2-7);
        }

        if(inButton())
            highlighted();
        else
            nonHighlighted();
    }
  
    public void highlighted()
    {
        myColor = color(250,237,150);      
    }

    public void nonHighlighted()
    {
        myColor = 255;
    }

    public boolean inButton()
    {
        if(mouseX > myX && mouseX < myX+widthh && mouseY > myY && mouseY < myY+heightt)
            return true;
        return false;
    }
}

//different screen functions
public void startScreen()
{
	gameStarted = true;
    playGame = false;
    gameIsOver = false;
    //instructions = false;

    start.show();
}
public void gamePlay()
{
	gameStarted = false;
    //instructions = false;
    gameIsOver = false;
    playGame = true;


    fill(255);
    textSize(40);

    text("Score: " + score, width/2, 600);
    
    if(gameOver())
    {
        gameIsOver = true;
        gameStarted = false;
        playGame = false;
    }
}
public void endGame()
{
    gameIsOver = true;
    gameStarted = false;
    playGame = false;
    //instructions = false;

    restart.show();

    text("GAME OVER", width/2, 600);
}

//setting number functions
public void setFirstNums() 
{
    for(int i = 0; i < NUM_ORIGINAL_BOXES; i++)
    {
        addAnotherNumber();
    }
}
public void addAnotherNumber()
{
    int roww = (int)(Math.random()*NUM_ROWS);
    int coll = (int)(Math.random()*NUM_COLS);
    if(buttons[roww][coll].getValue() == 0)
    {
        buttons[roww][coll].setOriginalValue();
    }
    else
        addAnotherNumber();
}

//stuck functions
public boolean areTheyStuckLeft()
{
	for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
        {
            if(buttons[r][c].isStuckLeft()==false)
            	return false;
        }
    return true;
}
public boolean areTheyStuckRight()
{
	for(int c = NUM_COLS -1 ; c>=0; c--)
        for(int r = NUM_ROWS -1; r>=0; r--)
        {
            if(buttons[r][c].isStuckRight()==false)
            	return false;
        }
    return true;
}
public boolean areTheyStuckUp()
{
	for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
        {
            if(buttons[r][c].isStuckUp()==false)
            	return false;
        }
    return true;
}
public boolean areTheyStuckDown()
{
	for(int c = NUM_COLS -1 ; c>=0; c--)
        for(int r = NUM_ROWS -1; r>=0; r--)
        {
            if(buttons[r][c].isStuckDown()==false)
            	return false;
        }
    return true;
}
public boolean gameOver()
{
	if(areTheyStuckRight()&&areTheyStuckDown()&&areTheyStuckUp()&&areTheyStuckLeft())	return true;
	return false;
}
public void restartNumbers()
{
    for(int c = 0; c<NUM_COLS; c++)
        for(int r = 0; r<NUM_ROWS; r++)
        {
            buttons[r][c].setValue(0);
        }
}

























public class Button
{
    private int myX, myY, myColor, widthh, heightt;
    private String myType;

    Button(int x, int y, String type)
    {
        myX = x;
        myY = y;
        myType = type;
        myColor = 255;
        widthh = 40;
        heightt = 40;
    }

    public void show()
    {
        fill(myColor);
        rect(myX, myY, widthh, heightt, 10);
        fill(255,0,0);
        textSize(30);

        if(myType == "restart")
            text("Restart", myX+widthh/2, myY);

        /*
        if(myType == "boxSizePlus")
        {
            text("+", myX+widthh/2, myY + heightt/2-5);
        }
        else if(myType == "boxSizeMinus")
        {
            text("-", myX+widthh/2, myY + heightt/2-5);
        }
        else if(myType == "numBoxesPlus")
        {
            text("+", myX+widthh/2, myY + heightt/2-5);
        }
        else if(myType == "numBoxesMinus")
        {
            text("-", myX+widthh/2, myY + heightt/2-5);
        }
        */
    }
  
    public void highlighted()
    {
        myColor = color(250,237,150);      
    }

    public void nonHighlighted()
    {
        myColor = 255;
    }

    public boolean inButton()
    {
        if(mouseX > myX && mouseX < myX+widthh && mouseY > myY && mouseY < myY+heightt)
            return true;
        return false;
    }
}

/*
public class GameRestart
{
    int myColor;

    GameRestart()
    {
        myColor = color(224, 74, 69);
    }

    void show()
    {
        fill(myColor);
        rect(200, 400, 200, 70,10);
        fill(255);
        textSize(30);
        text("Restart", 245,445);
    }

    void nonHighlighted()
    {
        myColor = color(224, 74, 69);
    }

    void highlighted()
    {
        myColor = color(242, 40, 33);
    }
}
*/


/*
ArrayList<Confetti> confetti = new ArrayList<Confetti>();

void setup()
{
  size(1000,700);
  for(int i = 0; i<100; i++)
    confetti.add(new Confetti());
}
void draw()
{
  background(0);
  for(Confetti con : confetti)
  {
    con.move();
    con.show();
  }
}
*/

public class Confetti
{
    int myX, myY, myColor, mySpeed, originalX, originalY;
    float myAngle;

    Confetti()
    {
        myX = height + (int)(Math.random()*(width-height));
        myY = -(int)(Math.random()*height);
        originalX = myX;
        originalY = myY;
        mySpeed = (int)(Math.random()*5)+6;
        double randomm = Math.random();
        if(randomm < 0.33f)
            myColor = color(255,0,0);
        else if(randomm < 0.66f)
            myColor = color(0,102,0);
        else
            myColor = color(0,0,255);

        myAngle = (float)(Math.random()*2*PI/3+PI/6);
    }

    public void show()
    {
        stroke(0);
        fill(myColor);
        rect(myX, myY, 10, 20,10);
    }

    public void move()
    {
        myX += (int)(cos(myAngle)*mySpeed);
        myY += (int)(sin(myAngle)*mySpeed);
        if(myY > height || myX < height || myX > width)
        {
          myY = originalY;
          myX = originalX;
          mySpeed = (int)(Math.random()*5)+6;
        }
    }
}



public void gameRunning()
{
    //in future: make button for different size playing field
}











  public void settings() {  size(500, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TwentyFourtyEight" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
