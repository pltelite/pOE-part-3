import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

public class Message {
    private static int messageCounter = 0;
    private static List<Message> sentMessages = new ArrayList<>();

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    public Message(String recipient, String message) {
        this.messageID = generateMessageID();
        this.messageNumber = ++messageCounter;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long num = (long)(rand.nextDouble() * 1_000_000_0000L); // 10 digits
        return String.format("%010d", num);
    }

    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    public boolean checkRecipientCell() {
        return recipient.matches("^\\+27\\d{9}$");
    }

    public boolean isValidMessageLength() {
        return message.length() <= 250;
    }

    public String createMessageHash() {
        String[] words = message.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return String.format("%s:%d:%s%s",
                messageID.substring(0, 2),
                messageNumber,
                firstWord.toUpperCase(),
                lastWord.toUpperCase());
    }

    public String sendMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete message.";
            case 3:
                // message can be stored elsewhere
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public static void printMessages() {
        for (Message msg : sentMessages) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Message ID: " + msg.messageID +
                "\nMessage Hash: " + msg.messageHash +
                "\nRecipient: " + msg.recipient +
                "\nMessage: " + msg.message);
        }
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    public static List<Message> getSentMessages() {
        return sentMessages;
    }

    // (Optional) Store to JSON method will go here later.
}

    

