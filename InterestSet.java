// InterestSet.java
// This class handles a collection of unique interests (no duplicates).
// It uses a HashSet inside to make adding/removing fast.
import java.util.HashSet;
import java.util.Set;

public class InterestSet implements InterestSetInterface {
    // This is the internal storage for interests, using HashSet for quick lookups.
    private HashSet<String> interests;

    // Constructor: Sets up an empty set when you create a new InterestSet.
    public InterestSet() {
        this.interests = new HashSet<>();
    }

    // Adds an item (interest) to the set.
    // First checks if it's not null, then adds it in lowercase to ignore case differences.
    // Example: Adding "Coding" and "coding" will only add once.
    @Override
    public void add(String item) {
        if (item != null) {
            interests.add(item.toLowerCase());
        }
    }

    // Removes an item from the set.
    // Checks if not null, then removes the lowercase version.
    // If it's not there, nothing happens.
    @Override
    public void remove(String item) {
        if (item != null) {
            interests.remove(item.toLowerCase());
        }
    }

    // Returns the number of unique interests.
    // Very fast, just asks the HashSet for its size.
    @Override
    public int size() {
        return interests.size();
    }

    // Finds common interests with another set.
    // Creates a new empty set, then loops through this set's items.
    // If the other set has the same item, adds it to the new set.
    // Returns the new set with only commons.
    @Override
    public InterestSet intersection(InterestSet other) {
        InterestSet common = new InterestSet();
        if (other != null) {
            for (String interest : this.interests) {
                if (other.interests.contains(interest)) {
                    common.add(interest);
                }
            }
        }
        return common;
    }

    // Helper method to calculate the total unique items from two sets (union).
    // Used for scoring similarities later.
    // Copies this set, adds all from the other, and gets the size.
    public int unionSize(InterestSet other) {
        Set<String> union = new HashSet<>(interests);
        if (other != null) {
            union.addAll(other.interests);
        }
        return union.size();
    }
}