import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Smoke here.
 * 
 * University of Kent
 * 18km
 */
public class Smoke  extends Actor
{
    // declare instance variables
    private GreenfootImage image;   // the original image
    private int fade;               // the rate of fading

    // smoke constructor
    public Smoke()
    {
        // initalize smoke image and fades it with each act
        image = getImage();
        fade = Greenfoot.getRandomNumber(4) + 1;  // 1 to 3
        if (fade > 3) {
            fade = fade - 2;
        }        
    }

    public void act() 
    {
        // fades the smoke image with each act
        shrink();
    }    

    private void shrink()
    {
        // method to "fade" smoke object with each act
        if(getImage().getWidth() < 10 || (getWorld().getObjects(Ball.class).isEmpty() )) {
            // remove smoke when out of world
            getWorld().removeObject(this);
        }
        else {
            // "fades" in size with each act
            GreenfootImage img = new GreenfootImage(image);
            img.scale ( getImage().getWidth()-fade, getImage().getHeight()-fade );
            setImage (img);
        }        
    }
}
