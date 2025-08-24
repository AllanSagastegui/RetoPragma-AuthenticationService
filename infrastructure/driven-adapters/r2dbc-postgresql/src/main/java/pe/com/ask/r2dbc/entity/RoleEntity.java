package pe.com.ask.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("roles")
public class RoleEntity {
    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}