import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Paddle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Paddle extends Actor
{
    // Declare class
    private Ball ball ;  
    private int enlarge ;
    private int shrink;        

    /**
     * Act - do whatever the Paddle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    // add new ball into world. Else NullPointerExeception
    public void addedToWorld(World world) 
    {
        newBall();
    } 

    public void act()
    {
        // sends information to expandImage method where it stretches the paddle
        if(enlarge > -1) {
            expandImage(getImage().getWidth()+1);
            // expand by 1 more animation
            enlarge++;
            // stop expansion when reached 25 times
            if(enlarge > 25) {
                enlarge = -1;
            }                   
        }
        // sends information to expandImage method where it stretches the paddle
        else if	(shrink > -1) {
            shrinkImage(getImage().getWidth()-1);
            // expand by 1 more animation
            shrink++;
            // stop expansion when reached 25 times
            if(shrink > 25) {
                shrink = -1;
            }            
        }
    }

    // method to expand image accordingly, frame by frame
    private void expandImage(int width) {   
        // initailize paddle image
        GreenfootImage img = getImage();
        // change its dimensions according to preference
        img.scale(width, img.getHeight());
        // refresh, display new paddle
        setImage(img);                            
    }

    private void shrinkImage(int width) {    
        if (width >=2)
        {        
            // initailize paddile image
            GreenfootImage img = getImage();
            // change its dimensions according to preference
            img.scale(width, img.getHeight());
            // refresh, display new paddle
            setImage(img);      
        }
    }

    // method called to create new ball after original ball dies and removed from world.
    public void newBall() 
    {
        ball = new Ball();
        // add ontop of paddle    
        getWorld().addObject(ball, getX(), getY()-18);
    }

    // moves paddle accordingly with mouse input
    public void moveMe(int distance)
    {
        setLocation(getX()+distance, getY());
        if(haveBall()) 
        {
            // calls method in ball for ball to move along with paddle
            ball.move(distance);	
        }

    }
    // mutator to access boolean information of ball status
    public boolean haveBall()
    {
        return ball != null;
    }    
    // send value to Ball class to release ball
    public void releaseBall(int mouseX, int mouseY) {
        ball.launch(mouseX,mouseY);
        // no more ball on paddle
        ball = null;
    }

    // method called from power up to start the expansion proccess.
    public void enlarge()
    {
        // 0<-1 thus, meets the if statement
        enlarge = 0;
    }

    // method called from power up to start the shrinking proccess.
    public void shrink()
    {
        // 0<-1 thus, meets the if statement
        shrink = 0;
    }

}
