package pe.com.ask.api.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.com.ask.api.doc.exception.UnauthorizedExceptionDoc;
import pe.com.ask.api.doc.exception.UnexpectedExceptionDoc;
import pe.com.ask.api.dto.response.GetAllClientsResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@Schema(description = "Documentation for get all clients endpoint")
public class GetAllClientsDoc {

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all clients by IDs",
            description = "Retrieves a list of clients given their UUIDs. Requires Bearer token authentication.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "List of clients successfully retrieved",
                    content = @Content(
                            schema = @Schema(implementation = GetAllClientsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: missing or invalid JWT token",
                    content = @Content(
                            schema = @Schema(implementation = UnauthorizedExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = UnexpectedExceptionDoc.class))
            )
    })
    public Mono<List<GetAllClientsResponse>> getAllClientsDoc(
            @RequestBody(description = "List of user UUIDs to retrieve")
            @org.springframework.web.bind.annotation.RequestBody List<UUID> ids
    ) {
        return Mono.empty();
    }
}