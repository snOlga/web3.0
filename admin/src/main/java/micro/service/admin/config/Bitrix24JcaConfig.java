package micro.service.admin.config;

import micro.service.admin.jca.bitrix24.Bitrix24ConnectionFactory;
import micro.service.admin.jca.bitrix24.Bitrix24ManagedConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.NamingException;
import javax.naming.Reference;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class Bitrix24JcaConfig {

    @Value("${bitrix.webhook.url}")
    private String webhookUrl;

    @Bean
    public Bitrix24ManagedConnectionFactory bitrix24ManagedConnectionFactory() {
        Bitrix24ManagedConnectionFactory mcf = new Bitrix24ManagedConnectionFactory();
        mcf.setWebhookUrl(webhookUrl);
        return mcf;
    }

    @Bean
    public Bitrix24ConnectionFactory bitrix24ConnectionFactory(
            Bitrix24ManagedConnectionFactory mcf) throws ResourceException, NamingException {
        ConnectionManager cm = new ConnectionManager() {
            @Override
            public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo cxRequestInfo)
                    throws ResourceException {
                ManagedConnection mc = mcf.createManagedConnection(null, cxRequestInfo);
                return mc.getConnection(null, cxRequestInfo);
            }
        };

        Bitrix24ConnectionFactory connectionFactory = (Bitrix24ConnectionFactory)
                mcf.createConnectionFactory(cm);

        Reference reference = new Reference(
                Bitrix24ConnectionFactory.class.getName(),
                "micro.service.admin.jca.bitrix24.Bitrix24ManagedConnectionFactory",
                null);

        connectionFactory.setReference(reference);
        return connectionFactory;
    }
}
