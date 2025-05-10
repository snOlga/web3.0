// Bitrix24ConnectionFactoryImpl.java
package micro.service.admin.jca.bitrix24;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;

import javax.naming.NamingException;
import javax.naming.Reference;

public class Bitrix24ConnectionFactoryImpl implements Bitrix24ConnectionFactory {
    private static final long serialVersionUID = 1L;

    private Bitrix24ManagedConnectionFactory mcf;
    private ConnectionManager cm;

    public Bitrix24ConnectionFactoryImpl(Bitrix24ManagedConnectionFactory mcf, ConnectionManager cm) {
        this.mcf = mcf;
        this.cm = cm;
    }

    @Override
    public Bitrix24Connection getConnection() throws ResourceException {
        return (Bitrix24Connection) cm.allocateConnection(mcf, null);
    }

    @Override
    public void setReference(Reference reference) {

    }

    @Override
    public Reference getReference() throws NamingException {
        return null;
    }
}
