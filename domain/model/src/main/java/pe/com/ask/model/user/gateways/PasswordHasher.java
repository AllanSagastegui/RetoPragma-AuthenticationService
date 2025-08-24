package pe.com.ask.model.user.gateways;

public interface PasswordHasher {
    String hash(String password);
}