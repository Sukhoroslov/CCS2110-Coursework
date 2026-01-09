public interface InterestSetInterface {
    void add(String item);
    void remove(String item);
    int size();
    InterestSet intersection(InterestSet other);
}
