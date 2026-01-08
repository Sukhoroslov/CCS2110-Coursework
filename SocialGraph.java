// SocialGraph.java
// This class is like the whole social network.
// It keeps track of users and their friendships (with weights for how close they are).
import java.util.*;

public class SocialGraph {
    // Maps user IDs to User objects.
    private HashMap<String, User> users;
    // Maps user IDs to their friends (another map of friend ID to weight).
    private HashMap<String, HashMap<String, Double>> adj;

    // Constructor: Sets up empty maps for users and friendships.
    public SocialGraph() {
        users = new HashMap<>();
        adj = new HashMap<>();
    }

    // Adds a new user to the network.
    // Checks if user is valid and not already added.
    // Also adds an empty friend list for them.
    public void addUser(User u) {
        if (u != null && !users.containsKey(u.getId())) {
            users.put(u.getId(), u);
            adj.put(u.getId(), new HashMap<>());
        }
    }

    // Adds a friendship between two users with a weight (like how much they interact).
    // Checks if both exist, not the same person, and no existing friendship.
    // Adds both ways since friendships are mutual.
    public void addFriendship(String u1, String u2, double weight) {
        if (users.containsKey(u1) && users.containsKey(u2) && !u1.equals(u2) && !adj.get(u1).containsKey(u2)) {
            adj.get(u1).put(u2, weight);
            adj.get(u2).put(u1, weight);
        }
    }

    // Removes a friendship between two users.
    // Checks if both exist, then removes from both sides.
    public void removeFriendship(String u1, String u2) {
        if (users.containsKey(u1) && users.containsKey(u2)) {
            adj.get(u1).remove(u2);
            adj.get(u2).remove(u1);
        }
    }

    // Gets all friends of a user.
    // Returns the set of friend IDs.
    // If no friends, returns empty set.
    public Set<String> getFriends(String u) {
        if (adj.containsKey(u)) {
            return adj.get(u).keySet();
        }
        return new HashSet<>();
    }

    // Recommends friends for a user, up to the limit.
    // Uses BFS to explore friends-of-friends (up to depth 3).
    // Scores based on shared interests, mutual friends, and average weights.
    // Sorts and picks top ones.
    public List<String> recommendFriends(String u, int limit) {
        if (!users.containsKey(u) || limit <= 0) {
            return new ArrayList<>();
        }
        // Set up for BFS: queue for users to visit, visited to avoid repeats, depths to track distance.
        int maxDepth = 3;
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> depthMap = new HashMap<>();
        queue.add(u);
        visited.add(u);
        depthMap.put(u, 0);

        // BFS loop: Start from the user, explore friends, then their friends, up to max depth.
        while (!queue.isEmpty()) {
            String current = queue.poll();
            int depth = depthMap.get(current);
            if (depth >= maxDepth) continue;
            for (String friend : getFriends(current)) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    depthMap.put(friend, depth + 1);
                    queue.add(friend);
                }
            }
        }

        // Collect candidates: Users found in BFS, but not self or direct friends.
        List<String> candidates = new ArrayList<>();
        Set<String> directFriends = getFriends(u);
        for (String v : visited) {
            if (!v.equals(u) && !directFriends.contains(v) && depthMap.get(v) > 0) {
                candidates.add(v);
            }
        }

        // Score each candidate.
        User user = users.get(u);
        List<Score> scores = new ArrayList<>();
        for (String cand : candidates) {
            User cUser = users.get(cand);
            // Calculate Jaccard similarity: common interests / total unique interests.
            InterestSet inter = user.getInterests().intersection(cUser.getInterests());
            int unionSize = user.getInterests().unionSize(cUser.getInterests());
            double jacc = unionSize > 0 ? (double) inter.size() / unionSize : 0.0;
            // Count mutual friends: friends shared between user and candidate.
            int mutual = 0;
            for (String f : directFriends) {
                if (getFriends(cand).contains(f)) {
                    mutual++;
                }
            }
            // Average weight: average strength of connections to mutual friends.
            double avgWeight = 0.0;
            if (mutual > 0) {
                double sum = 0.0;
                for (String f : directFriends) {
                    if (getFriends(cand).contains(f)) {
                        sum += adj.get(u).getOrDefault(f, 0.0);
                    }
                }
                avgWeight = sum / mutual;
            }
            // Total score: combine similarity, mutuals, and weights.
            double score = jacc + mutual + avgWeight;
            scores.add(new Score(cand, score));
        }

        // Sort scores from highest to lowest.
        scores.sort((a, b) -> Double.compare(b.score, a.score));

        // Pick top recommendations up to the limit.
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, scores.size()); i++) {
            result.add(scores.get(i).id);
        }
        return result;
    }

    // Helper class for holding a user ID and their score during sorting.
    private static class Score {
        String id;
        double score;
        Score(String id, double score) {
            this.id = id;
            this.score = score;
        }
    }

    // Gets a user by ID.
    public User getUser(String id) {
        return users.get(id);
    }

    // Checks if two users are friends.
    public boolean areFriends(String u1, String u2) {
        return adj.containsKey(u1) && adj.get(u1).containsKey(u2);
    }

    // Gets the total number of users.
    public int size() {
        return users.size();
    }
}