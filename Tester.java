public class Tester {
    public static void main(String[] args) {
        SocialGraph graph = new SocialGraph();
        
        // Create sample users with interests
        String[] interests = {"coding", "gaming", "music", "sports", "travel"};
        User[] users = new User[20];
        for (int i = 0; i < 20; i++) {
            users[i] = new User("user" + (i + 1));
            for (int j = 0; j < 3; j++) {
                users[i].addInterest(interests[(i + j) % 5]);
            }
            graph.addUser(users[i]);
        }
        
        // Add friendships (cycle + random)
        for (int i = 0; i < 20; i++) {
            graph.addFriendship(users[i], users[(i + 1) % 20], 1.0 + i * 0.1);
            if (i < 15) graph.addFriendship(users[i], users[(i + 3) % 20], 2.0);
        }
        
        System.out.println("Friends of user1:");
        ArrayBasedList friends = graph.getFriends(users[0]);
        for (int i = 1; i <= friends.size(); i++) {
            System.out.println(((User) friends.get(i)).getId());
        }
        
        System.out.println("\nRecommendations for user1:");
        ArrayBasedList recs = graph.recommendFriends(users[0], 5);
        for (int i = 1; i <= recs.size(); i++) {
            System.out.println(((User) recs.get(i)).getId());
        }
        
        // Performance test
        performanceTest();
    }
    
    public static void performanceTest() {
        System.out.println("\n=== Performance Test ===");
        long[] times = {0, 0, 0};
        int[] sizes = {100, 1000, 5000};
        
        for (int s = 0; s < 3; s++) {
            long total = 0;
            int size = sizes[s];
            for (int run = 0; run < 5; run++) {
                SocialGraph g = generateGraph(size);
                long start = System.nanoTime();
                g.recommendFriends(g.getUserByIndex(1), 10);
                total += System.nanoTime() - start;
            }
            times[s] = total / 5 / 1_000_000; // ms
            System.out.printf("%d users: %.2f ms avg\n", size, (double) times[s] / 5);
        }
    }
    
    private static SocialGraph generateGraph(int size) {
        SocialGraph g = new SocialGraph();
        String[] interests = {"coding", "gaming", "music", "sports", "travel", "art", "tech"};
        
        // 1. CREATE USERS PROPERLY
        for (int i = 1; i <= size; i++) {
            User u = new User("u" + i);
            for (int j = 0; j < 4; j++) {
                u.addInterest(interests[(i + j) % 7]);
            }
            g.addUser(u);  // ← CRITICAL: ACTUALLY ADD USER
        }
        
        // 2. ADD FRIENDSHIPS PROPERLY  
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= 3; j++) {
                int friendIdx = (i + j * 17) % size + 1;
                if (friendIdx != i) {
                    User u1 = g.getUserByIndex(i);
                    User u2 = g.getUserByIndex(friendIdx);
                    if (u1 != null && u2 != null) {
                        g.addFriendship(u1, u2, 1.5);
                    }
                }
            }
        }
        return g;
    }
}