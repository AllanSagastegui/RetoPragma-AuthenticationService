package pe.com.ask.usecase.signup;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.RoleNotFoundException;
import pe.com.ask.usecase.exception.UserAlreadyExistsException;
import pe.com.ask.usecase.utils.DEFAULT_ROLE;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;
    private final TransactionalGateway transactionalGateway;
    private final CustomLogger customLogger;

    public Mono<User> signUpUser(User user) {
        customLogger.trace("Start sign-up User flow for email: {}, dni: {}", user.getEmail(), user.getDni());
        Mono<Void> checkExistingUser = userRepository.findByEmail(user.getEmail())
                .hasElement()
                .flatMap(emailExists -> {
                    boolean b = emailExists;
                    if (b) {
                        customLogger.trace("User already exists with email: {}", user.getEmail());
                        return Mono.error(new UserAlreadyExistsException());
                    }
                    return userRepository.findByDni(user.getDni())
                            .doOnNext(existingUser -> {
                                if (existingUser != null) {
                                    customLogger.trace("User already exists with DNI: {}", user.getDni());
                                }
                            })
                            .flatMap(existingUser -> {
                                if (existingUser != null) {
                                    return Mono.error(new UserAlreadyExistsException());
                                }
                                return Mono.just("Continue");
                            })
                            .then();
                });
        return transactionalGateway.executeInTransaction(
                        checkExistingUser
                                .then(roleRepository.findByName(DEFAULT_ROLE.CLIENT.toString())
                                        .doOnNext(role -> customLogger.trace("Role CLIENT found with id: {}", role.getId()))
                                        .switchIfEmpty(Mono.defer(() -> {
                                            customLogger.trace("Role CLIENT not found");
                                            return Mono.error(new RoleNotFoundException());
                                        }))
                                        .flatMap(role -> {
                                            user.setPassword(passwordHasher.hash(user.getPassword()));
                                            customLogger.trace("Password hashed for user: {}", user.getEmail());
                                            user.setIdRole(role.getId());
                                            return userRepository.signUp(user)
                                                    .doOnSuccess(u -> customLogger.trace("User signed up successfully: {}", u.getEmail()));
                                        })
                                )
                )
                .doOnError(err -> customLogger.trace("Sign-up error for {}: {}", user.getEmail(), err.getMessage()));
    }
}
