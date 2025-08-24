package pe.com.ask.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Date birthday;
    private String address;
    private String phone;
    private BigDecimal baseSalary;
    private UUID idRole;
}