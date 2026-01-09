public class InterestSet implements InterestSetInterface {
    private ArrayBasedList interests = new ArrayBasedList();
    
    public InterestSet() {}
    
    @Override
    public void add(String item) {
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
                interests.remove(i);
                break;
            }
        }
    }
    
    @Override
    public int size() {
        return interests.size();
    }
    
    @Override
    public InterestSet intersection(InterestSet other) {
        InterestSet common = new InterestSet();
        if (other == null) return common;
        for (int i = 1; i <= interests.size(); i++) {
            String interest = (String) interests.get(i);
            if (other.contains(interest)) {
                common.add(interest);
            }
        }
        return common;
    }
    
    public int unionSize(InterestSet other) {
        InterestSet union = new InterestSet();
        for (int i = 1; i <= size(); i++) union.add((String) interests.get(i));
        if (other != null) {
            for (int i = 1; i <= other.size(); i++) union.add((String) other.interests.get(i));
        }
        return union.size();
    }
    
    private boolean contains(String item) {
        for (int i = 1; i <= interests.size(); i++) {
            if (((String) interests.get(i)).equals(item)) return true;
        }
        return false;
    }
    
    private ArrayBasedList getInterests() { return interests; }
}
