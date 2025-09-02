package pe.com.ask.api.utils.logmessages;

public final class GetAllClientsLog {

    private GetAllClientsLog() {}

    public static final String START_FLOW = "[UserHandler - GetAllClients] Starting get all clients request processing...";
    public static final String CLIENT_FOUND = "[UserHandler - GetAllClients] Client found with email: {}";
    public static final String END_FLOW = "[UserHandler - GetAllClients] Finished get all clients request processing";
    public static final String ERROR = "[UserHandler - GetAllClients] Error occurred while fetching clients: {}";
}