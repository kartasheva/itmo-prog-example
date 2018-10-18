package ru.ifmo.prog.lab7;

public class Counetr {
    private Double counter = 0.0;

    public Double appendToCounter(Integer value) {
        counter = counter + value;
        return counter;
    }

    public Double clear() {
        counter = 0.0;
        return counter;
    }

    public Double getCounter() {
        return counter;
    }

    public void setCounter(Double counter) {
        this.counter = counter;
    }
}
