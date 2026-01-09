/**
 * User ADT - simple wrapper for InterestSet
 * Delegates all interest operations to InterestSet (professor ADTs only)
 * Used as nodes in SocialGraph ArrayBasedList
 */
public class User {
    private String id;                    // Unique user identifier
    private InterestSet interests;        // Professor-compliant interests
    
    /**
     * Creates user with unique ID and empty interests
     */
    public User(String id) {
        this.id = id;
        this.interests = new InterestSet();  // Uses ArrayBasedList internally
    }
    
    /**
     * Add interest (no duplicates, case-insensitive)
     * Delegates to InterestSet.add() → O(n) linear scan
     */
    public void addInterest(String interest) {
        interests.add(interest);
    }
    
    /**
     * Remove interest (case-insensitive)
     * Delegates to InterestSet.remove() → O(n) linear scan
     */
    public void removeInterest(String interest) {
        interests.remove(interest);
    }
    
    /**
     * Get interests for Jaccard similarity computation
     * @return InterestSet (contains ArrayBasedList of interests)
     */
    public InterestSet getInterests() {
        return interests;
    }
    
    /**
     * Get unique user ID
     * Used by SocialGraph.findUserIndex() linear scan
     */
    public String getId() {
        return id;
    }
    
    // Optional: toString for debugging
    public String toString() {
        return "User[" + id + ", interests=" + interests.size() + "]";
    }
}
