package MicroBlog.Interfaces;

import MicroBlog.Utils.StringMin1Max140;

public interface UserPostFactory {
    // OVERVIEW: semplice factory che permette di creare utenti e post


    /*
     * REQUIRES: name != NULL
     * THROWS: se name == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna un nuovo User
     */
    public User createUser(String name);


    /*
     * REQUIRES: authorName != NULL && text != null
     * THROWS: se authorName == NULL || text == null lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna un nuovo Post
     */
    public Post createPost(String authorName, StringMin1Max140 text);
}
