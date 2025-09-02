package pe.com.ask.api.utils.routes;

public final class Routes {

    private Routes() {}

    public static final String SIGNUP = "/api/v1/usuarios";
    public static final String SIGNIN = "/api/v1/login";
    public static final String GETUSERSBYID = "/api/v1/usuarios/by-ids";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_DOCS = "/swagger-doc/**";
    public static final String SWAGGER_API_DOC = "/v3/api-docs/**";
    public static final String WEBJARS = "/webjars/**";
    public static final String TEST = "/test";
    public static final String ACTUATOR = "/actuator/**";
}