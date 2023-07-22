package exception;

public class ConfigurationFileContentNotValid extends RuntimeException {
    public ConfigurationFileContentNotValid(String message) {
        super(message);
    }
}