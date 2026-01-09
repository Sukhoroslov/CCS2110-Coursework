public interface SocialGraphInterface {
    // Pre: u != null, u.id unique. Post: User added. O(n)
    void addUser(User u);
    
    // Pre: u1,u2 exist, no self/edge, weight>0. Post: Bidirectional edge. O(n)
    void addFriendship(User u1, User u2, double weight);
    
    // Pre: edge exists. Post: Edge removed. O(n)
    void removeFriendship(User u1, User u2);
    
    // Pre: u exists. Post: Friends list. O(n)
    ArrayBasedList getFriends(User u);
    
    // Pre: u exists, limit>=0. Post: Top recommendations. O(V+E)
    ArrayBasedList recommendFriends(User u, int limit);
}
