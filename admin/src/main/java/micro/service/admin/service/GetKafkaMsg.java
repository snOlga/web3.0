package micro.service.admin.service;

import org.springframework.kafka.annotation.KafkaListener;

public class GetKafkaMsg {

    @KafkaListener(topics = "comments")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }
}
