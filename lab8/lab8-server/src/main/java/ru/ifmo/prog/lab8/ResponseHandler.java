package ru.ifmo.prog.lab8;

import java.nio.channels.SocketChannel;

public interface ResponseHandler {
    void handle(Message message, SocketChannel socket);
}
