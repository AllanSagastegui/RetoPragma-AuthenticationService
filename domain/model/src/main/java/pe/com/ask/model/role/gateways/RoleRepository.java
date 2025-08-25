package pe.com.ask.model.role.gateways;
import pe.com.ask.model.role.Role;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoleRepository {
    Mono<Role> findByName(String name);
    Mono<Role> findById(UUID id);
}