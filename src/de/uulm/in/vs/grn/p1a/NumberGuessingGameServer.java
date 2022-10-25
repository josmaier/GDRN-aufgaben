
/*
package auskommentiert, damit über CMD ausfuehrbar. Um in IDE auszuführen
entsprechen package line Kommentar entfernen.
*/

// package de.uulm.in.vs.grn.p1a;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGuessingGameServer {

    static final int upperBound = 50;
    static int tries = 6;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            Socket connectionSocket = serverSocket.accept();

            InputStream is = connectionSocket.getInputStream();
            OutputStream os = connectionSocket.getOutputStream();

            String introText = "Welcome to Luisa's secret NumberGuessing-Mansion!\n" +
                    "Enter the secret number and you shall tread into the hidden treasure room.\n" +
                    "I'll grant you 6 turns to guess my secret number between 0 and 50.\n";

            os.write(introText.getBytes());
            os.flush();

            int number = ThreadLocalRandom.current().nextInt(upperBound + 1);

            String output;
            String s;
            int guessedNumber;
            while (tries > 0) {

                s = "";
                while ((guessedNumber = is.read()) != (int) '\n') {
                    s += (char) guessedNumber;
                }

                guessedNumber = Integer.parseInt(s);

                if (guessedNumber > 50 || guessedNumber < 0) {
                    output = "Your number is too small/big!\n";
                    os.write(output.getBytes());
                    os.flush();
                    continue;
                }
                tries--;
                if (guessedNumber > number) {
                    if (tries == 0) {
                        output = "Sorry, you've lost!";
                        os.write(output.getBytes());
                        os.flush();
                        break;
                    } else {
                        output = "Your guess is too high. Remaining tries: " + tries + "\n";
                    }
                } else if (guessedNumber < number) {
                    if (tries == 0) {
                        output = "Sorry, you've lost!";
                        os.write(output.getBytes());
                        os.flush();
                        break;
                    } else {
                        output = "Your guess is too low. Remaining tries: " + tries + "\n";
                    }
                } else {
                    output = "Oh no, you've won!";
                    os.write(output.getBytes());
                    os.flush();
                    break;
                }

                os.write(output.getBytes());
                os.flush();

            }

            connectionSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

