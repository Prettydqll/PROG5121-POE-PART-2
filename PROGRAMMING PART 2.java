import java.util.Scanner;
import java.util.Random;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Arrays to store messages
        long[] checkMessageIDs = new long[10];
        String[] checkRecipientsCell = new String[10];
        String[] messages = new String[10];
        String[] createMessageHashes = new String[10];
        int messageCount = 0;

        System.out.println("Welcome to QuickChat");

        int choice = 0;
        while (choice!= 3) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                if (messageCount >= 10) {
                    System.out.println("Message limit reached.");
                    continue;
                }

                // Get recipient
                System.out.print("Enter recipient number: ");
                String recipient = scanner.nextLine();
                if (recipient.length() > 10) {
                    System.out.println("Recipient number too long. Max 10 characters.");
                    continue;
                }

                // Get message
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                if (message.length() > 250) {
                    System.out.println("Please enter a message of less than 250 characters.");
                    continue;
                }

                // Generate ID and Hash
                long messageID = 1000000L + random.nextInt(900000);
                String hash = generateHash(messageID, message);

                // Ask what to do with the message
                System.out.println("\nChoose an option:");
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Option: ");
                int action = scanner.nextInt();
                scanner.nextLine();

                if (action == 1) {
                    // Send Message
                    checkMessageIDs[messageCount] = messageID;
                    checkRecipientsCell[messageCount] = recipient;
                    messages[messageCount] = message;
                    createMessageHashes[messageCount] = hash;
                    messageCount++;

                    System.out.println("Message successfully sent");

                    // Show full details as per requirement 7
                    System.out.println("\n--- Message Details ---");
                    System.out.println("Message ID: " + checkMessageIDs);
                    System.out.println("Message Hash: " + createMessageHashes);
                    System.out.println("Recipient: " + checkRecipientsCell);
                    System.out.println("Message: " + message);

                } else if (action == 2) {
                    // Disregard Message
                    System.out.println("Press 0 to delete the message");
                    int del = scanner.nextInt();
                    scanner.nextLine();
                    if (del == 0) {
                        System.out.println("Message deleted.");
                    }

                } else if (action == 3) {
                    // Store Message to send later
                    saveToJSON(messageID, recipient, message, hash);
                    System.out.println("Message successfully stored");
                }

            } else if (choice == 2) {
                // Show sent messages
                if (messageCount == 0) {
                    System.out.println("No messages sent yet.");
                } else {
                    System.out.println("\n--- Sent Messages ---");
                    for (int i = 0; i < messageCount; i++) {
                        System.out.println("Message ID: " + checkMessageIDs[i]);
                        System.out.println("Message Hash: " + createMessageHashes[i]);
                        System.out.println("Recipient: " + checkRecipientsCell[i]);
                        System.out.println("Message: " + messages[i]);
                        System.out.println("--------------------");
                    }
                }

            } else if (choice == 3) {
                // Requirement 6: Show total number of messages
                System.out.println("Total messages sent: " + messageCount);
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    // Generate hash: first 2 digits of ID + first 2 letters of message
    public static String generateHash(long id, String message) {
        String idPart = String.valueOf(id).substring(0, 2);
        String msgPart = "XX";
        if (message.length() >= 2) {
            msgPart = message.substring(0, 2).toUpperCase();
        } else if (message.length() == 1) {
            msgPart = message.substring(0, 1).toUpperCase() + "X";
        }
        return idPart + ":" + msgPart;
    }

    // Simple JSON save to file
    public static void saveToJSON(long id, String recipient, String message, String hash) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("messages.json", true));
            writer.println("{");
            writer.println(" \"messageID\": \"" + id + "\",");
            writer.println(" \"hash\": \"" + hash + "\",");
            writer.println(" \"recipient\": \"" + recipient + "\",");
            writer.println(" \"message\": \"" + message + "\"");
            writer.println("}");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }
}