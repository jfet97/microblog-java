package MicroBlog.Interfaces;

import java.sql.Timestamp;
import java.util.Set;

public interface Post extends Cloneable {
    // OVERVIEW: tipo dei post per un semplice social network
    // TYPICAL ELEMENT: <id, author, text, timestamp, followers, mentions> con 1 <= text.length <= 140


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inserisce user tra i follower del post (in seguito ad esempio ad un like lasciato dall'utente al post)
     */
    public void addFollower(String user);


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inserisce user tra i menzionati (taggati) nel post
     */
    public void addMention(String user);


    /*
     * REQUIRES: user != NULL
     * THROWS: se user == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna true se user è stato menzionato (taggato) nel post, altrimenti ritorna false
     */
    public boolean isUserMentioned(String user);


    /*
     * EFFECTS: ritorna l'id del post
     */
    public String getId();


    /*
     * EFFECTS: ritorna l'id (name) dell'autore del post
     */
    public String getAuthor();


    /*
     * EFFECTS: ritorna il testo del post
     */
    public String getText();


    /*
     * EFFECTS: ritorna la timestamp del post
     */
    public Timestamp getTimestamp();


    /*
     * EFFECTS: ritorna un set contenente gli id (name) dei follower del post
     */
    public Set<String> getFollowers();


    /*
     * EFFECTS: ritorna un set contenente gli id (name) degli utenti menzionati nel post
     */
    public Set<String> getMentions();


    /*
     * EFFECTS: ritorna true se o è di tipo Post e possiede lo stesso id del this, altrimenti ritorna false
     */
    public boolean equals(Object o);

    /*
     * EFFECTS: ritorna l'id del this sottoforma di hashcode
     */
    public int hashCode();


    /*
     * EFFECTS: ritorna un clone del post
     */
    public Post clone();

}
