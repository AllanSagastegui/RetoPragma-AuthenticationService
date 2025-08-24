package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.CreateRoleDTO;
import pe.com.ask.api.dto.response.ResponseCreateRol;
import pe.com.ask.model.role.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    CreateRoleDTO toDTO(Role role);
    Role toEntity(CreateRoleDTO dto);

    default ResponseCreateRol toResponse(Role role) {
        if (role == null) return null;
        return new ResponseCreateRol(
                role.getName(),
                role.getDescription()
        );
    }
}