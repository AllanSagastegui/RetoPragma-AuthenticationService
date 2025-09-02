package pe.com.ask.api.utils.logmessages;

public final class SignInLog {

    private SignInLog() {}

    public static final String SIGNIN_START = "[UserHandler - SignIn] Starting sign-in request processing...";
    public static final String SIGNIN_RECEIVED_BODY = "[UserHandler - SignIn] Received request body";
    public static final String SIGNIN_VALIDATION_SUCCESS = "[UserHandler - SignIn] Validation successful for user: {}";
    public static final String SIGNIN_ATTEMPT = "[UserHandler - SignIn] Attempting login for user: {}";
    public static final String SIGNIN_SUCCESS = "[UserHandler - SignIn] User signed in successfully, token generated";
    public static final String SIGNIN_RESPONSE_CREATED = "[UserHandler - SignIn] Response successfully created";
    public static final String SIGNIN_ERROR = "[UserHandler - SignIn] Error during sign-in process: {}";
}