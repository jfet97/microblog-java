package MicroBlog.Implementations;

import java.util.HashSet;

import MicroBlog.Interfaces.User;

public class MicroBlogUser implements User {
    // AF = <name, followers, following, likedPosts, mentionsInPosts, writtenPosts>
    // IR = name != null && followers != null && following != null && likedPosts != null && mentionsInPosts != null && writtenPosts != null
    //      && followers.contains(null) == false && following.contains(null) == false && likedPosts.contains(null) == false
    //      && mentionsInPosts.contains(null) == false && writtenPosts.contains(null) == false

    // il nome dell'utente
    private String name;

    // gli id dei followers dell'utente
    private HashSet<String> followers;

    // gli id dei utenti seguiti
    private HashSet<String> following;

    // gli id dei post ai quali l'utente ha lasciato like
    private HashSet<String> likedPosts;

    // gli id dei post nei quali l'utente è stato menzionato
    private HashSet<String> mentionsInPosts;

    // gli id dei post creati dall'utente
    private HashSet<String> writtenPosts;


    /*
     * REQUIRES: name != NULL
     * THROWS: se name == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inizializza le variabili d'istanza del MicroBlogUser
     */
    public MicroBlogUser(String name) {
        if (name == null)
            throw new NullPointerException();
        else {
            this.name = name;
            this.followers = new HashSet<String>();
            this.following = new HashSet<String>();
            this.likedPosts = new HashSet<String>();
            this.mentionsInPosts = new HashSet<String>();
            this.writtenPosts = new HashSet<String>();
        }
    }


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge user alla lista degli utenti che l'utente this segue
     */
    public void follow(String user) {
        if (user == null)
        throw new NullPointerException();
        else {
            this.following.add(user);
        }
    }


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge user alla lista degli utenti che seguono l'utente this
     */
    public void addFollower(String user) {
        if (user == null)
        throw new NullPointerException();
        else {
            this.followers.add(user);
        }
    }


    /*
     * REQUIRES: post != NULL
     * THROWS: se post == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge post alla lista dei post a cui l'utente this ha messo like
     */
    public void likePost(String post) {
        if (post == null)
        throw new NullPointerException();
        else {
            this.likedPosts.add(post);
        }
    }


    /*
     * REQUIRES: post != NULL
     * THROWS: se post == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge post alla lista dei post nei quali l'utente this è stato menzionato (taggato)
     */
    public void addMention(String post) {
        if (post == null)
        throw new NullPointerException();
        else
        this.mentionsInPosts.add(post);
    }


    /*
     * REQUIRES: post != NULL
     * THROWS: se post == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge post alla lista dei post scritti dall'utente this
     */
    public void addWrittenPost(String post){
        if (post == null)
        throw new NullPointerException();
        else
        this.writtenPosts.add(post);
    }


    /*
     * EFFECTS: ritorna il nome dell'utente
     */
    public String getName() {
        return this.name;
    }


    /*
     * EFFECTS: restituisce gli id (name) dei follower dell'utente this
     */
    public HashSet<String> getFollowers() {
        HashSet<String> clone = new HashSet<String>();
        this.followers.stream().forEach(clone::add);
        return clone;
    }


    /*
     * EFFECTS: restituisce gli id (name) degli utenti seguiti dall'utente this
     */
    public HashSet<String> getFollowing() {
        HashSet<String> clone = new HashSet<String>();
        this.following.stream().forEach(clone::add);
        return clone;
    }


    /*
     * EFFECTS: restituisce gli id dei post a cui l'utente this ha messo like
     */
    public HashSet<String> getLikedPosts() {
        HashSet<String> clone = new HashSet<String>();
        this.likedPosts.stream().forEach(clone::add);
        return clone;
    }


    /*
     * EFFECTS: restituisce gli id dei post nei quali l'utente this è stato menzionato (taggato)
     */
    public HashSet<String> getMentionsInPosts() {
        HashSet<String> clone = new HashSet<String>();
        this.mentionsInPosts.stream().forEach(clone::add);
        return clone;
    }


    /*
     * EFFECTS: restituisce gli id dei post creati dall'utente this
     */
    public HashSet<String> getWrittenPosts() {
        HashSet<String> clone = new HashSet<String>();
        this.writtenPosts.stream().forEach(clone::add);
        return clone;
    }


    /*
     * EFFECTS: ritorna true se o è di tipo User e possiede lo stesso name del this, altrimenti ritorna false
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof User))
            return false;
        else
            return this.name.compareTo(((User) o).getName()) == 0;
    }


    /*
     * EFFECTS: restituisce il name del this sottoforma di hashcode
     */
    public int hashCode() {
        return this.name.hashCode();
    }
}