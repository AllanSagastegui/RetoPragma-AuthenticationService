package pe.com.ask.usecase.registeruser;
import lombok.RequiredArgsConstructor;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.RoleNotFoundException;
import pe.com.ask.usecase.exception.UserAlreadyExistsException;
import pe.com.ask.model.gateways.TransactionalGateway;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;
    private final TransactionalGateway  transactionalGateway;

    public Mono<User> saveUser(User user){
        return transactionalGateway.executeInTransaction(
                userRepository.existsByEmail(user.getEmail())
                        .flatMap(exists -> exists
                            ? Mono.error(new UserAlreadyExistsException(user.getEmail()))
                                : roleRepository.findById(user.getIdRole())
                                .switchIfEmpty(Mono.error(new RoleNotFoundException()))
                                .flatMap(role -> {
                                    user.setPassword(passwordHasher.hash(user.getPassword()));
                                    user.setIdRole(role.getId());
                                    return userRepository.insert(user);
                                })
                        )
        );
    }
}