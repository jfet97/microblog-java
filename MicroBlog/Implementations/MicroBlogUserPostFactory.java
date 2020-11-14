package MicroBlog.Implementations;

import MicroBlog.Interfaces.UserPostFactory;
import MicroBlog.Interfaces.Post;
import MicroBlog.Interfaces.User;
import MicroBlog.Utils.StringMin1Max140;

public class MicroBlogUserPostFactory implements UserPostFactory {
    // OVERVIEW: semplice factory che permette di creare MicroBlogUser e MicroBlogPost
    // IR = createUser() instanceof User == true &&  createPost() instanceof Post

    /*
     * REQUIRES: name != NULL
     * THROWS: se name == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna un nuovo MicroBlogUser
     */
    public User createUser(String name) {
        if(name == null) throw new NullPointerException();
        else return new MicroBlogUser(name);
    }


    /*
     * REQUIRES: authorName != NULL && text != null
     * THROWS: se authorName == NULL || text == null lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna un nuovo MicroBlogPost
     */
    public Post createPost(String authorName, StringMin1Max140 text) {
        if(authorName == null || text == null) throw new NullPointerException();
        else return new MicroBlogPost(authorName, text);
    }
}
