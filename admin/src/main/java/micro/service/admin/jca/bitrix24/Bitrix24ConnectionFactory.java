package micro.service.admin.jca.bitrix24;

import jakarta.resource.Referenceable;
import java.io.Serializable;

public interface Bitrix24ConnectionFactory extends Serializable, Referenceable {
    Bitrix24Connection getConnection() throws jakarta.resource.ResourceException;
}
