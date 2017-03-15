import de.bezier.guido.*;

private static final int NUM_ROWS = 4;
private static final int NUM_COLS = 4;
private static final int NUM_ORIGINAL_BOXES = 2;

private MSButton[][] buttons;

private ArrayList <MSButton> numberedSquares= new ArrayList <MSButton>();

GameButton leftSwipe = new GameButton(400,10,"left");
GameButton rightSwipe = new GameButton(400, 110, "right");
GameButton upSwipe = new GameButton(400,210, "up");
GameButton downSwipe = new GameButton(400,310, "down");

void setup ()
{
    size(600, 700);
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

/*
public void mouseClicked()
{       
        for(int c = 0; c<NUM_COLS; c++)
            for(int r = 0; r<NUM_ROWS; r++)
                buttons[r][c].setCombinedBoolean(false);
        
        if(leftSwipe.inButton())
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeToLeft();

        else if(rightSwipe.inButton())
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeToRight();

        else if(upSwipe.inButton())
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeUp();

        else if(downSwipe.inButton())
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeDown();

        addAnotherNumber();
}
*/

public void keyPressed()
{
    for(int c = 0; c<NUM_COLS; c++)
            for(int r = 0; r<NUM_ROWS; r++)
                buttons[r][c].setCombinedBoolean(false);

    if(key==CODED)
    {
        if(keyCode == LEFT)
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeToLeft();
        else if(keyCode == RIGHT)
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeToRight();
        else if(keyCode == UP)
            for(int c = 0; c<NUM_COLS; c++)
                for(int r = 0; r<NUM_ROWS; r++)
                    buttons[r][c].swipeUp();
        else if(keyCode == DOWN)
            for(int c = NUM_COLS -1; c>=0; c--)
                for(int r = NUM_COLS -1; r>=0; r--)
                    buttons[r][c].swipeDown();
    }
    addAnotherNumber();

}

public void draw ()
{
    background(0);

    leftSwipe.show();
    rightSwipe.show();
    upSwipe.show();
    downSwipe.show();
}

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
 
public class MSButton
{
    private int r, c, myValue;
    private float x,y, width, height;
    private boolean alreadyCombinedOnce;
    
    public MSButton ( int rr, int cc )
    {
        width = 300/NUM_COLS;
        height = 300/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
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

    public void swipeToRight()
    {
        if(myValue > 0 && isValid(r,c+1) && buttons[r][c+1].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r][c+1].setValue(myValue + myValue);
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
        if(myValue > 0 && isValid(r,c-1) && buttons[r][c-1].getValue() == myValue && alreadyCombinedOnce == false)
        {
            alreadyCombinedOnce = true;
            buttons[r][c-1].setValue(myValue + myValue);
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
        if(Math.random() > 0.5)
            myValue = 2;
    
        else
            myValue = 4;
    }
    
    public boolean isValid(int r, int c)
    {
        if(r<NUM_ROWS && r>=0 && c<NUM_COLS && c>=0) return true;
            
        return false;
    }

    public boolean isStuck()
    {
        return true;
        //if(c==3 && buttons[r][c-1].getValue()!=myValue
    }
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

        if (myType == "left")
        {
          strokeWeight(3);
          stroke(0);
          line(myX + 14, myY + 35, myX + 56, myY + 35);
          line(myX + 14, myY + 35, myX + 27, myY + 24);
          line(myX + 14, myY + 35, myX + 27, myY + 46);
        }
        else if(myType == "right")
        {
          strokeWeight(3);
          stroke(0);
          line(myX + 14, myY + 35, myX + 56, myY + 35);
          line(myX + 56, myY + 35, myX + 43, myY + 46);
          line(myX + 56, myY + 35, myX + 43, myY + 24);
        }
        else if(myType == "up")
        {
          strokeWeight(3);
          stroke(0);
          line(myX + 35, myY + 14, myX + 35, myY + 56);
          line(myX + 35, myY + 14, myX + 24, myY + 27);
          line(myX + 35, myY + 14, myX + 46, myY + 27);
        }
        else if(myType == "down")
        {
          strokeWeight(3);
          stroke(0);
          line(myX + 35, myY + 14, myX + 35, myY + 56);
          line(myX + 35, myY + 56, myX + 24, myY + 43);
          line(myX + 35, myY + 56, myX + 46, myY + 43);
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




























