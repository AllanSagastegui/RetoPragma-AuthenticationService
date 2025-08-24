package pe.com.ask.usecase.createrole;
import lombok.RequiredArgsConstructor;
import pe.com.ask.model.role.Role;
import pe.com.ask.model.role.gateways.RoleRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateRoleUseCase {
    private final RoleRepository roleRepository;

    public Mono<Role> saveRole(Role role) {
        return roleRepository.save(role);
    }
}