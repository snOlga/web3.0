package micro.service.admin.jca.bitrix24;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionMetaData;
import jakarta.resource.cci.Interaction;
import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bitrix24ConnectionImpl implements Bitrix24Connection, Serializable {
    private static final long serialVersionUID = 1L;

    private Bitrix24ManagedConnection mc;
    private String webhookUrl;
    private List<ConnectionEventListener> listeners = new ArrayList<>();

    public Bitrix24ConnectionImpl(Bitrix24ManagedConnection mc, String webhookUrl) {
        this.mc = mc;
        this.webhookUrl = webhookUrl;
    }

    @Override
    public String createTask(String title, String description) throws ResourceException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(webhookUrl + "tasks.task.add");

            JSONObject requestBody = new JSONObject();
            JSONObject fields = new JSONObject();

            fields.put("TITLE", title);
            fields.put("DESCRIPTION", description);
            fields.put("RESPONSIBLE_ID", 1);

            requestBody.put("fields", fields);

            StringEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new ResourceException("Failed to create task in Bitrix24", e);
        }
    }

    @Override
    public String updateTask(int taskId, String status) throws ResourceException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(webhookUrl + "tasks.task.update");

            JSONObject requestBody = new JSONObject();
            JSONObject fields = new JSONObject();

            fields.put("TASK_ID", taskId);
            fields.put("STATUS", status);

            requestBody.put("fields", fields);

            StringEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new ResourceException("Failed to update task in Bitrix24", e);
        }
    }

    @Override
    public void close() {
        ConnectionEvent event = new ConnectionEvent(mc, ConnectionEvent.CONNECTION_CLOSED);
        event.setConnectionHandle(this);

        for (ConnectionEventListener listener : listeners) {
            listener.connectionClosed(event);
        }
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        listeners.add(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Interaction createInteraction() throws ResourceException {
        return null;
    }

    @Override
    public jakarta.resource.cci.LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public jakarta.resource.cci.ResultSetInfo getResultSetInfo() throws ResourceException {
        return null;
    }
}
