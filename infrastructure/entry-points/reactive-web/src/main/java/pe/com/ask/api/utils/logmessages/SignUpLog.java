package pe.com.ask.api.utils.logmessages;

public final class SignUpLog {

    private SignUpLog() {}

    public static final String SIGNUP_START = "[UserHandler - SignUp] Starting sign-up request processing...";
    public static final String SIGNUP_RECEIVED_BODY = "[UserHandler - SignUp] Received request body";
    public static final String SIGNUP_VALIDATION_SUCCESS = "[UserHandler - SignUp] Validation successful for user: {}";
    public static final String SIGNUP_MAPPED_DTO_TO_ENTITY = "[UserHandler - SignUp] Mapped DTO to Entity";
    public static final String SIGNUP_USER_SAVED = "[UserHandler - SignUp] User saved successfully with ID: {}";
    public static final String SIGNUP_MAPPED_ENTITY_TO_RESPONSE = "[UserHandler - SignUp] Mapped Entity to Response: {}";
    public static final String SIGNUP_RESPONSE_CREATED = "[UserHandler - SignUp] Response successfully created";
    public static final String SIGNUP_ERROR = "[UserHandler - SignUp] Error during sign-up process: {}";
}