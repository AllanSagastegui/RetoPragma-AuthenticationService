package pe.com.ask.r2dbc;

import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.r2dbc.entity.UserEntity;
import pe.com.ask.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
        UUID,
        UserReactiveRepository
> implements UserRepository {

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return super.repository.findByEmail(email)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findByDni(String dni) {
        return super.repository.findByDni(dni)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Flux<User> getUsersByIds(List<UUID> userIds) {
        return super.repository.findAllById(userIds)
                .map(this::toEntity);
    }


    @Override
    public Mono<User> signUp(User user){
        return super.save(user);
    }
}