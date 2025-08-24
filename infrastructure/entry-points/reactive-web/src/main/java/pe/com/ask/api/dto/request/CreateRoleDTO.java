package pe.com.ask.api.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateRoleDTO(

        UUID id,

        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Description cannot be blank")
        String description
) { }