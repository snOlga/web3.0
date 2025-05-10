package micro.service.admin.jca.bitrix24;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Bitrix24ManagedConnection implements ManagedConnection {
    private static final Logger log = Logger.getLogger(Bitrix24ManagedConnection.class.getName());

    private final Bitrix24ManagedConnectionFactory mcf;
    private PrintWriter logWriter;
    private Bitrix24ConnectionImpl connection;
    private final List<ConnectionEventListener> listeners = new ArrayList<>();

    public Bitrix24ManagedConnection(Bitrix24ManagedConnectionFactory mcf) {
        this.mcf = mcf;
        this.connection = new Bitrix24ConnectionImpl(this, mcf.getWebhookUrl());
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return connection;
    }

    @Override
    public void destroy() throws ResourceException {
        connection = null;
    }

    @Override
    public void cleanup() throws ResourceException {
        connection = null;
    }

    @Override
    public void associateConnection(Object connection) throws ResourceException {
        if (!(connection instanceof Bitrix24ConnectionImpl)) {
            throw new ResourceException("Invalid connection type");
        }
        this.connection = (Bitrix24ConnectionImpl) connection;
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new ManagedConnectionMetaData() {
            @Override
            public String getEISProductName() throws ResourceException {
                return "Bitrix24 Connector";
            }

            @Override
            public String getEISProductVersion() throws ResourceException {
                return "1.0";
            }

            @Override
            public int getMaxConnections() throws ResourceException {
                return 0;
            }

            @Override
            public String getUserName() throws ResourceException {
                return null;
            }
        };
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        logWriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }
}
