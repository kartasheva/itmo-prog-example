package ru.ifmo.prog.lab8;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

import static ru.ifmo.prog.lab8.MessageType.*;
import static ru.ifmo.prog.lab8.OutputHelper.print;
import static ru.ifmo.prog.lab8.Server.writeData;

public class Lab8Server {
    private static ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();
    private static DatabaseClient<Cart> databaseClient = DatabaseClient.getInstance();

    public static void main(String[] args) {

        try {
            Server server = new Server();
            Collection<Cart> collection = new Collection<>(Cart.class);

            SwingUtilities.invokeLater(() -> {
                ServerGraphics serverGraphics = new ServerGraphics(server, collection);
            });

            server.addResponseHandler((message, client) -> {
                try {
                    String tableName = Cart.class.getName();
                    Class itemClass = Cart.class;
                    switch (message.messageType) {
                        case CREATE:
                            String newKey = "key" + System.nanoTime();
                            databaseClient.createItem(
                                    tableName,
                                    newKey,
                                    mapper.readValue(mapper.writeValueAsString(message.payload), Cart.class));
                            writeData(
                                    client,
                                    new Message(
                                            CREATE_RESPONSE,
                                            databaseClient.readItem(tableName, newKey, itemClass)
                                    ));
                            break;
                        case READ:
                            writeData(
                                    client,
                                    new Message(
                                            READ_RESPONSE,
                                            databaseClient.readItem(tableName, (String) message.payload, itemClass)
                                    ));
                            break;
                        case UPDATE:
                            Cart cart = mapper.readValue(mapper.writeValueAsString(message.payload), Cart.class);
                            databaseClient.updateItem(
                                    tableName,
                                    "key1",
                                    cart);
                            writeData(
                                    client,
                                    new Message(
                                            UPDATE_RESPONSE,
                                            databaseClient.readItem(tableName, "key1", itemClass)
                                    ));
                            break;
                        case DELETE:
                            databaseClient.deleteItem(tableName, (String) message.payload);
                            writeData(
                                    client,
                                    new Message(
                                            DELETE_RESPONSE,
                                            databaseClient.readItem(tableName, (String) message.payload, itemClass)
                                    ));
                            break;
                        case LOAD:
                            collection.load();
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONSE,
                                            collection.getStore()
                                    ));
                            break;
                        case SHOW_FILTRED:
                        case SHOW:
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONSE,
                                            collection.getStore()
                                    ));
                            break;
                        case CLEAR:
                            collection.clear();
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONSE,
                                            collection.getStore()
                                    ));
                            break;
                        case REMOVE:
                            collection.remove((String) message.payload);
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONSE,
                                            collection.getStore()
                                    ));
                            break;
                        case REMOVE_ALL:
                            collection.removeAll(mapper.readValue(mapper.writeValueAsString(message.payload), Cart.class));
                            writeData(
                                    client,
                                    new Message(
                                            SHOW_RESPONSE,
                                            collection.getStore()
                                    ));
                            break;
                        case CLOSE:
                            server.stop();
                            break;
                        default:
                            break;
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            server.start();
        } catch (IOException | SQLException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        } finally {
            print(Thread.currentThread().getName(), "ended");
        }
    }
}
