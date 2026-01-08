// InterestSetInterface.java
// This is the blueprint (interface) for how InterestSet should work.
// It lists the basic actions: add, remove, get size, and find common interests.
public interface InterestSetInterface {
    // Add a new interest (like "coding") to the set.
    // It won't add duplicates because it's a set.
    void add(String item);

    // Remove an interest if it exists in the set.
    void remove(String item);

    // Get how many unique interests are in the set.
    int size();

    // Find common interests between this set and another one.
    // Returns a new set with only the shared items.
    InterestSet intersection(InterestSet other);
}