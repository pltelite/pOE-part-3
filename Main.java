import java.util.*;
import java.io.*;
import com.google.gson.*;

public class MessageApp {
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Message> sentMessages = new ArrayList<>();
    static ArrayList<Message> disregardedMessages = new ArrayList<>();
    static ArrayList<Message> storedMessages = new ArrayList<>();
    static ArrayList<String> messageHashes = new ArrayList<>();
    static ArrayList<String> messageIDs = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);
    static User currentUser = null;

    public static void main(String[] args) {
        loadStoredMessages("stored_messages.json");

        System.out.println("==== WELCOME TO MESSAGE APP ====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> registerUser();
                case "2" -> {
                    if (loginUser()) {
                        showMainMenu(); // Once logged in, show full app menu
                    }
                }
                case "3" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter phone (+27 or 0 format): ");
        String phone = scanner.nextLine();

        if (!User.isValidPhone(phone)) {
            System.out.println("Invalid phone number.");
            return;
        }

        users.add(new User(username, password, phone));
        System.out.println("✅ Registered successfully.");
    }

    static boolean loginUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                System.out.println("✅ Login successful. Welcome, " + currentUser.getUsername());
                return true;
            }
        }

        System.out.println("❌ Login failed.");
        return false;
    }

    static void showMainMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n==== MAIN MENU ====");
            System.out.println("1. Send Message");
            System.out.println("2. View Sent Messages");
            System.out.println("3. Display Longest Sent Message");
            System.out.println("4. Search by Message ID");
            System.out.println("5. Find Messages by Recipient");
            System.out.println("6. Delete by Message Hash");
            System.out.println("7. Display Report");
            System.out.println("8. Logout");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> sendMessage();
                case "2" -> viewSentMessages();
                case "3" -> displayLongestMessage();
                case "4" -> {
                    System.out.print("Enter message ID: ");
                    searchByID(scanner.nextLine());
                }
                case "5" -> {
                    System.out.print("Enter recipient: ");
                    searchByRecipient(scanner.nextLine());
                }
                case "6" -> {
                    System.out.print("Enter hash: ");
                    deleteByHash(scanner.nextLine());
                }
                case "7" -> displayReport();
                case "8" -> {
                    loggedIn = false;
                    currentUser = null;
                    System.out.println("Logged out.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void sendMessage() {
        System.out.print("Recipient number: ");
        String recipient = scanner.nextLine();

        System.out.print("Message content: ");
        String text = scanner.nextLine();

        System.out.print("Flag (Sent/Stored/Disregard): ");
        String flag = scanner.nextLine();

        Message m = new Message(recipient, text, flag);
        switch (flag.toLowerCase()) {
            case "sent" -> sentMessages.add(m);
            case "stored" -> storedMessages.add(m);
            case "disregard" -> disregardedMessages.add(m);
            default -> System.out.println("Invalid flag. Use Sent/Stored/Disregard.");
        }

        messageHashes.add(m.getHash());
        messageIDs.add(m.getId());

        System.out.println("✅ Message processed.");
    }

    static void viewSentMessages() {
        for (Message m : sentMessages) {
            System.out.println("Sender: " + currentUser.getUsername() + " -> Recipient: " + m.getRecipient());
        }
    }

    static void displayLongestMessage() {
        Message longest = sentMessages.stream()
                .max(Comparator.comparingInt(m -> m.getMessage().length()))
                .orElse(null);
        if (longest != null) {
            System.out.println("Longest Message:\n" + longest.getMessage());
        } else {
            System.out.println("No sent messages yet.");
        }
    }

    static void searchByID(String id) {
        for (Message m : sentMessages) {
            if (m.getId().equals(id)) {
                System.out.println("Recipient: " + m.getRecipient());
                System.out.println("Message: " + m.getMessage());
                return;
            }
        }
        System.out.println("Message not found.");
    }

    static void searchByRecipient(String recipient) {
        boolean found = false;
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                System.out.println(m);
                found = true;
            }
        }
        if (!found) System.out.println("No messages found.");
    }

    static void deleteByHash(String hash) {
        Iterator<Message> it = sentMessages.iterator();
        while (it.hasNext()) {
            Message m = it.next();
            if (m.getHash().equals(hash)) {
                it.remove();
                System.out.println("✅ Message deleted: " + m.getMessage());
                return;
            }
        }
        System.out.println("Message not found.");
    }

    static void displayReport() {
        System.out.println("\n==== SENT MESSAGES REPORT ====");
        for (Message m : sentMessages) {
            System.out.println("Hash: " + m.getHash());
            System.out.println("Recipient: " + m.getRecipient());
            System.out.println("Message: " + m.getMessage());
            System.out.println("--------");
        }
    }

    static void loadStoredMessages(String filename) {
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader(filename);
            Message[] stored = gson.fromJson(reader, Message[].class);
            storedMessages.addAll(Arrays.asList(stored));
            reader.close();
        } catch (Exception e) {
            System.out.println("No stored messages found.");
        }
    }
}