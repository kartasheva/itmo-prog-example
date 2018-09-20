package ru.ifmo.prog.lab7;

public class Counetr {
    private Integer counter = 0;

    public Integer appendToCounter(Integer value) {
        counter = counter + value;
        return counter;
    }

    public Integer clear() {
        counter = 0;
        return counter;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
