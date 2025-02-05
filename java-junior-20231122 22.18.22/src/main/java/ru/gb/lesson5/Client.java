package ru.gb.lesson5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        final Socket client = new Socket("localhost", Server.PORT);

        // Чтение сообщений от сервера
        new Thread(() -> {
            try (Scanner input = new Scanner(client.getInputStream())) {
                while (input.hasNextLine()) {
                    System.out.println(input.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Соединение с сервером потеряно.");
            }
        }).start();

        // Отправка сообщений на сервер
        try (PrintWriter output = new PrintWriter(client.getOutputStream(), true);
             Scanner consoleScanner = new Scanner(System.in)) {

            while (true) {
                String consoleInput = consoleScanner.nextLine();
                output.println(consoleInput);

                if (Objects.equals("q", consoleInput)) {
                    System.out.println("Отключение от сервера...");
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при отправке сообщения.");
        }
    }
}
