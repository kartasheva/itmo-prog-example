package ru.ifmo.prog.lab7;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import static java.lang.String.format;
import static ru.ifmo.prog.lab7.Constants.HOST;
import static ru.ifmo.prog.lab7.Constants.PORT;
import static ru.ifmo.prog.lab7.MessageType.SHOW_RESPONCE;
import static ru.ifmo.prog.lab7.OutputHelper.print;

public class Server {
    private static ObjectMapper mapper = new ObjectMapper();
    private JFrame sGraphics;
    private SocketChannel client;
    private Collection<Cart> collection;
    private Message message;

    public static void main(String[] args) {
        try {
            Collection<Cart> collection = new Collection<>(args[0], Cart.class);
            Selector selector = Selector.open();

            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(HOST, PORT));
            print(Thread.currentThread().getName(), format("Server started on %s:%d", HOST, PORT));

            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                print(Thread.currentThread().getName(), "Wait for new connection...");
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {

                    SelectionKey key = selectedKeys.next();

                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocket.accept();
                        Thread clientHandler = new Thread(() -> {
                            print(Thread.currentThread().getName(), "started...");
                            JFrame sGraphics = new ServerGraphics();
                            sGraphics.setVisible(true);

                            try {
                                Selector clientSelector = Selector.open();
                                client.configureBlocking(false);
                                client.register(clientSelector, SelectionKey.OP_READ);
                                while (true) {
                                    clientSelector.select();
                                    Iterator<SelectionKey> clientSelectionKeys = clientSelector.selectedKeys().iterator();
                                    while (clientSelectionKeys.hasNext()) {
                                        SelectionKey clientSelectionKey = clientSelectionKeys.next();

                                        if (clientSelectionKey.isReadable()) {
                                            Message message = readData(client);
                                            if (message != null) {
                                                switch (message.messageType) {
                                                    case LOAD:
                                                        load(collection, client);
                                                        break;
                                                    case SHOW:
                                                        show(collection, client);
                                                        break;
                                                    case CLEAR:
                                                        clear(collection, client);
                                                        break;
                                                    case REMOVE:
                                                        remove(collection,client,message);
                                                        break;
                                                    case REMOVE_ALL:
                                                        removeAll(collection,client,message);
                                                        break;
                                                    case CLOSE:
                                                        return;
                                                    default:
                                                        break;
                                                }
                                            }
                                        }

                                        clientSelectionKeys.remove();
                                    }
                                }
                            } catch (IOException | InterruptedException e) {
                                print(Thread.currentThread().getName(), e.getMessage());
                            } finally {
                                try {
                                    collection.save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                print(Thread.currentThread().getName(), "ended");
                            }
                        });
                        clientHandler.start();
                    }

                    selectedKeys.remove();
                }
            }
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        }
    }

    public static void writeData(SocketChannel socket, Message message) throws IOException {
        String payload = mapper.writeValueAsString(message);
        print(Thread.currentThread().getName(), format("send data - %s", payload));
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer buffer = ByteBuffer.wrap(charset.encode(payload).array());
        socket.write(buffer);
    }

    public static Message readData(SocketChannel socketChannel) throws IOException, InterruptedException {
        String payload = null;
        ByteBuffer buffer = ByteBuffer.allocate(10);
        Charset charset = Charset.forName("UTF-8");
        StringBuilder builder = new StringBuilder();
        while (socketChannel.read(buffer) > 0) {
            buffer.rewind();
            builder.append(charset.decode(buffer));
            buffer.flip();
        }
        payload = builder.toString();
        print(Thread.currentThread().getName(), format("read data - %s", payload));
        return mapper.readValue(payload, Message.class);

    }

    public SocketChannel getClient() {
        return client;
    }

    public Collection<Cart> getCollection() {
        return collection;
    }

    public Message getMessage(){ return message; }

    public static void load(Collection collection, SocketChannel client) {
        try {
            collection.load();
            writeData(client, new Message(SHOW_RESPONCE, collection.getStore()));
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        }
    }

    public static void show(Collection collection, SocketChannel client) {
        try {
            writeData(client, new Message(SHOW_RESPONCE, collection.getStore()));
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        }
    }

    public static void clear(Collection collection, SocketChannel client) {
        try {
            collection.clear();
            writeData(client, new Message(SHOW_RESPONCE, collection.getStore()));
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        }
    }

    public static void remove(Collection collection, SocketChannel client, Message message) {
        try {
            collection.remove((String) message.payload);
            writeData(client, new Message(SHOW_RESPONCE, collection.getStore()));
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        }
    }

    public static void removeAll(Collection collection, SocketChannel client, Message message) {
        try {
            collection.removeAll(mapper.readValue(mapper.writeValueAsString(message.payload), Cart.class));
            writeData(client, new Message(SHOW_RESPONCE, collection.getStore()));
        } catch (IOException e) {
            print(Thread.currentThread().getName(), e.getMessage());
        }
    }
}
