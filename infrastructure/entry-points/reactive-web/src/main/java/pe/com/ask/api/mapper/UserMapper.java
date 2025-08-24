package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.ResponseRegister;
import pe.com.ask.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    SignUpDTO toDTO(User user);
    User toEntity(SignUpDTO dto);
    default ResponseRegister toResponse(User user) {
        if (user == null) return null;
        return new ResponseRegister(
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthday(),
                user.getAddress(),
                user.getPhone(),
                user.getBaseSalary()
        );
    }
}