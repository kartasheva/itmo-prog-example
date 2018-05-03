package ru.ifmo.prog.lab5;

import java.util.Objects;

public class Cart extends Noise implements Comparable<Cart> {
    private boolean clatterOfHooves;
    public void hoovesOn(){
        clatterOfHooves = true;
    }
    public void hoovesOff(){
        clatterOfHooves = false;
    }
    @Override
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

    public boolean isClatterOfHooves() {
        return clatterOfHooves;
    }

    public void setClatterOfHooves(boolean clatterOfHooves) {
        this.clatterOfHooves = clatterOfHooves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return clatterOfHooves == cart.clatterOfHooves;
    }

    @Override
    public int hashCode() {

        return Objects.hash(clatterOfHooves);
    }
}
