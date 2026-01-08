// User.java
// This class represents a person in the social network.
// Each user has an ID (like "user1") and a set of interests.
public class User {
    // User's unique ID.
    private String id;
    // User's interests, stored in an InterestSet for easy management.
    private InterestSet interests;

    // Constructor: Creates a new user with the given ID and empty interests.
    public User(String id) {
        this.id = id;
        this.interests = new InterestSet();
    }

    // Adds an interest to the user's list.
    // Just passes it to the InterestSet.
    public void addInterest(String interest) {
        interests.add(interest);
    }

    // Removes an interest if it exists.
    // Passes to InterestSet.
    public void removeInterest(String interest) {
        interests.remove(interest);
    }

    // Gets the whole set of interests.
    // Returns the InterestSet object.
    public InterestSet getInterests() {
        return interests;
    }

    // Gets the user's ID.
    // Simple getter.
    public String getId() {
        return id;
    }
}