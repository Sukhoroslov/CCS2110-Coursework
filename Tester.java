// Tester.java
// This class tests the whole system with sample data.
// It creates users, adds friendships, runs operations, and checks performance.
import java.util.List;
import java.util.Random;

public class Tester {
    public static void main(String[] args) {
        // Create a new social graph.
        SocialGraph graph = new SocialGraph();

        // Set up sample data: 20 users with random interests.
        int numUsers = 20;
        Random rand = new Random();
        String[] interestPool = {"coding", "gaming", "music", "sports", "travel", "reading", "art", "tech", "food", "movies"};
        for (int i = 1; i <= numUsers; i++) {
            User u = new User("user" + i);
            for (int j = 0; j < 4; j++) {
                // Pick random interests from the pool.
                u.addInterest(interestPool[rand.nextInt(interestPool.length)]);
            }
            graph.addUser(u);
        }

        // Add some friendships: each user gets 3 friends in a cycle.
        for (int i = 1; i <= numUsers; i++) {
            for (int j = 1; j <= 3; j++) {
                int friend = (i + j) % numUsers + 1;
                if (friend != i) {
                    // Random weight between 1 and 11.
                    graph.addFriendship("user" + i, "user" + friend, rand.nextDouble() * 10 + 1);
                }
            }
        }

        // Test: Show friends of user1.
        System.out.println("Friends of user1:");
        for (String f : graph.getFriends("user1")) {
            System.out.println(f);
        }

        // Test: Remove a friendship.
        graph.removeFriendship("user1", "user2");
        System.out.println("After remove user2:");
        for (String f : graph.getFriends("user1")) {
            System.out.println(f);
        }

        // Test: Add an interest and get recommendations.
        graph.getUser("user1").addInterest("newinterest");
        System.out.println("Recommendations for user1:");
        List<String> recs = graph.recommendFriends("user1", 5);
        for (String r : recs) {
            System.out.println(r);
        }

        // Performance test: Create a bigger graph with 1000 users.
        int largeNum = 1000;
        SocialGraph largeGraph = new SocialGraph();
        for (int i = 1; i <= largeNum; i++) {
            User u = new User("u" + i);
            for (int j = 0; j < 5; j++) {
                u.addInterest(interestPool[rand.nextInt(interestPool.length)]);
            }
            largeGraph.addUser(u);
        }
        for (int i = 1; i <= largeNum; i++) {
            for (int j = 1; j <= 5; j++) {
                // Random friends, avoiding self.
                int friend = rand.nextInt(largeNum) + 1;
                if (friend != i) {
                    largeGraph.addFriendship("u" + i, "u" + friend, rand.nextDouble() * 10 + 1);
                }
            }
        }

        // Run recommendation 5 times and average the time.
        long totalTime = 0;
        int runs = 5;
        for (int r = 0; r < runs; r++) {
            long start = System.nanoTime();
            largeGraph.recommendFriends("u1", 10);
            long end = System.nanoTime();
            totalTime += (end - start);
        }
        System.out.println("Average time for recommendFriends on 1000 users: " + (totalTime / runs / 1_000_000) + " ms");
    }
}