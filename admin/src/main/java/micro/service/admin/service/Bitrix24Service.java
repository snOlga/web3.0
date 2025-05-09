package micro.service.admin.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Bitrix24Service {

    @Value("${bitrix.webhook.url}")
    private String webhookUrl;

    public String createTask(String title, String description) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(webhookUrl + "tasks.task.add");

            JSONObject requestBody = new JSONObject();
            JSONObject fields = new JSONObject();

            fields.put("TITLE", title);
            fields.put("DESCRIPTION", description);
            fields.put("RESPONSIBLE_ID", 1); // ID ответственного

            requestBody.put("fields", fields);

            StringEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String updateTask(int taskId, String status) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(webhookUrl + "tasks.task.update");

            JSONObject requestBody = new JSONObject();
            JSONObject fields = new JSONObject();

            fields.put("TASK_ID", taskId);
            fields.put("STATUS", status); // 2 - в работе, 5 - завершена

            requestBody.put("fields", fields);

            StringEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}