package com.example.demo.pubsub.consumer;


import com.example.demo.pubsub.queue.Message;
import com.example.demo.pubsub.queue.Topic;
import com.example.demo.pubsub.util.Util;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@Getter
@Slf4j
public class Consumer {

    private Integer ID = Util.getRandomInRange(10, 1000000) + Util.getRandomInRange(10, 1000000);


    public Consumer(Topic topic) {
        sendSubscriptionRequest(topic, 0);
    }

    public Consumer(Topic topic, int offSet) {
        sendSubscriptionRequest(topic, offSet);
    }

    @SneakyThrows
    public void processMessage(Message message) {
        log.info("Message received in consumer - {}, Message : {}",
                ID, message);
        throw new RuntimeException();
//        Thread.sleep(Util.getRandomInRange(100, 1500));
    }
   public void sendSubscriptionRequest(Topic topic, int offset){
        topic.addConsumer(this, offset);
        log.info("Consumer " + ID + " subscribed to topic " + topic.getName());
    }
    public void removeSubscription(Topic topic) {
        topic.removeConsumer(this);
    }


}
