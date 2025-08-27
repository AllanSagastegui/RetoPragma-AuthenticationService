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
import pe.com.ask.usecase.utils.logmessages.SignUpUseCaseLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;
    private final TransactionalGateway transactionalGateway;
    private final CustomLogger customLogger;

    public Mono<User> signUpUser(User user) {
        customLogger.trace(SignUpUseCaseLog.START_FLOW, user.getEmail(), user.getDni());

        Mono<Void> checkExistingUser = userRepository.findByEmail(user.getEmail())
                .hasElement()
                .flatMap(emailExists -> {
                    boolean b = emailExists;
                    if (b) {
                        customLogger.trace(SignUpUseCaseLog.USER_ALREADY_EXISTS_EMAIL, user.getEmail());
                        return Mono.error(new UserAlreadyExistsException());
                    }
                    return userRepository.findByDni(user.getDni())
                            .doOnNext(existingUser -> {
                                if (existingUser != null) {
                                    customLogger.trace(SignUpUseCaseLog.USER_ALREADY_EXISTS_DNI, user.getDni());
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
                                        .doOnNext(role -> customLogger.trace(SignUpUseCaseLog.ROLE_FOUND, role.getId()))
                                        .switchIfEmpty(Mono.defer(() -> {
                                            customLogger.trace(SignUpUseCaseLog.ROLE_NOT_FOUND);
                                            return Mono.error(new RoleNotFoundException());
                                        }))
                                        .flatMap(role -> {
                                            user.setPassword(passwordHasher.hash(user.getPassword()));
                                            customLogger.trace(SignUpUseCaseLog.PASSWORD_HASHED, user.getEmail());
                                            user.setIdRole(role.getId());
                                            return userRepository.signUp(user)
                                                    .doOnSuccess(u -> customLogger.trace(SignUpUseCaseLog.USER_SIGNED_UP, u.getEmail()));
                                        })
                                )
                )
                .doOnError(err -> customLogger.trace(SignUpUseCaseLog.SIGNUP_ERROR, user.getEmail(), err.getMessage()));
    }
}