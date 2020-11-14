package MicroBlog.Interfaces;

import java.util.Set;

public interface User {
    // OVERVIEW: tipo degli utenti per un semplice social network
    // TYPICAL ELEMENT: <name, followers, following, likedPosts, mentionsInPosts, writtenPosts>


    /*
     * EFFECTS: ritorna il nome dell'utente
     */
    public String getName();


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inserisce user tra gli utenti seguiti dall'utente this
     */
    public void follow(String user);


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS:  inserisce user tra gli utenti che seguono l'utente this
     */
    public void addFollower(String user);


    /*
     * REQUIRES: post != NULL
     * THROWS: se post == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inserisce post tra i post ai quali l'utente this ha messo like
     */
    public void likePost(String post);


    /*
     * REQUIRES: post != NULL
     * THROWS: se post == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inserisce post i post nei quali l'utente this è stato menzionato (taggato)
     */
    public void addMention(String post);


    /*
     * REQUIRES: post != NULL
     * THROWS: se post == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inserisce post tra i post creati dall'utente this
     */
    public void addWrittenPost(String post);


    /*
     * EFFECTS: restituisce gli id (name) dei follower dell'utente this
     */
    public Set<String> getFollowers();


    /*
     * EFFECTS: restituisce gli id (name) degli utenti seguiti dall'utente this
     */
    public Set<String> getFollowing();


    /*
     * EFFECTS: restituisce gli id dei post a cui l'utente this ha messo like
     */
    public Set<String> getLikedPosts();


    /*
     * EFFECTS: restituisce gli id dei post nei quali l'utente this è stato menzionato (taggato)
     */
    public Set<String> getMentionsInPosts();


    /*
     * EFFECTS: restituisce gli id dei post creati dall'utente this
     */
    public Set<String> getWrittenPosts();


    /*
     * EFFECTS: ritorna true se o è di tipo User e possiede lo stesso name del this, altrimenti ritorna false
     */
    public boolean equals(Object o);


    /*
     * EFFECTS: restituisce il name del this sottoforma di hashcode
     */
    public int hashCode();
}