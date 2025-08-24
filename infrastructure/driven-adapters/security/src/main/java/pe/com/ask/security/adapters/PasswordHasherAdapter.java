package pe.com.ask.security.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.ask.model.user.gateways.PasswordHasher;

@Component
@RequiredArgsConstructor
public class PasswordHasherAdapter implements PasswordHasher {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }
}