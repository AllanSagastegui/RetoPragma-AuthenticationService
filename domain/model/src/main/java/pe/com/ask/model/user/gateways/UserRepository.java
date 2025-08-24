package pe.com.ask.model.user.gateways;
import pe.com.ask.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> signUp(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<User> findByEmail(String email);
}