package MicroBlog.Implementations;

import java.sql.Timestamp;
import java.util.HashSet;

import MicroBlog.Interfaces.Post;
import MicroBlog.Utils.StringMin1Max140;
import MicroBlog.Utils.RandomIdGenerator;

public class MicroBlogPost implements Post {
    // AF = <id, author, text, timestamp, followers, mentions>
    // IR = IR(StringMin1Max140) && id != null && author != null && text != null && timestamp != null && followers != null && mentions != null
    //      && followers.contains(null) == false && mentions.contains(null) == false


    // l'id del post
    private String id;

    // l'autore del post
    private String author;

    // il testo del post
    private StringMin1Max140 text;

    // la timestamp del post
    private final Timestamp timestamp;

    // gli id dei follower del post (i.e. chi ha messo like al post)
    private HashSet<String> followers;

    // gli id degli utenti menzionati (taggati) nel post
    private HashSet<String> mentions;


    /*
     * REQUIRES: author != NULL && text != NULL
     * THROWS: se author == NULL || text == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inizializza le variabili d'istanza del MicroBlogPost
     */
    public MicroBlogPost(String author, StringMin1Max140 text) {
        if (author == null || text == null) {
            throw new NullPointerException();
        }

        this.id = RandomIdGenerator.generateId();
        this.author = author;
        this.text = text;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.followers = new HashSet<String>();
        this.mentions = new HashSet<String>();

    }


    /*
     * REQUIRES: author != null && text != null && id != null && timestamp != null && followers != null && mentions != null
     * && followers.contains(null) == false && mentions.contains(null) == false
     * THROWS: se author == null || text == null || id == null || timestamp == null || followers == null || mentions == null
     *         || followers.contains(null) == true || mentions.contains(null) == true
     *         lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inizializza le variabili d'istanza del Post
     * NOTE: costruttore privato utile per forzare il valore di tutte le variabili d'istanza, da utilizzare quando si clona un Post
     */
    private MicroBlogPost(String author, StringMin1Max140 text, String id, Timestamp timestamp,
            HashSet<String> followers, HashSet<String> mentions) {
        if (author == null || text == null || id == null || timestamp == null || followers == null || mentions == null) {
            throw new NullPointerException();
        }

        if(followers.contains(null) || mentions.contains(null)) {
            throw new NullPointerException();
        }

        this.author = author;
        this.text = text;
        this.id = id;
        this.timestamp = timestamp;
        this.followers = followers;
        this.mentions = mentions;
    }


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge un utente all'insieme di coloro che seguono il post (e.g. dopo averci messo like)
     */
    public void addFollower(String user) {
        if (user == null)
            throw new NullPointerException();
        else {
            this.followers.add(user);
        }
    }


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: aggiunge un utente all'insieme di coloro che sono stati menzionati nel post
     */
    public void addMention(String user) {
        this.mentions.add(user);
    }


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: restituisce true se l'utente è stato menzionato nel post, altrimenti false
     */
    public boolean isUserMentioned(String user) {
        if (user == null)
            throw new NullPointerException();
        else
            return this.mentions.contains(user);
    }


    /*
     * EFFECTS: ritorna l'id del post
     */
    public String getId() {
        return this.id;
    }


    /*
     * EFFECTS: ritorna l'id (name) dell'autore del post
     */
    public String getAuthor() {
        return this.author;
    }


    /*
     * EFFECTS: ritorna il testo del post
     */
    public String getText() {
        return this.text.read();
    }


    /*
     * EFFECTS: ritorna la timestamp del post
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }


    /*
     * EFFECTS: ritorna un set contenente gli id (name) dei follower del post
     */
    public HashSet<String> getFollowers() {
        return new HashSet<String>(this.followers);
    }


    /*
     * EFFECTS: ritorna un set contenente gli id (name) degli utenti menzionati nel post
     */
    public HashSet<String> getMentions() {
        return new HashSet<String>(this.mentions);
    }


    /*
     * EFFECTS: ritorna true se o è di tipo Post e possiede lo stesso id del this, altrimenti ritorna false
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Post))
            return false;
        else
            return this.id.compareTo(((Post) o).getId()) == 0;
    }


    /*
     * EFFECTS: ritorna l'id del this sottoforma di hashcode
     */
    public int hashCode() {
        return this.id.hashCode();
    }


    /*
     * EFFECTS: ritorna un clone del post
     */
    public MicroBlogPost clone() {
        return new MicroBlogPost(this.author, this.text, this.id, this.timestamp, this.getFollowers(),
                this.getMentions());
    }

}
