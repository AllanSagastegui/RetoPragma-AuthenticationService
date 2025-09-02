package pe.com.ask.usecase.getusersbyid;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllClientsLog;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetUsersByIdUseCase {

    private final UserRepository userRepository;
    private final CustomLogger logger;

    public Flux<User> getUsersByIds(List<UUID> userIds) {
        logger.trace(GetAllClientsLog.START_FLOW);

        return userRepository.getUsersByIds(userIds)
                .doOnNext(user -> logger.trace(GetAllClientsLog.CLIENT_FOUND, user.getEmail())
                )
                .doOnComplete(() -> logger.trace(GetAllClientsLog.END_FLOW))
                .doOnError(err -> logger.trace(GetAllClientsLog.ERROR, err.getMessage()));
    }
}