import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
import java.net.*;
import java.awt.Color;
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    // Declare variables, booleans and classes.
    private final int BRICKWIDTH = 45;
    private final int BRICKHEIGHT = 20;    
    private final int VOFFSET = 12;
    private final int HOFFSET = 12;
    private Paddle paddle; 
    private Ball ball;
    private Fader fader;
    private CoverPage menu;
    private GameOver gameOver;
    private ScoreBoard scoreBoard=new ScoreBoard();
    private Counter levelNum = new Counter();;
    private Pointy aim = new Pointy();  
    // a total of 4 lives per game
    private int lives = 4;
    // start score from 0
    private int score = 0;
    // start game with level 1
    private int level = 1;
    // create 3 new life "bars"
    private Lives live1 = new Lives();
    private Lives live2 = new Lives();
    private Lives live3 = new Lives();
    // initalize background music, save was "backgroundMusic"
    GreenfootSound backgroundMusic = new GreenfootSound("theme.wav");
    // boolean to determine if ball was launched
    private boolean start = false;
    // boolean to determine is gameOver music was played
    private boolean played = false;
    // instead of boolean uses integers to meet if statement for main menu. Fixes launch bug where ball launches immediatly after menu.
    private int clickMenu = 1;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(700, 520, 1); 
        // Sets the order of display of Actors
        setPaintOrder(CoverPage.class,GameOver.class, Fader.class,Ball.class,Pointy.class,Paddle.class, Smoke.class, Lives.class, ScoreBoard.class, Counter.class);
        // create new paddle and ball
        paddle = new Paddle();
        ball = new Ball();
        // add paddle into world
        addObject(paddle, getWidth()/2, getHeight()-26);
        addObject(aim,paddle.getX(),paddle.getY()-20);
        // Add the score board into the world
        addObject(scoreBoard,658,511);     
        // Add the level counter to world
        addObject(levelNum,575,511);  
        // Create a new fader for this class and add it to the world
        fader = new Fader();
        addObject (fader, 400, 300);
        // import menu 
        menu = new CoverPage();
        addObject (menu, 350,260);

        /** Offset = space between each
        // vOFFSET = distance between each consequtive line
        // placed here so gets initalized right after compile, fixes display bug 

         *  "I"
         */
        for ( int i = 2; i <= 4 ; i++)
        {   

            addObject( new Brick(1), (BRICKWIDTH * (i - 1)) + (BRICKWIDTH / 2) + (HOFFSET *  i), (BRICKHEIGHT * 2)+ (VOFFSET * 2) + (BRICKHEIGHT / 2));  
            addObject( new Brick(1), (BRICKWIDTH * (2)) + (BRICKWIDTH / 2) + (HOFFSET *  3), (BRICKHEIGHT * (i+1))+ VOFFSET *(i+1) + (BRICKHEIGHT / 2));  
            addObject( new Brick(1), (BRICKWIDTH * (i - 1)) + (BRICKWIDTH / 2) + (HOFFSET *  i), (BRICKHEIGHT * 5)+ (VOFFSET * 8) + (BRICKHEIGHT / 2)); 

        }
        for ( int i = 1; i <= 7 ; i++)
        {
            addObject( new Brick(6), (BRICKWIDTH * (i - 1)) + (BRICKWIDTH / 2) + (HOFFSET *  (i*4)), (BRICKHEIGHT *2 )+ (VOFFSET * 21) + (BRICKHEIGHT / 2));
        }

        /**
         * "<3" Love
         */

        addObject( new Brick(3), 392, 71);     
        addObject( new Brick(3), 355,98); 
        addObject( new Brick(3), 435,94); 
        addObject( new Brick(3), 335,125); 
        addObject( new Brick(3), 370,155); 
        addObject( new Brick(3), 406,181); 
        addObject( new Brick(3), 455,210);
        addObject( new Brick(3), 485,94); 
        addObject( new Brick(3), 525,72); 
        addObject( new Brick(3), 555,98); 
        addObject( new Brick(3), 573,125); 
        addObject( new Brick(3), 545, 153); 
        addObject( new Brick(3), 510, 183); 
        addObject( new Brick(3), 460, 120); 

        //Add life "bar" into world       
        addObject( live1, 23, 510);
        addObject( live2, 69, 510);
        addObject( live3, 115, 510);        
        // clears screen instantly to show level 1
        fader.fadeBackIn();     
        // play background music continuously
        backgroundMusic.playLoop();
    }

    // each act check for death, mouse input and whether to create new level   
    public void act()
    {
        checkLevel();
        checkMouse();
        checkLives();       
    }

    // checks if player looses life
    public  void checkLives()
    {
        // Whenever player lose life, remove corresponding life bar
        if (lives == 3) 
        {
            removeObject(live3);
        }
        if (lives == 2)
        { 
            removeObject(live2);
        }
        if (lives == 1)
        { 
            removeObject(live1);            
            // End game. Remove Actors from world.
            backgroundMusic.stop();
            // play game over sound
            gameOverSound();
            // Display GameOver screen
            gameOver = new GameOver();
            addObject (gameOver, 350,260);
            removeObjects(getObjects(Smoke.class)); 
            removeObjects(getObjects(Ball.class)); 
            removeObjects(getObjects(Pointy.class));
            // end game when gameover sound is finished playing            
            if (played)
            {
                Greenfoot.stop();
            }
        }   
    } 

    // return boolean after gameover sound played
    public void gameOverSound()
    {
        Greenfoot.playSound("gameOver.wav");
        played=true;
    }

    // subtract 1 from total life and add a new ball to the world
    public void takeLife()
    {
        replaceBall();
        lives--;
    }

    // reward points according to destroyed brick
    public void addPoints(int points)
    {
        score+=points;      
        // refreshes counter display for score
        scoreBoard.update(score);
    }

    // checks for player input from mouse
    public void checkMouse()
    {
        // send cursor value to mouse variable
        MouseInfo mouse = Greenfoot.getMouseInfo();
        int changeX;
        int mouseX;
        int mouseY;
        // check don't exceed left and right border of background
        // don't move paddle before player shoots
        if (Greenfoot.mouseClicked(null)) 
        {
            // once clicked, remove menu
            removeObject(menu); 
            // fixes bug. Instead of boolean, increase int by 1 to meet the if statement of ball launch.           
            clickMenu++;                                   
        }

        // if ball has launched, move paddle according to user input
        if (start)
        {
            if (Greenfoot.mouseMoved(null) && mouse.getX() > (paddle.getImage().getWidth())/3 && mouse.getX() <  (getWidth()+5) - paddle.getImage().getWidth()/2) 
            {     
                // calculate difference for actual magnitude moved
                changeX = mouse.getX()-paddle.getX(); 
                // move paddle accordingly
                movePaddle(changeX);
            }
        }

        // boolean does NOT work. Since the click from the menu will meet this statement. As a result, ball launches immediatly after menu screen.
        if (clickMenu>2)
        {
            if(paddle.haveBall() && Greenfoot.mouseClicked(null)) 
            {
                // release ball 
                start = true;  
                mouseX = mouse.getX();
                mouseY= mouse.getY();  
                // launches ball according to angle of launch
                launchBall(mouseX,mouseY);   
                // removes pointer
                removeObject(aim);
            }
        }

    }
    // move paddle accordingly
    public void movePaddle(int distance)
    {
        paddle.moveMe(distance);
    }

    // checks to see if start new level
    public void checkLevel()
    {
        if(getObjects(Brick.class).isEmpty()) 
        {
            // remove ball from world. Reset into original location. removeObject(ball); does NOT work.
            removeObjects(getObjects(Ball.class)); 
            // reset to original location
            resetPosition();
            // increase level by 1 and call upon next level.
            level++;
            nextLevel();   
        }
    }

    // create new level 
    public void nextLevel()
    {
        // fader effect. Consume screen
        fader = new Fader();
        addObject (fader, 400, 300); 

        // level 2 map
        if (level==2)
        {         
            // refreshes level counter
            levelNum.update(2);
            // fades the screen back in
            fader.fadeBackIn();   

            /**
             *  "C"
             */

            addObject( new Brick(1), 198, 90);  
            addObject( new Brick(4), 98, 43);  
            addObject( new Brick(6), 148, 226);  
            addObject( new Brick(4), 162, 49);  
            addObject( new Brick(1), 62, 90);  
            addObject( new Brick(4), 198, 196);  
            addObject( new Brick(4), 83, 214);  
            addObject( new Brick(4), 54, 175); 
            addObject( new Brick(4), 47, 128);

            /**
             * "O"
             */
            addObject( new Brick(3), 384, 53);  
            addObject( new Brick(3), 324, 53);  
            addObject( new Brick(1), 284, 90);  
            addObject( new Brick(1), 429, 90);  
            addObject( new Brick(3), 277, 134);  
            addObject( new Brick(5), 358, 213);  
            addObject( new Brick(3), 412, 184);  
            addObject( new Brick(3), 297, 185); 
            addObject( new Brick(3), 434, 137); 

            /**
             * "M"
             */
            addObject( new Brick(2), 665, 53);  
            addObject( new Brick(2), 665, 215);  
            addObject( new Brick(1), 560, 90);  
            addObject( new Brick(1), 613, 90);  
            addObject( new Brick(6), 588, 135);  
            addObject( new Brick(2), 665, 105);  
            addObject( new Brick(2), 508, 163);  
            addObject( new Brick(2), 665, 159);  
            addObject( new Brick(2), 508, 215);  
            addObject( new Brick(2), 508, 53);  
            addObject( new Brick(2), 508, 111);              

        }        
        // level 3
        else if (level==3)
        {
            // refreshes score counter
            levelNum.update(3);
            // fades screen back in
            fader.fadeBackIn();   

            /**
             *  "S"
             */

            addObject( new Brick(1), 124, 297);  
            addObject( new Brick(4), 172, 261);  
            addObject( new Brick(2), 179, 212);  
            addObject( new Brick(6), 141, 173);  
            addObject( new Brick(6), 75, 160);  
            addObject( new Brick(2), 44, 122);  
            addObject( new Brick(4), 57, 82);  
            addObject( new Brick(1), 94, 48); 
            addObject( new Brick(3), 145, 77);
            addObject( new Brick(5), 177, 111);
            addObject( new Brick(3), 63, 265);  
            addObject( new Brick(5), 48, 223);  

            /**
             * "C"
             */

            addObject( new Brick(5), 391, 87);  
            addObject( new Brick(4), 344, 57);  
            addObject( new Brick(1), 286, 94);  
            addObject( new Brick(2), 274, 157);  
            addObject( new Brick(2), 273, 217); 
            addObject( new Brick(1), 294, 266); 
            addObject( new Brick(4), 355, 294);  
            addObject( new Brick(5), 408, 260); 

            /**
             * "I"
             */
            addObject( new Brick(6), 612, 261);  
            addObject( new Brick(6), 555, 296);  
            addObject( new Brick(6), 610, 297);  
            addObject( new Brick(3), 576, 67);  
            addObject( new Brick(3), 645, 66);  
            addObject( new Brick(3),580, 179);  
            addObject( new Brick(6), 554, 262);  
            addObject( new Brick(3), 546, 118);  
            addObject( new Brick(3), 619, 119);    
            addObject( new Brick(3), 505, 67); 

        }       

    }

    // launching ball to commence game
    public void launchBall(int mouseX, int mouseY)
    {
        paddle.releaseBall(mouseX,mouseY);
    }

    public void replaceBall()
    {
        // call method from paddle to create new ball Actor into world
        paddle.newBall();
    }

    // resets paddle, ball to original position after new level is called
    public void resetPosition()
    {
        start = false;      
        paddle.setLocation(getWidth()/2, getHeight()-25);    
        // BALL setlocation
        // create new ball since old ball was removed from world.
        replaceBall();
        addObject(aim,paddle.getX(),paddle.getY()-20);
    }

    // resets paddle, ball to original position after life lost
    public void getStartAgain()
    {
        start = false;

        paddle.setLocation(getWidth()/2, getHeight()-25);        
        addObject(aim,paddle.getX(),paddle.getY()-20);

    }
}

 