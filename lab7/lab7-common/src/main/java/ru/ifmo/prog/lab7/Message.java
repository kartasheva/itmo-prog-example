package ru.ifmo.prog.lab7;

import java.util.Objects;

public class Message {
    MessageType messageType;
    Object payload;

    public Message(MessageType messageType, Object payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public Message() {
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return messageType == message.messageType &&
                Objects.equals(payload, message.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, payload);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", payload=" + payload +
                '}';
    }
}
