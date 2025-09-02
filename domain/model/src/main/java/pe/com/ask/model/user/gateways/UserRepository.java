package pe.com.ask.model.user.gateways;
import pe.com.ask.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    Mono<User> signUp(User user);
    Mono<User> findByEmail(String email);
    Mono<User> findByDni(String dni);
    Flux<User> getUsersByIds(List<UUID> userIds);
}