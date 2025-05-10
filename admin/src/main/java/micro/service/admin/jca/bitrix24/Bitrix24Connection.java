package micro.service.admin.jca.bitrix24;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.Connection;
import java.io.Closeable;

public interface Bitrix24Connection extends Connection, Closeable {
    String createTask(String title, String description) throws ResourceException;
    String updateTask(int taskId, String status) throws ResourceException;
}
