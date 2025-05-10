package micro.service.admin.jca.bitrix24;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

public class Bitrix24ManagedConnectionFactory implements ManagedConnectionFactory {
    private String webhookUrl;

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new Bitrix24ConnectionFactoryImpl(this, cxManager);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        return new Bitrix24ConnectionFactoryImpl(this, null);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        return new Bitrix24ManagedConnection(this);
    }

    @Override
    public ManagedConnection matchManagedConnections(
            Set connectionSet, Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {}

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return null;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Bitrix24ManagedConnectionFactory) {
            Bitrix24ManagedConnectionFactory other = (Bitrix24ManagedConnectionFactory) obj;
            return (this.webhookUrl == null ? other.webhookUrl == null : this.webhookUrl.equals(other.webhookUrl));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return webhookUrl != null ? webhookUrl.hashCode() : 0;
    }
}
