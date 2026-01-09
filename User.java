public class User {
    private String id;
    private InterestSet interests;
    
    public User(String id) {
        this.id = id;
        this.interests = new InterestSet();
    }
    
    public void addInterest(String interest) {
        interests.add(interest);
    }
    
    public void removeInterest(String interest) {
        interests.remove(interest);
    }
    
    public InterestSet getInterests() {
        return interests;
    }
    
    public String getId() {
        return id;
    }
}
