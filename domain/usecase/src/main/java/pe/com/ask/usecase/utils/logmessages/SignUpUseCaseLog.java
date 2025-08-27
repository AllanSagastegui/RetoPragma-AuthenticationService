package pe.com.ask.usecase.utils.logmessages;

public final class SignUpUseCaseLog {

    private SignUpUseCaseLog() {}

    public static final String START_FLOW = "[SignUp - UseCase] Start sign-up User flow for email: {}, dni: {}";
    public static final String USER_ALREADY_EXISTS_EMAIL = "[SignUp - UseCase] User already exists with email: {}";
    public static final String USER_ALREADY_EXISTS_DNI = "[SignUp - UseCase] User already exists with DNI: {}";
    public static final String ROLE_FOUND = "[SignUp - UseCase] Role CLIENT found with id: {}";
    public static final String ROLE_NOT_FOUND = "[SignUp - UseCase] Role CLIENT not found";
    public static final String PASSWORD_HASHED = "[SignUp - UseCase] Password hashed for user: {}";
    public static final String USER_SIGNED_UP = "[SignUp - UseCase] User signed up successfully: {}";
    public static final String SIGNUP_ERROR = "[SignUp - UseCase] Sign-up error for {}: {}";
}