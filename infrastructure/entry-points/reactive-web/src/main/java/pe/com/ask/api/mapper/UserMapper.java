package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    SignUpDTO toDTO(User user);
    User toEntity(SignUpDTO dto);
    default SignUpResponse toResponse(User user) {
        if (user == null) return null;
        return new SignUpResponse(
                user.getName(),
                user.getLastName(),
                user.getDni(),
                user.getEmail(),
                user.getBirthday(),
                user.getAddress(),
                user.getPhone(),
                user.getBaseSalary()
        );
    }
}