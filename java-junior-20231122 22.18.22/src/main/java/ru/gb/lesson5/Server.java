package ru.gb.lesson5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Server {

    // message broker (kafka, redis, rabbitmq, ...)
    // client sent letter to broker

    // server sent to SMTP-server

    public static final int PORT = 8181;

    private static long clientIdCounter = 1L;
    private static Map<String, SocketWrapper> clients = new HashMap<>();
    private static final char marker = '@';

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                final Socket client = server.accept();
                final long clientId = clientIdCounter++;

                SocketWrapper wrapper = new SocketWrapper(clientId, client);
                System.out.println("Подключился новый клиент[" + wrapper + "]");
                clients.put(marker + String.valueOf(clientId), wrapper);

                new Thread(() -> handleClient(wrapper)).start();
            }
        }
    }

    private static void handleClient(SocketWrapper wrapper) {
        try (Scanner input = wrapper.getInput(); PrintWriter output = wrapper.getOutput()) {
            output.println("Подключение успешно. Список всех клиентов: " + clients.keySet());

            while (true) {
                String clientInput = input.nextLine();
                if (Objects.equals("q", clientInput)) {
                    clients.remove(String.valueOf(marker + wrapper.getId()));
                    broadcast("Клиент[" + wrapper.getId() + "] отключился", wrapper.getId());
                    break;
                }

                if (clientInput.startsWith("@")) {
                    int spaceIndex = clientInput.indexOf(" ");
                    if (spaceIndex > 1) {
                        String clientKey = clientInput.substring(0, spaceIndex);
                        SocketWrapper destination = clients.get(clientKey);
                        if (destination != null) {
                            destination.getOutput().println("Лично от " + wrapper.getId() + ": " + clientInput.substring(spaceIndex + 1));
                        } else {
                            output.println("Клиент " + clientKey + " не найден");
                        }
                    }
                } else {
                    broadcast("Клиент[" + wrapper.getId() + "]: " + clientInput, wrapper.getId());
                }
            }
        }
    }

    private static void broadcast(String message, long senderId) {
        clients.values().forEach(client -> {
            if (client.getId() != senderId) {
                client.getOutput().println(message);
            }
        });
    }
}
