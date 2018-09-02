package ru.ifmo.prog.lab7;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;

import static ru.ifmo.prog.lab7.MessageType.SHOW_RESPONCE;
import static ru.ifmo.prog.lab7.OutputHelper.print;
import static ru.ifmo.prog.lab7.Server.writeData;

public class Lab7Server {
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        try {
            Server server = new Server();
            Collection<Cart> collection = new Collection<>(args[0], Cart.class);

            SwingUtilities.invokeLater(() -> {
                ServerGraphics serverGraphics = new ServerGraphics(server, collection);
            });

            server.addResponseHandler((message, client) -> {
                try {
                    switch (message.messageType) {
                        case LOAD:
                            collection.load();
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONCE,
                                            collection.getStore()
                                    ));
                            break;
                        case SHOW:
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONCE,
                                            collection.getStore()
                                    ));
                            break;
                        case CLEAR:
                            collection.clear();
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONCE,
                                            collection.getStore()
                                    ));
                            break;
                        case REMOVE:
                            collection.remove((String) message.payload);
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONCE,
                                            collection.getStore()
                                    ));
                            break;
                        case REMOVE_ALL:
                            collection.removeAll(mapper.readValue(mapper.writeValueAsString(message.payload), Cart.class));
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONCE,
                                            collection.getStore()
                                    ));
                            break;
                        case CLOSE:
                            server.stop();
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            server.start();
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        } finally {
            print(Thread.currentThread().getName(), "ended");
        }
    }
}
