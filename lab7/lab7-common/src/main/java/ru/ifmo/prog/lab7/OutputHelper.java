package ru.ifmo.prog.lab7;

public class OutputHelper {
    public static void print(String threadName, String message) {
        System.out.printf("%s: %s%n", threadName, message);
    }
}
