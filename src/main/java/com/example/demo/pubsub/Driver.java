package com.example.demo.pubsub;


import com.example.demo.pubsub.consumer.Consumer;
import com.example.demo.pubsub.queue.Message;
import com.example.demo.pubsub.queue.Topic;
import com.example.demo.pubsub.util.KeyedExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Driver {

    public static void main(String[] args) throws InterruptedException {

        KeyedExecutor keyedExecutor = new KeyedExecutor(3);
        keyedExecutor.submit(1, () -> {
            log.info("1");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                log.info("Wea are done");
            }

        });
        log.info("----1---");
        keyedExecutor.submit(1, () -> log.info("2"));
//
//
//        Thread.sleep(15);
        log.info("----2---");


        Topic topic = new Topic();
//        Topic topic2 = new Topic();
        Consumer consumer1 = new Consumer(topic, 0);
        Consumer consumer2 = new Consumer(topic, 5);
        Consumer consumer3 = new Consumer(topic, 0);
        Consumer consumer4 = new Consumer(topic, 9);
        Consumer consumer5 = new Consumer(topic, 5);
        new Thread(() -> topic.sendMessagesToAllConsumer()).start();
        for (int j = 0; j < 2; j++) {
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                int finalJ = j;
                threads.add(new Thread(() -> {
                    if (finalI == 2 && finalJ == 0) {
                        consumer1.sendSubscriptionRequest(topic, 2);
                    }
                    Message message = Message.builder().content(String.format("Batch : %s , Message : %s", finalJ, finalI)).build();
//                        Thread.sleep(Util.getRandomInRange(500, 400));
                    topic.sendMessage(message);
                }));
            }
            threads.get(0).start();
            threads.get(1).start();
            Thread.sleep(1000);
//                threads.forEach(Thread::start);
            for (int i = 2; i < 5; i++) {
                threads.get(i).start();
            }
//            }
//            Thread.sleep(Util.getRandomInRange(500, 400));
//            topic.sendMessagesToAllConsumer();
//            topic2.sendMessagesToAllConsumer();
//            topic2.sendMessagesToAllConsumer();

            log.info("sleeppppppppp");

            Thread.sleep(2000);
        }
    }
}

