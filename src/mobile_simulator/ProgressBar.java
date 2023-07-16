/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mobile_simulator;

/**
 *
 * @author User
 */


import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ProgressBar extends JPanel implements Runnable {
    private int x = 0;
    private int y = 0;
    private int progress = 0;
    private final int timeToRepair = 4;
    private long startTime = 0;
    private Thread thread;
    
    public ProgressBar(int x, int y) 
    {
        this.x = x;
        this.y = y;
    }
    
    // Method: start
    //
    // This method creates a new thread and starts it, this will start the progress bar animation.
    
    public void start() 
    {
        thread = new Thread(this);
        thread.start();
    }
    
    // Method: run
    //
    // This method run is the from the runnable interface, which updates the progression of the bar based on the time
    // since it started, and will repaint the bar by calling the paint method.
    
    public void run() 
    {
        startTime = System.currentTimeMillis();
        while (!Thread.interrupted()) {
            long currentTime = System.currentTimeMillis();
            int elapsedTime = (int)(currentTime - startTime);
            progress = (int)((float)elapsedTime / (float)(timeToRepair * 1000) * 100);
            if (progress >= 100) 
            {
                progress = 0;
                startTime = System.currentTimeMillis();
            }
            repaint();
            try 
            {
                Thread.sleep(50);
            } 
            catch (InterruptedException e) 
            {
                break;
            }
        }
    }
    
    // Method: paint
    //
    // This method is override to draw the progress bar using the graphics methods. Firstly, it clears the last drawing
    // with a white rectangle, then draws the progress bar using the blue rectangle, and finally draws the black border around
    // the progress bar
    
    public void paint(Graphics g) 
    {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 100, 10);
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 100 * progress / 100, 10);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 100, 10);
    }
}