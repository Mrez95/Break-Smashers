import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Live here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lives extends Actor
{
    // Call Greenfoot image command
    private GreenfootImage image;
    public Lives()
    {
        // sets new image as life "bar". Total 3.
        image = new GreenfootImage("paddle2.png");
        // shrink to scale
        image.scale(43,13);
        // add image to world
        setImage(image);
    }     
}
