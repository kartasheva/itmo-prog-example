package ru.ifmo.prog.lab5;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        Collection<Cart> collection = null;
        try {
            String filename = args[0];
            collection = new Collection<>(filename, Cart.class);
            collection.show();

            try (Scanner scan = new Scanner(System.in)){
                for(;;){
                    if (scan.hasNextLine()) {
                        String command = scan.nextLine();
                        if (command.startsWith("removeAll")) {
                            if (collection.removeAll(mapper.readValue(command.split(" ")[1], Cart.class))) {
                                System.out.println("Items removed!");
                            }
                        } else if (command.startsWith("remove")) {
                            if (collection.remove(command.split(" ")[1]) == null) {
                                System.out.println("Key not found!");
                            } else {
                                System.out.println("Item removed!");
                            }
                        } else if (command.startsWith("clear")) {
                            collection.clear();
                            System.out.println("Collection is empty!");
                        } else if (command.startsWith("load")) {
                            collection.load();
                            System.out.println("Collection is reloaded!");
                        } else if (command.startsWith("show")) {
                            collection.show();
                        } else if (command.startsWith("exit")) {
                            System.out.println("Bye!");
                            return;
                        } else {
                            System.out.println("Incorrect command");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (collection != null) {
                try {
                    collection.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
