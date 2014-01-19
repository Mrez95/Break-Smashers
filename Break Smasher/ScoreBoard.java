import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)
// allow to change color
import java.awt.Color;
// allow to change font
import java.awt.Font;
import java.awt.Graphics;
public class ScoreBoard extends Actor
{  
    // declare instance variables
    // width of the score board
    private int WIDTH = 85;  
    // height of the score board
    private int HEIGHT = 20;      

    // The constructor composes the image for the ScoreBoard.     
    public ScoreBoard()
    {
        // initalized new image
        GreenfootImage image = new GreenfootImage(WIDTH, HEIGHT);
        // cosmetic porcedures for color and font     
        Font myFont = new Font("TIMES", Font.BOLD ,12);
        image.setFont(myFont);
        // display on screen
        setImage(image); 
        // Set score to 0 at begining
        update(0);
    }

    // updates the score display
    public void update(int score) 
    {
        // x and y relative to the image. baseline of leftmost character.
        int x = 5;      
        int y = 15;        
        // "Repaint" the score display         
        GreenfootImage image = getImage();
        // background color
        image.setColor(Color.DARK_GRAY);
        // "erase" the display
        image.fillRect(1, 1, WIDTH-2, HEIGHT-2);  
        // text color
        image.setColor(Color.WHITE);      

        // display image onto screen in place of the previous
        image.drawString("Score: " + score, x, y);  
        setImage(image);         
    }
}
