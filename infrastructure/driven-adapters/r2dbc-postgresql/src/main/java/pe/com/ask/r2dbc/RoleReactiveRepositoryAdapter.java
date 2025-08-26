package pe.com.ask.r2dbc;

import pe.com.ask.model.role.Role;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.r2dbc.entity.RoleEntity;
import pe.com.ask.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role/* change for domain model */,
        RoleEntity/* change for adapter model */,
        UUID,
        RoleReactiveRepository
> implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Role.class/* change for domain model */));
    }

    @Override
    public Mono<Role> findByName(String name) {
        return super.repository.findByName(name)
                .map(entity -> mapper.map(entity, Role.class));
    }

    @Override
    public Mono<Role> findById(UUID id){
        return super.findById(id);
    }
}
