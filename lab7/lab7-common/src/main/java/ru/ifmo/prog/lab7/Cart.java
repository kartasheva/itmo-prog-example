package ru.ifmo.prog.lab7;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class Cart implements Comparable<Cart> {
    private boolean clatterOfHooves;
    private String title;
    private Integer size;
    private Integer x;
    private Integer y;
    private Date createdAt;
    private Color color;

    public void noise(){
        if (clatterOfHooves){
            System.out.println("Sounds of cart and clatter of hooves");
        }
        else{
            System.out.println("Sounds of cart");
        }
    }

    @Override
    public int compareTo(Cart o) {
        return Boolean.compare(this.clatterOfHooves, o.clatterOfHooves);
    }

    public Cart() {
    }

    public Cart(boolean clatterOfHooves, String title, Integer size, Integer x, Integer y, Date createdAt,Color color) {
        this.clatterOfHooves = clatterOfHooves;
        this.title = title;
        this.size = size;
        this.x = x;
        this.y = y;
        this.createdAt = createdAt;
        this.color = color;
    }

    public boolean isClatterOfHooves() {
        return clatterOfHooves;
    }

    public void setClatterOfHooves(boolean clatterOfHooves) {
        this.clatterOfHooves = clatterOfHooves;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Color getColor(){ return color; }

    public void setColor(Color color){ this.color = color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return clatterOfHooves == cart.clatterOfHooves &&
                Objects.equals(title, cart.title) &&
                Objects.equals(size, cart.size) &&
                Objects.equals(x, cart.x) &&
                Objects.equals(y, cart.y) &&
                Objects.equals(createdAt, cart.createdAt) &&
                Objects.equals(color, cart.color);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clatterOfHooves, title, size, x, y, createdAt, color);
    }
}
