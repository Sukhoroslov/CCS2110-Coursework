public class SocialGraph implements SocialGraphInterface {
    private ArrayBasedList users = new ArrayBasedList();           // User objects
    private ArrayBasedList adjacency = new ArrayBasedList();       // Parallel adj lists
    
    public SocialGraph() {}
    
    private int findUserIndex(String userId) {
        for (int i = 1; i <= users.size(); i++) {
            if (((User) users.get(i)).getId().equals(userId)) return i;
        }
        return -1;
    }
    
    @Override
    public void addUser(User u) {
        if (u == null || findUserIndex(u.getId()) != -1) return;
        users.add(users.size() + 1, u);
        ArrayBasedList emptyAdj = new ArrayBasedList();
        adjacency.add(adjacency.size() + 1, emptyAdj);
    }
    
    @Override
    public void addFriendship(User u1, User u2, double weight) {
        int i1 = findUserIndex(u1.getId()), i2 = findUserIndex(u2.getId());
        if (i1 == -1) { addUser(u1); i1 = findUserIndex(u1.getId()); }
        if (i2 == -1) { addUser(u2); i2 = findUserIndex(u2.getId()); }
        if (i1 == i2 || weight <= 0 || isDirectFriend(i1, i2)) return;
        
        ArrayBasedList adj1 = (ArrayBasedList) adjacency.get(i1);
        ArrayBasedList adj2 = (ArrayBasedList) adjacency.get(i2);
        adj1.add(adj1.size() + 1, new FriendPair(i2, weight));
        adj2.add(adj2.size() + 1, new FriendPair(i1, weight));
    }
    
    @Override
    public void removeFriendship(User u1, User u2) {
        int i1 = findUserIndex(u1.getId()), i2 = findUserIndex(u2.getId());
        if (i1 == -1 || i2 == -1) return;
        removeFriendPair(i1, i2);
        removeFriendPair(i2, i1);
    }
    
    @Override
    public ArrayBasedList getFriends(User u) {
        int i = findUserIndex(u.getId());
        if (i == -1) return new ArrayBasedList();
        
        ArrayBasedList adj = (ArrayBasedList) adjacency.get(i);
        ArrayBasedList friends = new ArrayBasedList();
        for (int j = 1; j <= adj.size(); j++) {
            FriendPair pair = (FriendPair) adj.get(j);
            friends.add(friends.size() + 1, users.get(pair.friendIndex));
        }
        return friends;
    }
    
    @Override
    public ArrayBasedList recommendFriends(User u, int limit) {
        int uidx = findUserIndex(u.getId());
        if (uidx == -1) return new ArrayBasedList();
        
        QueueReferenceBased q = new QueueReferenceBased();
        ArrayBasedList visited = new ArrayBasedList();
        ArrayBasedList distances = new ArrayBasedList();
        ArrayBasedList candidates = new ArrayBasedList();
        
        // BFS setup
        visited.add(1, uidx);
        distances.add(1, 0);
        q.enqueue(uidx);
        
        while (!q.isEmpty()) {
            int vidx = (Integer) q.dequeue();
            int vdist = (Integer) indexToDistance(distances, visited, vidx);
            if (vdist > 3) continue;
            
            ArrayBasedList adj = (ArrayBasedList) adjacency.get(vidx);
            for (int j = 1; j <= adj.size(); j++) {
                FriendPair pair = (FriendPair) adj.get(j);
                int widx = pair.friendIndex;
                
                if (!contains(visited, widx)) {
                    visited.add(visited.size() + 1, widx);
                    distances.add(distances.size() + 1, vdist + 1);
                    q.enqueue(widx);
                }
                
                if (widx != uidx && !isDirectFriend(uidx, widx)) {
                    User w = (User) users.get(widx);
                    double score = calculateScore(u, w);
                    candidates.add(candidates.size() + 1, new UserScore(w, score));
                }
            }
        }
        
        // Simple bubble sort (professor-style)
        bubbleSortScores(candidates);
        
        ArrayBasedList top = new ArrayBasedList();
        int take = Math.min(limit, candidates.size());
        for (int i = 1; i <= take; i++) {
            top.add(top.size() + 1, ((UserScore) candidates.get(i)).user);
        }
        return top;
    }
    
    // Helper methods
    private boolean isDirectFriend(int i1, int i2) {
        ArrayBasedList adj = (ArrayBasedList) adjacency.get(i1);
        for (int j = 1; j <= adj.size(); j++) {
            FriendPair pair = (FriendPair) adj.get(j);
            if (pair.friendIndex == i2) return true;
        }
        return false;
    }
    
    private void removeFriendPair(int i1, int i2) {
        ArrayBasedList adj = (ArrayBasedList) adjacency.get(i1);
        for (int j = 1; j <= adj.size(); j++) {
            FriendPair pair = (FriendPair) adj.get(j);
            if (pair.friendIndex == i2) {
                adj.remove(j);
                break;
            }
        }
    }
    
    private double calculateScore(User u1, User u2) {
        InterestSet common = u1.getInterests().intersection(u2.getInterests());
        double jaccard = (double) common.size() / u1.getInterests().unionSize(u2.getInterests());
        int mutual = countMutualFriends(u1.getId(), u2.getId());
        return 0.6 * jaccard + 0.4 * mutual;
    }
    
    private int countMutualFriends(String id1, String id2) {
        int i1 = findUserIndex(id1), i2 = findUserIndex(id2);
        if (i1 == -1 || i2 == -1) return 0;
        
        ArrayBasedList adj1 = (ArrayBasedList) adjacency.get(i1);
        ArrayBasedList adj2 = (ArrayBasedList) adjacency.get(i2);
        int count = 0;
        
        for (int j = 1; j <= adj1.size(); j++) {
            int f1 = ((FriendPair) adj1.get(j)).friendIndex;
            for (int k = 1; k <= adj2.size(); k++) {
                if (((FriendPair) adj2.get(k)).friendIndex == f1) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
    
    private boolean contains(ArrayBasedList list, int item) {
        for (int i = 1; i <= list.size(); i++) {
            if ((Integer) list.get(i) == item) return true;
        }
        return false;
    }
    
    private int indexToDistance(ArrayBasedList distances, ArrayBasedList visited, int vidx) {
        for (int i = 1; i <= visited.size(); i++) {
            if ((Integer) visited.get(i) == vidx) return (Integer) distances.get(i);
        }
        return 0;
    }
    
    private void bubbleSortScores(ArrayBasedList candidates) {
        for (int i = 1; i <= candidates.size() - 1; i++) {
            for (int j = 1; j <= candidates.size() - i; j++) {
                UserScore s1 = (UserScore) candidates.get(j);
                UserScore s2 = (UserScore) candidates.get(j + 1);
                if (s1.score < s2.score) {
                    candidates.remove(j);
                    candidates.add(j, s2);
                    candidates.remove(j + 2);
                    candidates.add(j + 1, s1);
                }
            }
        }
    }
}

// Helper classes (professor-style)
class FriendPair {
    int friendIndex;
    double weight;
    FriendPair(int idx, double w) { friendIndex = idx; weight = w; }
}

class UserScore {
    User user;
    double score;
    UserScore(User u, double s) { user = u; score = s; }
}
