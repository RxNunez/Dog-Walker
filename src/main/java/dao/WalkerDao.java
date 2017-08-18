public interface WalkerDao {

    //create
    void add (Walker walker);
    //read
    List<Walker> getAll();
    //find
    Walker findById(int id);
    //update
    //void update(int id, String content);
    //delete
    //void deleteWalker();
    //void clearAllWalkers();

}