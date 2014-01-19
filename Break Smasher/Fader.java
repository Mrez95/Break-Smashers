import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
/**
 * Fader spawns a black square over the board that fades in and out to
 * provide a fading effect
 * 
 * @Jordan Cohen
 * @v0.1.0
 */
public class Fader extends Actor
{
    // Declare new variables
    GreenfootImage fade = new GreenfootImage (800, 600);
    Color black = new Color (0,0,0);
    // contructor 
    public Fader ()
    {
        // create and fill the fader image to pitch black
        fade.setColor (black);
        fade.fill();
        // opaque
        fade.setTransparency (0);
        // display image on world
        setImage (fade);
    }
    // method to create the "fading" effect when end level
    public void addedToWorld (World w)
    {
        // from 255(transparent) to 0 (opaque), we loop through phase by phase to create fading effect
        for (int fader = 0; fader <= 255; fader += 20)
        {
            fade.setTransparency(fader);
            // delay by 0.02 seconds to create better effect
            Greenfoot.delay(2);
        }
    }
    // method to create the "fading" effect when new level begins
    public void fadeBackIn ()
    {
        // slight delay to stay longer in opaque form
        Greenfoot.delay(10);
        // from 0 (opaque) to 255(transparent), we loop through phase by phase to create fading effect        
        for (int fader = 255; fader >= 0; fader -= 20)
        {
            fade.setTransparency(fader);
            // delay by 0.02 seconds to create better effect
            Greenfoot.delay(2);
        }  
        // remove from world after effect ends
        getWorld().removeObject(this);
    }
}