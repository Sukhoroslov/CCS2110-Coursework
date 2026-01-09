/**
 * InterestSet implementation using ONLY professor's ArrayBasedList
 * NO HashSet/ArrayList/LinkedList - pure coursework compliant
 * Supports Jaccard similarity for friend recommendations
 */
public class InterestSet implements InterestSetInterface {
    private ArrayBasedList interests = new ArrayBasedList();  // Professor ADT only
    
    public InterestSet() {}  // Empty set
    
    @Override
    public void add(String item) {
        // No duplicates + case-insensitive
        if (item != null && !contains(item.toLowerCase())) {
            interests.add(interests.size() + 1, item.toLowerCase());
        }
    }
    
    @Override
    public void remove(String item) {
        if (item == null) return;
        String target = item.toLowerCase();
        for (int i = 1; i <= interests.size(); i++) {
            if (((String) interests.get(i)).equals(target)) {
                interests.remove(i);  // Professor ArrayBasedList.remove()
                break;
            }
        }
    }
    
    @Override
    public int size() {
        return interests.size();  // O(1) professor implementation
    }
    
    @Override
    public InterestSet intersection(InterestSet other) {
        InterestSet common = new InterestSet();  // Fresh set
        if (other == null) return common;
        
        // Manual linear scan intersection (NO HashSet.retainAll())
        for (int i = 1; i <= interests.size(); i++) {
            String interest = (String) interests.get(i);
            if (other.contains(interest)) {  // O(m) lookup
                common.add(interest);
            }
        }
        return common;
    }
    
    /**
     * Union size for Jaccard denominator: |A ∪ B| = |A| + |B| - |A ∩ B|
     * Called by SocialGraph.calculateScore()
     */
    public int unionSize(InterestSet other) {
        InterestSet union = new InterestSet();
        // Add all from this
        for (int i = 1; i <= size(); i++) {
            union.add((String) interests.get(i));
        }
        // Add all from other (duplicates auto-filtered)
        if (other != null) {
            for (int i = 1; i <= other.size(); i++) {
                union.add((String) other.interests.get(i));
            }
        }
        return union.size();
    }
    
    // Private helper: O(n) linear scan (professor-style)
    private boolean contains(String item) {
        for (int i = 1; i <= interests.size(); i++) {
            if (((String) interests.get(i)).equals(item)) {
                return true;
            }
        }
        return false;
    }
    
    // Package-private for SocialGraph access
    ArrayBasedList getInterests() { 
        return interests; 
    }
}
