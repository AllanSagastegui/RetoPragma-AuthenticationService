package pe.com.ask.api.mapper;

import org.mapstruct.Mapper;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.model.token.Token;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    SignInResponse toResponse(Token token);
}