package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.GetAllClientsResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.model.user.User;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default User toEntity(SignUpDTO dto){
        if(dto == null) return null;
        return User.builder()
                .name(dto.name())
                .lastName(dto.lastName())
                .dni(dto.dni())
                .email(dto.email())
                .password(dto.password())
                .birthday(LocalDate.parse(dto.birthday()))
                .address(dto.address())
                .phone(dto.phone())
                .baseSalary(dto.baseSalary())
                .build();
    }

    default SignUpResponse toResponse(User user) {
        if (user == null) return null;
        return new SignUpResponse(
                user.getName(),
                user.getLastName(),
                user.getDni(),
                user.getEmail(),
                user.getBirthday().toString(),
                user.getAddress(),
                user.getPhone(),
                user.getBaseSalary()
        );
    }

    default GetAllClientsResponse toGetAllClientsResponse(User user) {
        if (user == null) return null;
        return new GetAllClientsResponse(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getDni(),
                user.getEmail(),
                user.getBaseSalary()
        );
    }
}