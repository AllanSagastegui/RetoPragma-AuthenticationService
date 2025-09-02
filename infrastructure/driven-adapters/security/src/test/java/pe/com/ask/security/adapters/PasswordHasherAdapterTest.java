package pe.com.ask.security.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordHasherAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private PasswordHasherAdapter passwordHasherAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordHasherAdapter = new PasswordHasherAdapter(passwordEncoder);
    }

    @Test
    @DisplayName("Should hash password correctly")
    void testHash() {
        String rawPassword = "myPassword123";
        String hashedPassword = "hashedPassword";

        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);

        String result = passwordHasherAdapter.hash(rawPassword);

        assertEquals(hashedPassword, result);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    @DisplayName("Should match password correctly when valid")
    void testMatchesSuccess() {
        String rawPassword = "myPassword123";
        String hashedPassword = "hashedPassword";

        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        boolean result = passwordHasherAdapter.matches(rawPassword, hashedPassword);

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }

    @Test
    @DisplayName("Should not match password when invalid")
    void testMatchesFail() {
        String rawPassword = "myPassword123";
        String hashedPassword = "hashedPassword";

        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        boolean result = passwordHasherAdapter.matches(rawPassword, hashedPassword);

        assertFalse(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, hashedPassword);
    }
}