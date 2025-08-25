package pe.com.ask.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResponseRegister(
        String name,
        String lastName,
        String email,
        LocalDate birthday,
        String address,
        String phone,
        BigDecimal baseSalary
) {}