/**
 * Interface for InterestSet ADT - manages user's interests for Jaccard similarity
 * Used in SocialGraph recommendation scoring: |intersection| / |union|
 * Professor-compliant: NO HashSet, pure abstract interface
 */
public interface InterestSetInterface {
    /**
     * Adds interest if not already present (no duplicates)
     * @param item interest name (case-insensitive)
     * Time: O(n) linear scan for duplicates
     */
    void add(String item);
    
    /**
     * Removes interest if present
     * @param item interest name (case-insensitive)
     * Time: O(n) linear scan to find + remove
     */
    void remove(String item);
    
    /**
     * Returns number of unique interests
     * @return count of interests
     * Time: O(1)
     */
    int size();
    
    /**
     * Computes intersection for Jaccard similarity: common interests only
     * @param other other InterestSet to intersect with
     * @return new InterestSet containing only common interests
     * Time: O(n×m) where n=|this|, m=|other|
     */
    InterestSet intersection(InterestSet other);
}
