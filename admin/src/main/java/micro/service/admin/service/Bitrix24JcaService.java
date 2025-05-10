package micro.service.admin.service;

import micro.service.admin.jca.bitrix24.Bitrix24Connection;
import micro.service.admin.jca.bitrix24.Bitrix24ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Bitrix24JcaService {
    private final Bitrix24ConnectionFactory connectionFactory;

    @Autowired
    public Bitrix24JcaService(Bitrix24ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String createTask(String title, String description) throws Exception {
        Bitrix24Connection connection = connectionFactory.getConnection();
        try {
            return connection.createTask(title, description);
        } finally {
            connection.close();
        }
    }

    public String updateTask(int taskId, String status) throws Exception {
        Bitrix24Connection connection = connectionFactory.getConnection();
        try {
            return connection.updateTask(taskId, status);
        } finally {
            connection.close();
        }
    }
}
