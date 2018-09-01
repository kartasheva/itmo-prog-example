package ru.ifmo.prog.lab7;

import javax.swing.*;
import java.awt.*;

public class Figure extends JComponent {
     private Color color;
     private int x;
     private int y;
     private int width;
     private int height;

     public Figure(int x, int y, Color color, int width, int height){
         this.x = x;
         this.y = y;
         this.color = color;
         this.width = width;
         this.height = height;

     }

     public int getX(){
         return x;
     }

     public void setX(int x){
         this.x = x;
     }

     public int getY(){
         return y;
     }

     public void setY(int y){
         this.y = y;
     }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getWidth(){
        return width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int hetHeight(){
        return height;
    }



    public void paint(Graphics2D g){
         g.drawRect(x, y, width, height);
         g.setColor(color);
         g.fillRect(x+1, y+1, width-1, height-1);

     }
}
