package pe.com.ask.api.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity() {
        SignUpDTO dto = new SignUpDTO(
                "Allan",
                "Sagastegui",
                "12345678",
                "allan@example.com",
                "password123",
                "1995-08-25",
                "Av. Siempre Viva 123",
                "987654321",
                new BigDecimal("2500.0")
        );

        User user = mapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.name(), user.getName());
        assertEquals(dto.lastName(), user.getLastName());
        assertEquals(dto.dni(), user.getDni());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.password(), user.getPassword());
        assertEquals(LocalDate.parse(dto.birthday()), user.getBirthday());
        assertEquals(dto.address(), user.getAddress());
        assertEquals(dto.phone(), user.getPhone());
        assertEquals(dto.baseSalary(), user.getBaseSalary());
    }

    @Test
    void testToResponse() {
        User user = User.builder()
                .name("Allan")
                .lastName("Sagastegui")
                .dni("12345678")
                .email("allan@example.com")
                .password("password123")
                .birthday(LocalDate.of(1995, 8, 25))
                .address("Av. Siempre Viva 123")
                .phone("987654321")
                .baseSalary(new BigDecimal("2500.0"))
                .build();

        SignUpResponse response = mapper.toResponse(user);

        assertNotNull(response);
        assertEquals(user.getName(), response.name());
        assertEquals(user.getLastName(), response.lastName());
        assertEquals(user.getDni(), response.dni());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getBirthday().toString(), response.birthday());
        assertEquals(user.getAddress(), response.address());
        assertEquals(user.getPhone(), response.phone());
        assertEquals(user.getBaseSalary(), response.baseSalary());
    }

    @Test
    void testNullHandling() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toResponse(null));
    }
}
