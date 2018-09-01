package ru.ifmo.prog.lab7;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class ClientGraphics extends JFrame {


    public void windowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setSize(500, 400);
    }

    public ClientGraphics(){
        super("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
       windowLocation();
       Figure figure1 = new Figure(50, 50, Color.BLUE, 100, 100);
       Graphics g = new Graphics() {
           @Override
           public Graphics create() {
               return null;
           }

           @Override
           public void translate(int x, int y) {

           }

           @Override
           public Color getColor() {
               return null;
           }

           @Override
           public void setColor(Color c) {

           }

           @Override
           public void setPaintMode() {

           }

           @Override
           public void setXORMode(Color c1) {

           }

           @Override
           public Font getFont() {
               return null;
           }

           @Override
           public void setFont(Font font) {

           }

           @Override
           public FontMetrics getFontMetrics(Font f) {
               return null;
           }

           @Override
           public Rectangle getClipBounds() {
               return null;
           }

           @Override
           public void clipRect(int x, int y, int width, int height) {

           }

           @Override
           public void setClip(int x, int y, int width, int height) {

           }

           @Override
           public Shape getClip() {
               return null;
           }

           @Override
           public void setClip(Shape clip) {

           }

           @Override
           public void copyArea(int x, int y, int width, int height, int dx, int dy) {

           }

           @Override
           public void drawLine(int x1, int y1, int x2, int y2) {

           }

           @Override
           public void fillRect(int x, int y, int width, int height) {

           }

           @Override
           public void clearRect(int x, int y, int width, int height) {

           }

           @Override
           public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

           }

           @Override
           public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

           }

           @Override
           public void drawOval(int x, int y, int width, int height) {

           }

           @Override
           public void fillOval(int x, int y, int width, int height) {

           }

           @Override
           public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

           }

           @Override
           public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

           }

           @Override
           public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

           }

           @Override
           public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

           }

           @Override
           public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

           }

           @Override
           public void drawString(String str, int x, int y) {

           }

           @Override
           public void drawString(AttributedCharacterIterator iterator, int x, int y) {

           }

           @Override
           public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
               return false;
           }

           @Override
           public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
               return false;
           }

           @Override
           public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
               return false;
           }

           @Override
           public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
               return false;
           }

           @Override
           public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
               return false;
           }

           @Override
           public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
               return false;
           }

           @Override
           public void dispose() {

           }
       };
       paint(g);



    }

    public void paint(Graphics g){
        g.drawRect(100,100,100,100);
        g.setColor(Color.BLUE);
        g.fillRect(101,101,99,99);

    }
    }
