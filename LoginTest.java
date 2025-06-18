import javax.swing.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

public class Message {

    private String messageID;
    private static int messageCount = 0;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    public static List<Message> allMessages = new ArrayList<>();
    public static int totalMessages = 0;

    public Message(String recipient, String message) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = ++messageCount;
        this.messageHash = createMessageHash();
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        return recipient.startsWith("+27") && recipient.length() <= 13;
    }

    public boolean isValidMessageLength() {
        return message.length() <= 250;
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    public String createMessageHash() {
        String[] words = message.split(" ");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : "";
        String hashPrefix = messageID.substring(0, 2) + ":" + messageNumber;
        return hashPrefix + ":" + firstWord + lastWord;
    }

    public String sendMessage(int option) {
        switch (option) {
            case 1:
                allMessages.add(this);
                totalMessages++;
                showMessageDetails();
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete message.";
            case 3:
                storeMessageToJson();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public void showMessageDetails() {
        JOptionPane.showMessageDialog(null,
            "Message ID: " + messageID + "\n" +
            "Message Hash: " + messageHash + "\n" +
            "Recipient: " + recipient + "\n" +
            "Message: " + message);
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public void storeMessageToJson() {
        JSONObject json = new JSONObject();
        json.put("messageID", this.messageID);
        json.put("messageHash", this.messageHash);
        json.put("recipient", this.recipient);
        json.put("message", this.message);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json.toJSONString() + "\n");
            System.out.println("Message stored in JSON file.");
        } catch (IOException e) {
            System.out.println("An error occurred while storing the message.");
        }
    }

    public static List<Message> getAllMessages() {
        return allMessages;
    }
}
