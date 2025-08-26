package pe.com.ask.api.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.model.token.Token;

import static org.junit.jupiter.api.Assertions.*;

class TokenMapperTest {

    private final TokenMapper mapper = Mappers.getMapper(TokenMapper.class);

    @Test
    void testToResponse() {
        Token token = new Token();
        token.setToken("jwt-token-123");

        SignInResponse response = mapper.toResponse(token);

        assertNotNull(response);
        assertEquals(token.getToken(), response.token());
    }

    @Test
    void testNullHandling() {
        assertNull(mapper.toResponse(null));
    }
}
