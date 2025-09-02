package pe.com.ask.usecase.utils.logmessages;

public final class GetAllClientsLog {

    private GetAllClientsLog() {}

    public static final String START_FLOW = "[GetAllClients - UseCase] Start GetAllClients flow";
    public static final String ROLE_FOUND = "[GetAllClients - UseCase] Role CLIENT found: {0}";
    public static final String ROLE_NOT_FOUND = "[GetAllClients - UseCase] Role CLIENT not found";
    public static final String CLIENT_FOUND = "[GetAllClients - UseCase] Client user found: {0}";
    public static final String END_FLOW = "[GetAllClients - UseCase] Finished GetAllClients flow";
    public static final String ERROR = "[GetAllClients - UseCase] Error in GetAllClientsUseCase: {0}";
}