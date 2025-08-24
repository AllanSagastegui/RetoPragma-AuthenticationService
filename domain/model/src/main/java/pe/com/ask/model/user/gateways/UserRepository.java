package pe.com.ask.model.user.gateways;
import pe.com.ask.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> insert(User user);
    Mono<Boolean> existsByEmail(String email);
}