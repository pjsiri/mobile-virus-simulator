/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile_simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Panel extends JPanel implements KeyListener, ComponentListener {
    
  Phone[] phones = new Phone[1];
  
  Thread[] threads = new Thread[1];
  
  int width = 974;
  
  int height = 648;
  
  int number_phones = 1;
  
  int infectionRange = 20;
  
  RepairShop repairShop = new RepairShop(490, 530);
  
  ProgressBar progressBar = new ProgressBar(440,570);
  
  int totalInfected = 0;
  
  int totalGoRepairShop = 0;
  
  Image image;
  Image image1;
  
  public Panel() 
  {
    addKeyListener(this);
    addComponentListener(this);
    setFocusable(true);

    int i = 0;
    while (i < this.phones.length) 
    {
      this.phones[i] = new Phone(this.number_phones, this.repairShop);
      this.threads[i] = new Thread(this.phones[i]);
      this.threads[i].start();
      i++;
    } 

    this.progressBar.start();
    image = new ImageIcon("phone2.png").getImage();
    image1 = new ImageIcon("repairShop.png").getImage();
  }
  
  // Method: addPhone
  //
  // This method takes a array of phone objects and returns a new array of phone objects with the additional phones added to the end.
  // Firstly, it creates a new array of phone objects called newPhones. It then loops through the array using a while loop and copies each element
  // from phone to newPhones, after the loop it creates a new phone object assigns the last element of newPhones.
  // it sets the width and height of the newly created phones to this.width and this.height.
  // 
  // Finally, it returns the new Phones array which now contain the original phones array elements followed with the newly added phone object at the end.
  
  private Phone[] addPhone(Phone[] phone) 
  {
    Phone[] newPhones = new Phone[phone.length + 1];
    int i = 0;

    while(i < phone.length) 
    {
        newPhones[i] = phone[i]; 
        i++;
    }

    newPhones[newPhones.length - 1] = new Phone(++this.number_phones, this.repairShop);
    (newPhones[newPhones.length - 1]).width = this.width;
    (newPhones[newPhones.length - 1]).height = this.height;

    return newPhones;
  }
  
  // Method: addThread
  //
  // This method takes a array of threads and returns a new array of thread objects with an additional thread object added at the end.
  // Firstly, it creates a new thread array and uses a do while loop to copy all the threads given to the newThreads.
  // Then, after the loop it returns the newThreads.
  
  private Thread[] addThread(Thread[] threads) 
  {
  Thread[] newThreads = new Thread[threads.length + 1];
  int i = 0;
  
  if (threads.length > 0) 
  {
    do 
    {
      newThreads[i] = threads[i]; 
      i++;
    } 
    while (i < threads.length);
  }
  
  return newThreads;
  }
  
  // Method: removePhone
  //
  // This method takes an array of phone objects and index, returns a new array of phone objects with element at index removed
  // It does this by creating a new array of phones with the array given length -1 and using a while loop to go through the array given.
  // It then assigns the value phone[index] to phones[phones.length -1]. Thereofre, removing the index and replacing it with the last element.
  // Next it goes through newphones and copies each phone to the newPhones array.
  //
  // Finally, it returns the newPhones array with the phone removed at the given index.
  
  private Phone[] removePhone(Phone[] phones, int position) 
  {
    Phone[] newPhones = new Phone[phones.length - 1];
    phones[position] = phones[phones.length - 1];

    int i = 0;
    while (i < newPhones.length) 
    {
        newPhones[i] = phones[i];
        i++;
    }

    return newPhones;
  }
  
  // Method: addRec
  //
  // This method adds the rectangle object that represent a phone in the simulation. It starts off by creating a new thread
  // for the newly added phone object. It calls the addPhone method, Therefore, adding a new phone to the phone array.
  // Next, it creates a new thread object for the newly created phone object by accessingg the last element. And then passing it through
  // to the thread constructor. Where it will assign the newly created thread object to the last elemt in the thread array.
  //
  // Finally, this method starts the thread, which results in the run method of the object being ran in a seperate thread.
  
  private void addRec() 
  {
    this.phones = addPhone(this.phones);
    
    this.threads = addThread(this.threads);
    
    Thread newThread = new Thread(this.phones[this.phones.length - 1]);
    this.threads[this.threads.length - 1] = newThread;
    
    newThread.start();
  }
  
  public void paint(Graphics g) 
  {
    paintComponent(g);
    setBackground(Color.BLACK);
    this.totalInfected = 0;
    this.totalGoRepairShop = 0;
    g.drawImage(image, 450, 10, this);
    g.drawImage(image1, 460, 670, this);
    g.setColor(Color.WHITE);
    String repairShop = "| REPAIR SHOP |";
    g.drawString(repairShop, this.repairShop.x - 30, this.repairShop.y);
    g.drawString("| OPEN FOR BUSINESS |", this.repairShop.x - 50, this.repairShop.y+15);
    for (int i = 0; i < this.phones.length; i++) 
    {
      if ((this.phones[i]).health > 0) 
      {
        if ((this.phones[i]).isInfected && !(this.phones[i]).goRepairShop) 
        {
          g.setColor(Color.RED);
          this.totalInfected++;
        } 
        else if ((this.phones[i]).isInfected && (this.phones[i]).goRepairShop) 
        {
          g.setColor(Color.GREEN);
          this.totalGoRepairShop++;
        } 
        else 
        {
          g.setColor(Color.blue);
        } 
        g.fillRect((this.phones[i]).x, (this.phones[i]).y, 10, 10);
        
        if ((this.phones[i]).isInfected) 
        {
            for (Phone phone : this.phones) 
            {
                if (distance(this.phones[i], phone) < this.infectionRange) 
                {
                    (phone).isInfected = true;
                }
            } 
          (this.phones[i]).repairShop = this.repairShop;
        } 
      } 
      else 
      {
        if ((this.phones[i]).goRepairShop)
          this.repairShop.isFree = true; 
        this.phones = removePhone(this.phones, i);
      } 
    } 
    g.setColor(Color.BLUE);
    g.drawString("Total Mobile Phones: " + this.phones.length, 30, 540);
    g.setColor(Color.RED);
    g.drawString("Total Infected Phones: " + this.totalInfected, 30, 555);
    g.setColor(Color.GREEN);
    g.drawString("Total Going to Repair: " + this.totalGoRepairShop, 30, 570);
    if(!this.repairShop.isFree)
    {
        this.progressBar.paint(g);
        g.setColor(Color.WHITE);
        String progress = "Progress Bar: Repairing Phones! ";

        g.drawString(progress, this.repairShop.x-68, this.repairShop.y+34);
    }
    
    int[] data = {10, 20, 30, 40, 50}; 
  
    int barWidth = 10;
    int barGap = 15;
    int x = 800;
    int y = 550;
    g.setColor(Color.WHITE);
    g.drawString("Mobile phone status", x, 575);


    g.setColor(Color.WHITE);
    g.drawLine(x, y + barWidth, x + data.length * (barWidth + barGap), y + barWidth);
    g.setColor(Color.WHITE);

    g.setColor(Color.red);
    int InfectedbarHeight = this.totalInfected * 5;
    g.fillRect(x + 1 * (barWidth + barGap), y + barWidth - InfectedbarHeight, barWidth, InfectedbarHeight);

    g.setColor(Color.blue);
    int mobileBarHeight = this.phones.length * 5;
    g.fillRect(x + 2 * (barWidth + barGap), y + barWidth - mobileBarHeight, barWidth, mobileBarHeight);

    g.setColor(Color.green);
    int repairBarHeight = this.totalGoRepairShop * 5;
    g.fillRect(x + 3 * (barWidth + barGap), y + barWidth - repairBarHeight, barWidth, repairBarHeight);
    repaint();
  }
  
  private float distance(Phone b1, Phone b2) 
  {
    return (float)Math.sqrt(Math.pow((b1.x - b2.x), 2.0D) + Math.pow((b1.y - b2.y), 2.0D));
  }
  
  public void keyTyped(KeyEvent ke) {}
  
  // Method: keyPressed
  //
  // This method allows the user to press the up arrow to increase the number of phones in the simuation.
  // and when the user presses v, it will randomly select a phone to become infected with a virus, needing it to be brought
  // to the repairShop.
  
  public void keyPressed(KeyEvent ke) 
  {
    if (ke.getKeyCode() == 38)
    {
      addRec(); 
    }
    if (ke.getKeyCode() == 86)
    {
      (this.phones[(int)(Math.random() * this.phones.length)]).isInfected = true; 
    }
  }
  
  public void keyReleased(KeyEvent ke) {}
  
  public void componentResized(ComponentEvent ce) 
  {
    this.width = getWidth();
    this.height = getHeight();
    for (int i = 0; i < this.phones.length; i++) 
    {
        (this.phones[i]).height = this.height;
        (this.phones[i]).width = this.width;
    } 
  }
  
  public void componentMoved(ComponentEvent ce) {}
  
  public void componentShown(ComponentEvent ce) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public void componentHidden(ComponentEvent ce) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}