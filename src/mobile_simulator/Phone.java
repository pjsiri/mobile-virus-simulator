/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mobile_simulator;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */

// Question: “Which object(s) have you chosen for the synchronize? Why?”
//
// Answer: I have chosen "this.repairShop" object to synchronize, as this object is shared between phone threads. Therefore
//         preventing the race condition.
//         

public class Phone implements Runnable{
    int x = 0;
    
    int y = 0;
    
    int vx = 0;
  
    int vy = 0;
  
    int delay = 0;
  
    int width;
  
    int height;
  
    int id;
  
    int time = 0;
  
    int health;
  
    boolean isInfected = false;
  
    boolean goRepairShop = false;
    
    RepairShop repairShop;
    
    public Phone(int id, RepairShop repairShop) 
    {
        this.delay = 10;
        this.x = (int)(Math.random() * 974.0D);
        this.y = (int)(Math.random() * 648.0D);
        this.vx = (int)((Math.random() * 2.0D - 1.0D) * 5.0D);
        this.vy = (int)((Math.random() * 2.0D - 1.0D) * 5.0D);
        this.id = id;
        this.repairShop = repairShop;
        this.health = 500;
    }
    
    // Method: run
    //
    // This method run is from the Runnable interface. It runs as long as the phone health is greater than 0.
    // Which means the phone is still alive and in the simulation. In the loop, the duration is 10 for the thread.sleep.
    // And if the phone gets infected, the phone health is decrement by 1. The this.repairShop object is synchronized as it
    // is shared by the phone objects. By synchronizing this object as the monitor, it will prevent the race condition.
    // Then, if checks if the repairShop is free, if free, then it sets this.gorepairShop to true and setting this.hospital.isFree to false.
    // This means the phone is signaling that it needs to head to the repairShop. Then it calls, goRepairShop method which sends the phone to the
    // repairShop to make it healthy again. Finally, the loop continues until the phone health is 0 and the phone will be removed from the simulation.
    
    @Override
    public void run() 
    {
        while (this.health > 0) 
        {
          try 
          {
            Thread.sleep(this.delay);
          } 
          catch (InterruptedException ex) 
          {
            Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, (String)null, ex);
          } 
          
          if (this.isInfected) 
          {
            this.health--;
            synchronized (this.repairShop) 
            {
              if (this.repairShop.isFree) 
              {
                this.repairShop.isFree = false;
                this.goRepairShop = true;
              } 
            } 
            
            if (this.goRepairShop) 
            {
              if (!goToRepairShop()) 
              {
                this.goRepairShop = false;
                inRepairShop();
              } 
              continue;
            } 
            move();
            continue;
          } 
          move();
        } 
    }
    
    // Method: run
    //
    // This method allows the movement behaviour of the phone objects in the simulation. Firstly, the method checks
    // whether the object's x location is greater than the width or if the y location is less than 0. If true, the it will
    // go in reverse of the object horizontal velocity (vx). It does this through mutiplying it by -1. Which means it will effectively
    // bounce the object off the walls when reached. Then it checks the y location if it is greater than the height of the panel space.
    // if so, then it will move in reverse direction of the phone vertical velocity, therefore bouncing it off the top of bottom walls.
    // 
    // Then, the method increases time by 1 and takes the modulus of 100 of the time. This causes the time variable to reset to 0
    // when it does reach 100. When the time does reach 0, the method updates the horizontal and vertical velocity with a random speed between -5 and 5.
    // and scaling the result result to a range of -1 to 1 and then mutiplying it by 5.
    //
    // Finally, the method updates the object x,y location by adding the current vx and vy to x and y direction based of its speed.
    
    
    private void move() 
    {
        if (this.x > this.width || this.x < 0)
        {
          this.vx *= -1; 
        }
        if (this.y > this.height || this.y < 0)
        {
          this.vy *= -1; 
        }
        
        this.time++;
        this.time %= 100;
        
        if (this.time == 0) 
        {
          this.vx = (int)((Math.random() * 2.0D - 1.0D) * 5.0D);
          this.vy = (int)((Math.random() * 2.0D - 1.0D) * 5.0D);
        } 
        this.x += this.vx;
        this.y += this.vy;
    }
    
    // Method: goToRepairShop
    //
    // This method represent the movement of the phone in the simulation when it moves towards the repairShop in the GUI.
    // Firstly, the method calculates the horizontal and vertical speed needed to get to the repairShop, which is represent by this.repairShop.
    // If the phone current x location is less than the repairshop x location. Then it sets the x velocity is 3. Which makes the phone move to the right
    // to the repair Shop. If the x location is greater than the repairshop x location, then it moves to the left of the repairShop by setting the vx to -3.
    // Then, when the x and repairshop x is the same, it will set the vx speed to 0, therefore, stopping the phone at the repairShop.
    // This method also calculates the y location as well, and whether it moves up or down to the repairShop. When the y location is the same as the repairshop,
    // it will stop and be repaired by the repairShop.
    //
    // This method returns true if the object is not at the repairShop, else it will return false, which mean it has arrived at the repairShop.
    
    private boolean goToRepairShop() 
    {
        if (this.x < this.repairShop.x) 
        {
            this.vx = 3;
        } 
        else if (this.x > this.repairShop.x) 
        {
            this.vx = -3;
        } 
        else 
        {
            this.vx = 0;
        }

        if (this.y < this.repairShop.y) 
        {
            this.vy = 3;
        } 
        else if (this.y > this.repairShop.y) 
        {
            this.vy = -3;
        } 
        else 
        {
            this.vy = 0;
        }

        this.x += this.vx;
        this.y += this.vy;

        return (Math.abs(this.x - this.repairShop.x) > 5 || Math.abs(this.y - this.repairShop.y) > 5);
    }
    
    // Method: inRepairShop
    //
    // This method represent the phone being in the repair Shop. After the sleep duration in the try and catch block, 
    // The method will set the phone health to 500, and the status of isInfected to false, and setting the repairshop.isfree to true.
    // Therefore, indicating that the repairShop is now avaliable to use for other infected phones.
    
    private void inRepairShop() 
    {
        try 
        {
          Thread.sleep(3000L);
        } 
        catch (InterruptedException ex) {
          Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, (String)null, ex);
        } 
        this.health = 500;
        this.isInfected = false;
        this.repairShop.isFree = true;
    }
}