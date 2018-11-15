package ru.ifmo.prog.lab8;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Cart implements Comparable<Cart> {
    private Boolean clatterOfHooves;
    private String title;
    private Integer size;
    private Double x;
    private Double y;
    private OffsetDateTime createdAt;
    private LabColor labColor;


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
        createdAt = OffsetDateTime.now();
        x = 0.0;
        y = 0.0;
    }

    public Cart(boolean clatterOfHooves, String title, Integer size, Double x, Double y, OffsetDateTime createdAt, LabColor labColor) {
        this.clatterOfHooves = clatterOfHooves;
        this.title = title;
        this.size = size;
        this.x = x;
        this.y = y;
        this.createdAt = createdAt;
        this.labColor = labColor;
    }

    public Boolean isClatterOfHooves() {
        return clatterOfHooves;
    }

    public void setClatterOfHooves(Boolean clatterOfHooves) {
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

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LabColor getLabColor(){ return labColor; }

    public void setLabColor(LabColor labColor){ this.labColor = labColor; }

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
                Objects.equals(labColor, cart.labColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clatterOfHooves, title, size, x, y, createdAt, labColor);
    }
}
