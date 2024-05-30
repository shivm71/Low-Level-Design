package com.example.demo.pubsub.queue;


import com.example.demo.pubsub.util.Util;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    String content;

    @Builder.Default
    String ID = "Message ID -" + Util.getRandomInRange(100, 4500);

    @Builder.Default
    long expiryTime = (System.currentTimeMillis() + Util.getRandomInRange(10000, 19000));

    int ttl;

    @Builder.Default
    boolean isDeleted = false;

}

