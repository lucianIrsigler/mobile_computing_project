import java.util.regex.Pattern;

/**
 * A class to validate text using regex
 * */
public class Validation {
    //we use static to ensure the program only creates one of each regex,rather than
    //creating new regex objects each time
    //we use final to ensure these regex patterns can not be changed
    private static final String NAME_REGEX = "^[A-Z][A-Za-z\\s'-]*$";
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public Validation(){}

    /**
     * This method validates text based on a validation rule
     *
     * @param text Text to validate.
     * @param validationRule regex pattern to use.
     * @return Returns if the pattern can match the text to the regex.
     */
    public boolean validateText(String text, String validationRule) {
        return Pattern.matches(validationRule,text);
    }

    //The following 3 functions just allows the code to access the regex
    // and just the regex, thus ensuring only a read operation is done
    public static String getNameValidationRule() {
        return NAME_REGEX;
    }

    public static String getPasswordValidationRule() {
        return PASSWORD_REGEX;
    }

    public static String getEmailValidationRule() {
        return EMAIL_REGEX;
    }
}

/*
An example on how to call the method:
String text = "<something goes here>";
Validation validation = new Validation();
boolean b = validation.validateText(text, Validation.getNameValidationRule());
*/
