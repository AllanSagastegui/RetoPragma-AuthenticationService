package pe.com.ask.api.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public record ResponseRegister(
        String name,
        String lastName,
        String email,
        Date birthday,
        String address,
        String phone,
        BigDecimal baseSalary
) {}