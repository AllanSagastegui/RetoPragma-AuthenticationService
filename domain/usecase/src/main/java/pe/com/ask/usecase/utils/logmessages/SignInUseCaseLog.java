package pe.com.ask.usecase.utils.logmessages;

public class SignInUseCaseLog {

    private SignInUseCaseLog() {}

    public static final String START_FLOW = "[SignIn - UseCase] Start sign-in User flow";
    public static final String SUBSCRIBED_FIND_BY_EMAIL = "[SignIn - UseCase] Subscribed to findByEmail for {}";
    public static final String USER_FOUND = "[SignIn - UseCase] User found {}";
    public static final String USER_NOT_FOUND = "[SignIn - UseCase] User not found for {}";
    public static final String VALIDATING_PASSWORD = "[SignIn - UseCase] Validating password for user: {}";
    public static final String PASSWORD_VALIDATION_PASSED = "[SignIn - UseCase] Password validation passed for user: {}";
    public static final String PASSWORD_VALIDATION_FAILED = "[SignIn - UseCase] Password validation failed for user: {}";
    public static final String TOKEN_GENERATED = "[SignIn - UseCase] Token generated successfully for email: {}";
    public static final String SIGNIN_ERROR = "[SignIn - UseCase] Sign-in error for {}: {}";
}