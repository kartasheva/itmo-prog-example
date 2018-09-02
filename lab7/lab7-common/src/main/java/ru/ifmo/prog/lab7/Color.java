package ru.ifmo.prog.lab7;

import java.util.Objects;

public class Color {
    private Integer red;
    private Integer green;
    private Integer blue;

    public Color() {
        this.red = 0xFF;
        this.green = 0xFF;
        this.blue = 0xFF;
    }

    public Color(Integer red, Integer green, Integer blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(java.awt.Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public Color(String color) {
        this.red = Integer.valueOf(color.substring( 1, 3 ), 16);
        this.green = Integer.valueOf(color.substring( 3, 5 ), 16);
        this.blue = Integer.valueOf(color.substring( 5, 7 ), 16);
    }

    public Integer getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

    public Integer getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public Integer getBlue() {
        return blue;
    }

    public void setBlue(Integer blue) {
        this.blue = blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Objects.equals(red, color.red) &&
                Objects.equals(green, color.green) &&
                Objects.equals(blue, color.blue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }

    @Override
    public String toString() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public java.awt.Color toAWTColor() {
        return new java.awt.Color(red, green, blue);
    }
}
