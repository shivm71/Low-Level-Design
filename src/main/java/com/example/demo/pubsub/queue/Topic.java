package com.example.demo.pubsub.queue;


import com.example.demo.pubsub.consumer.Consumer;
import com.example.demo.pubsub.util.Util;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Data
public class Topic {
    private static final int MESSAGE_SEND_RETRY_INTERVAL = 5000;
    private static final int MESSAGE_DELETE_RETRY_INTERVAL = 5000;
    private final String name = String.format("TOPIC - %s", Util.getRandomInRange(1, 100));
    AtomicBoolean isQueueBusy = new AtomicBoolean(false);
    private List<Message> queue = new ArrayList<>();
    private int MAX_SIZE = 0;
    private int deletedElements = 0;
    //ConsumerID , Consumer
    private Map<Integer, Consumer> consumerMap = new HashMap<>();
    //ConsumerID , offset
    private Map<Integer, Integer> offsetMap = new HashMap<>();
    //ConsumerID, Thread
    private Map<Integer, Thread> threadMap = new HashMap<>();

    public Topic() {
        new Thread(() -> deleteExpiredElements()).start();
    }

    private void waitForQueue() {
        while (isQueueBusy.get()) {
        }
    }

    @SneakyThrows
    private void deleteExpiredElements() {
        while (true) {
            log.info("Deleting the Queue Message");
            waitForQueue();
            List<Message> tempQueue = new ArrayList<>(getQueue());
            isQueueBusy.set(true);
            for (Message message : tempQueue) {
                if (message.getExpiryTime() < System.currentTimeMillis()) {
                    log.info("Deleting message - {}", message);
                    queue.remove(message);
                    deletedElements += 1;
                }
            }
            isQueueBusy.set(false);
            Thread.sleep(MESSAGE_DELETE_RETRY_INTERVAL);
        }
    }

    public void sendMessage(Message message) {
        log.info("Message send request received in topic : {} as message : {}", name, message);
        synchronized (this) {
            waitForQueue();
            isQueueBusy.set(true);
            queue.add(message);
            MAX_SIZE += 1;
            isQueueBusy.set(false);
        }
    }

    public Message getQueueElement(int index) {
        waitForQueue();
        return queue.get(index - deletedElements);

    }

    public void addConsumer(Consumer consumer, int offset) {
        consumerMap.put(consumer.getID(), consumer);
        offsetMap.put(consumer.getID(), offset);
    }

    public void removeConsumer(Consumer consumer) {
        if (consumerMap.containsKey(consumer.getID())) {
            consumerMap.remove(consumer.getID());
            offsetMap.remove(consumer.getID());
            if (threadMap.containsKey(consumer.getID())) {
                threadMap.remove(consumer.getID());
            }

        }
    }

    private void sendAllMessageToConsumer(Consumer consumer) {
        Integer offset = offsetMap.get(consumer.getID());
        for (int i = offset + 1; i < MAX_SIZE; i++) {
            consumer.processMessage(getQueueElement(i));
            offsetMap.put(consumer.getID(), i);
        }
        Thread.currentThread().interrupt();
    }

    @SneakyThrows
    public void sendMessagesToAllConsumer() {


        while (true) {
            if (MAX_SIZE == 0) {
                Thread.sleep(MESSAGE_SEND_RETRY_INTERVAL);
                continue;
            }
            log.info("Sending messages to all consumers in topic : {}", name);
            for (Map.Entry<Integer, Consumer> entry : consumerMap.entrySet()) {
                Integer consumerId = entry.getKey();
                Thread consumerThread = threadMap.getOrDefault(consumerId, null);
                if (Objects.isNull(consumerThread) || !consumerThread.isAlive()) {
                    consumerThread = new Thread(() -> sendAllMessageToConsumer(entry.getValue()));
                    consumerThread.start();
                } else {
                    log.info("Topic Thread is already running for {}", consumerId);
                }

                threadMap.put(consumerId, consumerThread);
            }
            Thread.sleep(MESSAGE_SEND_RETRY_INTERVAL);


        }

    }


}



