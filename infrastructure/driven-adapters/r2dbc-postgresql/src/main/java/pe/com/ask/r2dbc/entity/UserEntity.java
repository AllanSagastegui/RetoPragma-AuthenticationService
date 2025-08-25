package pe.com.ask.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("users")
public class UserEntity {
    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("birthday")
    private LocalDate birthday;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("id_role")
    private UUID idRole;
}