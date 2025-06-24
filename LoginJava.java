import java.util.regex.*;

public class Login {
    private String storedUsername;
    private String storedPassword;
    private String storedCellNumber;
    private String firstName;
    private String lastName;

    // Constructor
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean length = password.length() >= 8;
        boolean capital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean number = Pattern.compile("[0-9]").matcher(password).find();
        boolean specialChar = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
        return length && capital && number && specialChar;
    }

    public boolean checkCellPhoneNumber(String cellNumber) {
        // Matches e.g., +27831234567 (international code +27 + 9 digits = 12 characters)
        return cellNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellNumber = cellNumber;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public boolean loginUser(String username, String password) {
        return storedUsername != null && storedPassword != null &&
               storedUsername.equals(username) && storedPassword.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
//end of part 1
