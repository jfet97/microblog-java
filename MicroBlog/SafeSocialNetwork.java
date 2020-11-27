package MicroBlog;

import java.util.HashSet;
import java.util.Set;

import MicroBlog.Exceptions.MicroBlogPostDoesNotExist;
import MicroBlog.Interfaces.UserPostFactory;

public class SafeSocialNetwork extends SocialNetwork {
    // OVERVIEW: il tipo SafeSocialNetwork è un sottotipo del SocialNetwork ed aggiunge
    // la possibilità di segnalare post con contenuti offensivi
    // AF = <userPostFactory, users, posts, reportedPosts> -> <userPostFactory, users, posts>
    // IR = IR(SocialNetwork) && reportedPosts != null && reportedPosts.contains(null) == false
    //      && reportedPosts.contains(id) => this.posts.contains(id)

    Set<String> reportedPosts;

    /*
     * REQUIRES: upf != NULL
     * THROWS: se upf == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inizializza un SafeSocialNetwork
     */
    public SafeSocialNetwork(UserPostFactory upf) {
        super(upf);
        this.reportedPosts = new HashSet<String>();
    }

    /*
     * REQUIRES: postId != NULL && this.posts.contains(postId)
     * THROWS: se postId == NULL lancia una NullPointerException (eccezione unchecked)
     *         se !this.posts.contains(postId) lancia una MicroBlogPostDoesNotExist (eccezione checked)
     * MODIFIES: this
     * EFFECTS: aggiunge postId ai post segnalati
     */
    public void reportPost(String postId) throws MicroBlogPostDoesNotExist {
        if(postId == null) {
            throw new NullPointerException();
        } else if(!this.postBelongsToSocial(postId)) {
            throw new MicroBlogPostDoesNotExist(postId);
        } else {
            this.reportedPosts.add(postId);
        }
    }

    /*
     * REQUIRES: postId != NULL && this.posts.contains(postId)
     * THROWS: se postId == NULL lancia una NullPointerException (eccezione unchecked)
     *         se !this.posts.contains(postId) lancia una MicroBlogPostDoesNotExist (eccezione checked)
     * EFFECTS: restituisce true se il post ricevuto come parametro è stato segnalato, altrimenti false
     */
    public boolean isPostReported(String postId) throws MicroBlogPostDoesNotExist {
        if(postId == null) {
            throw new NullPointerException();
        } else if(!this.postBelongsToSocial(postId)) {
            throw new MicroBlogPostDoesNotExist(postId);
        } else {
            return this.reportedPosts.contains(postId);
        }
    }

}
