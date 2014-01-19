import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Pointy extends Actor
{
    // declare new instance variable
    private int angle = 270;
    
    // each act, check mouse input and rotation   
    public void act() 
    {
        checkMouse();      
        setRotation(angle);
    }
    
    // checks mouse for player input
    public void checkMouse() 
    {
        MouseInfo mouse = Greenfoot.getMouseInfo ();
        if (mouse != null)
        {
            // pointer turns towers mouse position (coordinate)
            turnTowards (mouse.getX(), mouse.getY());            

        }        
    }
    
    // Extracted from Greenfoot 2.2.1. Turns towards cursor location

    public void turnTowards (int x, int y)
    {
        double a = Math.atan2(y - this.getY(), x - this.getX());
        angle = (int) Math.toDegrees(a);
    }
    
    // accessor to extract mouse information (location)
    public void getPosition(int getXCoordinate)
    {
        setLocation(getXCoordinate,getY());
    }
    
    // mutator to return angle of inclination
    public int giveAngle()
    {
        return angle;
    }

}