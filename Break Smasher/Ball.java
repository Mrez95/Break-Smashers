import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import static java.lang.Math.*;

/**
 * Write a description of class Ball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ball extends SmoothMover
{
    // declare variable, boolean and class
    private double changeX;      
    private double changeY;     
    private Paddle paddle = new Paddle();
    private Brick brick;   
    private LargePaddle paddleUpgrade;
    private SmallPaddle paddleDowngrade;
    private int count = 2;
    //declare constants. Ball size
    private final double BALLX = 350;
    private final double BALLY = 505;  

    // set true since ball is on paddle at begining of level
    private boolean onPaddle = true; 
   

    /**
     * Act - do whatever the Ball wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    // each act, check for user input, make smoke and check death
    public void act() 
    {
        if (!onPaddle){
            moveBall();                             
            checkMiss();           
            makeSmoke();            
        }
    }    
    
    // smoke effect, called from smoke class
    private void makeSmoke()
    {
        count--;
        // the -15 makes the ball stop producing smoke 15 pixels from base. 
        //This fixes the Actor not in World bug as it will no longer go searching for getY of ball        
        if (count == 0 && (getY() < getWorld().getHeight()-20) ) {
            getWorld().addObject ( new Smoke(), getX(), getY());
            count = 2;
        }
    }

    public void moveMe()
    {
        // no need to cast as smooth mover can handle doubles
        setLocation ((getX() + changeX), (getY() + changeY));
        // checks collision dectection with paddle, brick and world edge
        checkPaddle();
        checkOutOfBound();  
        checkBrick();
    }
    // check collision detection with wall
    public void checkOutOfBound()
    {
        if (getX() == 0 || getX() == getWorld().getWidth()-1) {
            changeX = -changeX;
            // sound effect
            Greenfoot.playSound("baseball.wav");
        }
        // Makes ball move in opposite direction after collision
        if (getY() == 0) {
            changeY = -changeY;
        }      

    }
    
    // collision dectection with brick
    public void checkBrick(){
        Brick brick = (Brick)getOneIntersectingObject(Brick.class);
        if ( brick != null )
        {
            if (getY() > brick.getY() || getY() < brick.getY())
            {
                changeY = -changeY;
                // Fixes multi-kill bug
                setLocation(getX(),getY()+1);
                // 1 in 10 hits will recieve upgrade (enlarge Paddle)
                if (Greenfoot.getRandomNumber(10) == 1)
                {
                    // add powerup to drop
                    paddleUpgrade = new LargePaddle();                    
                    getWorld().addObject ( paddleUpgrade, brick.getX(), brick.getY());       
                    paddleUpgrade.fall();
                }
                // 1 in 10 hits will recieve downgrade (shrink Paddle)
                if (Greenfoot.getRandomNumber(10) == 5)
                {
                    // add powerdown to drop
                    paddleDowngrade = new SmallPaddle();
                    getWorld().addObject ( paddleDowngrade, brick.getX(), brick.getY());       
                    paddleDowngrade.fall();
                }
            }
            else
            {   
                // moves ball in opposite direction after collision
                changeX = -changeX;
            }
            // changes brick appearance accordingly
            brick.effect();
            // sound effect
            Greenfoot.playSound("laser.wav");
        }
    }

    // delete ball when passes MinX
    private void checkMiss() 
    {
        if (getY() == getWorld().getHeight()-1) {
            // send to method for update on counter
            ballDead();
            getWorld().removeObject(this);  
        }
    }

    public void ballDead()
    {
        // reset to original position. Updates status to world
        ((MyWorld)getWorld()).getStartAgain();
        ((MyWorld) getWorld()).takeLife();           
    }
    // checks to see if ball made contact with paddle
    private void checkPaddle() 
    {
        Paddle paddle = (Paddle) getOneIntersectingObject(Paddle.class);
        if (paddle != null)
        {             
            // bounce if made contact
            bounce(paddle);
        }            
    }

    // ball collision with paddle
    private void bounce(Actor a) {
        // reflect opposite side
        changeY = -changeY;
        // refelcts depending on incoming angle
        int reflected = getX() - a.getX();
        // caculate angle of reflection based on incoming angle
        // divide by 8 to minimize the rebound magnitude. Not as dramatic/hard.
        changeX = changeX + (reflected/8);        
        if (changeX > 7) {
            changeX = 7;
        }
        if (changeX < -7) {
            changeX = -7;
        }
        // sound effect
        Greenfoot.playSound("laser.wav");
    }

    public void replaceBall()
    { 
        // sends information to world class (MyWorld) to call upon replaceBall method to create a new ball in place of old one.
        ((MyWorld) getWorld()). replaceBall();       
    }

    public void moveBall()
    {
        // Move ball along with paddle accordingly if unlaunched
        if (!onPaddle) {
            moveMe();
        }        
    }

    /**
     * Method by Eddie Zhang
     */
    private void trajectoryPath(int mouseX, int mouseY)
    {
        // method created to calculate tragectory path. 
        // calcuates distance of cursor from ball. 
        double displacedX = abs(mouseX - BALLX);
        double displacedY = abs(mouseY) - BALLY;
        // Cosine Law to isolate for hypotenuse length
        double hypotenuse = abs(sqrt(( pow(displacedX,2)+ pow(displacedY,2))- (( 2*displacedX*displacedY)*(cos(90)))));
        /**finds angle of trajectory
        // 10 represents the length of travel for each act. personal preference
        // multiply by 180/PI to convert from radians to degrees
        // abs = absolute value
        // 1 degree = 180/PI radians (MUST convert to radians to use this method */ 
        double theta = abs(atan(displacedY/displacedX));
        // pythagorean theorem        
        double actChangeX = abs((cos(theta))*8);
        // never negative so uses absolute value
        double actChangeY = abs((sin(theta))*8);  
        // assign value accordinging to left or right aim
        changeY = -actChangeY;
        if (mouseX < BALLX)
        {
            changeX = -actChangeX;
        }
        else
        {
            changeX = actChangeX;
        }
    }

    public void launch(int mouseX,int mouseY)
    { 
        // change to negative so ball can move upwards
        trajectoryPath(mouseX,mouseY);
        // ball launched
        onPaddle = false;
    }
    // move ball along with paddle if not launched
    public void move(int distance) 
    {
        setLocation(getX() + distance, getY());
    }
}
