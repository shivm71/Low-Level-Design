package com.example.demo.twittor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Twittor {

    public static void main(String[] args) {
        TweetDriver tweetDriver = new TweetDriver();
        tweetDriver.run();
    }
}

class Analytics {

    int retweet = 0;
    int likes = 0;
    // audit
    String createdBy;
    Long createdTime = Instant.now().toEpochMilli();
}

class Tweet extends Analytics {

    public String id = UUID.randomUUID().toString();
    String content;
    List<String> hashtags;

    @Override
    public String toString() {
        return "Content = " + content + ", hashtags =" + hashtags + " retweet = " + retweet + " Likes = " + likes;
    }

}


class TweetStore {

    private static final TweetStore INSTANCE = new TweetStore();
    Map<String, List<Tweet>> store = new HashMap<>();

    private TweetStore() {
    }

    public static TweetStore getInstance() {
        return INSTANCE;
    }
}


class UserStore {

    private static final UserStore INSTANCE = new UserStore();
    Map<String, User> store = Map.of("user1", new User(), "user2", new User(), "user3", new User());

    private UserStore() {
    }

    public static UserStore getInstance() {
        return INSTANCE;
    }


}

class TweetService {

    TweetStore tweetStore = TweetStore.getInstance();

    public Tweet addTweet(String userId, Tweet tweet) {
        if (!tweetStore.store.containsKey(userId)) {
            tweetStore.store.put(userId, new ArrayList<>());
        }
        tweetStore.store.get(userId).add(tweet);
        return tweet;
    }

    public List<Tweet> getAllTweets(String userId) {
        return tweetStore.store.getOrDefault(userId, new ArrayList<>());
    }

    public void retweet(String userId, String tweetId) {
        Tweet tweet = tweetStore.store.getOrDefault(userId, new ArrayList<>()).stream().filter(tw -> tweetId.equals(tw.id)).findFirst().get();
        tweet.retweet += 1;
    }

}

class TimeLineService {

    TweetStore tweetStore = TweetStore.getInstance();
    UserStore userStore = UserStore.getInstance();

    public List<Tweet> getTimeLine(String userId) {
        Set<String> followerIds = userStore.store.get(userId).followers;
        List<Tweet> allTweets = followerIds.stream().map(id -> tweetStore.store.get(id)).flatMap(List::stream).toList();
        List<Tweet> sortedTweets = allTweets.stream().sorted((a, b) -> Math.toIntExact(a.createdTime - b.createdTime)).toList();
        return sortedTweets;
    }

}


class UserService {

    UserStore userStore = UserStore.getInstance();

    public void addFollower(String userId, String followerId) {
        User user = userStore.store.get(userId);
        User followerUser = userStore.store.get(followerId);

        followerUser.followees.add(userId);
        user.followers.add(followerId);

    }

}


class User {
    String name;
    Set<String> followers = new HashSet<>();
    Set<String> followees = new HashSet<>();

}


class TweetDriver {

    TweetService tweetService = new TweetService();
    UserService userService = new UserService();
    TimeLineService timeLineService = new TimeLineService();

    public void run() {

        Tweet tweet = new Tweet();
        tweet.content = "This is my first tweet";
        tweet.hashtags = List.of("This", "Shivam", "uber");

        Tweet tweet2 = new Tweet();
        tweet2.content = "This is my second tweet";
        tweet2.hashtags = List.of("This", "Shivam", "uber");

        Tweet tweet3 = new Tweet();
        tweet3.content = "This is my third tweet";
        tweet3.hashtags = List.of("This", "Shivam", "uber");

        Tweet tweet4 = new Tweet();
        tweet4.content = "This is my fourth tweet";
        tweet4.hashtags = List.of("This", "Shivam", "uber");


        tweetService.addTweet("user1", tweet);
        tweetService.addTweet("user1", tweet2);
        tweetService.addTweet("user2", tweet3);
        tweetService.addTweet("user3", tweet4);

        tweetService.retweet("user1", tweet.id);

        System.out.println(tweetService.getAllTweets("user1"));
        System.out.println(tweetService.getAllTweets("user2"));

        userService.addFollower("user1", "user2");
        userService.addFollower("user2", "user1");
        userService.addFollower("user2", "user3");
        System.out.println(timeLineService.getTimeLine("user2"));
        System.out.println(timeLineService.getTimeLine("user1"));

    }

}


