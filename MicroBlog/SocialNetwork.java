package MicroBlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;

import MicroBlog.Exceptions.*;
import MicroBlog.Interfaces.*;
import MicroBlog.Utils.*;

public class SocialNetwork {
    // OVERVIEW: il tipo SocialNetwork rappresenta un semplice social network dove gli utenti possono
    //           creare post, mettere like ai post, seguire altri utenti ed essere menzionati nei post
    // AF = <userPostFactory, users, posts> -> <userPostFactory, users, posts>
    // IR = IR(Post) && IR(User) && IR(UserPostFactory) <-- assumo le invarianti delle implementazioni concrete di queste interfacce
    //      && users != null && posts != null
    //      && users.containsKey(null) == false && users.containsValue(null) == false
    //      && posts.containsKey(null) == false && posts.containsValue(null) == false
    //      && ∀ i, j ∈ [0, users.entrySet().size()) && i != j && i > j => users.entrySet().toArray()[i].getValue().name != users.entrySet().toArray()[j].getValue().name
    //      && ∀ i, j ∈ [0, posts.entrySet().size()) && i != j && i > j => posts.entrySet().toArray()[i].getValue().id != users.entrySet().toArray()[j].getValue().id
    //      && ∀ u1, u2 ∈ users.values() && u1 != u2 => (
    //          u1.followers.contains(u2.name) <=> u2.following.contains(u1.name)
    //      )
    //      && ∀ u ∈ users.values() => (
    //          u.followers.contains(u.name) == false
    //      )
    //      && ∀ u ∈ users.values(), ∀ p ∈ posts.values() => (
    //          u.likedPosts.contains(p.id) <=> p.followers.contains(u.name) <=> p.author.followers.contains(u) &&
    //          u.mentionsInPosts.contains(p.id) <=> p.mentions.contains(u.name) &&
    //          u.writtenPosts.contains(p.id) <=> p.author == u.name
    //      )


    // la factory che genera user e post
    private UserPostFactory userPostFactory;

    // gli utenti del social network indicizzati tramite name univoco
    private HashMap<String, User> users;

    // gli utenti del social network indicizzati tramite name univoco
    private HashMap<String, Post> posts;


    /*
     * REQUIRES: upf != NULL
     * THROWS: se upf == NULL lancia una NullPointerException (eccezione unchecked)
     * MODIFIES: this
     * EFFECTS: inizializza un SocialNetwork
     */
    public SocialNetwork(UserPostFactory upf) {
        if (upf == null) {
            throw new NullPointerException();
        } else {
            this.userPostFactory = upf;
            this.users = new HashMap<String, User>();
            this.posts = new HashMap<String, Post>();
        }
    }


    /*
     * EFFECTS: ritorna un utente wrappato in un optional se name corrisponde al nome di un utente del social
     *          altrimenti ritorna Optional.empty()
     */
    private Optional<User> getUserByName(String name) {
        return name != null ? Optional.ofNullable(this.users.get(name)) : Optional.empty();
    }


    /*
     * REQUIRES: name != NULL
     * THROWS: se name == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna una mappa <username, Optional<User>>, poiché ogni name ∈ names potrebbe non essere un utente del social network
     */
    private Map<String, Optional<User>> getUsersByName(Set<String> names) {
        if (names == null) {
            throw new NullPointerException();
        } else {
            Map<String, Optional<User>> users = new HashMap<String, Optional<User>>();

            // invoco il metodo d'istanza getUserByName su ogni nome ricevuto in input
            // e aggiungo il risultato nella mappa da restituire
            names.forEach(name -> users.put(name, this.getUserByName(name)));

            return users;
        }
    }


    /*
     * EFFECTS: ritorna un post wrappato in un optional se id corrisponde al'id di un post del social
     *          altrimenti ritorna Optional.empty()
     */
    private Optional<Post> getPostById(String id) {
        return id != null ? Optional.ofNullable(this.posts.get(id)) : Optional.empty();
    }


    /*
     * EFFECTS: ritorna un post clonato e wrappato in un optional se id corrisponde al'id di un utente del social
     *          altrimenti ritorna Optional.empty()
     */
    public Optional<Post> getClonedPostById(String id) {
        // invoco il metodo d'istanza getPostById sull'id ricevuto
        // e mappo il post risultante, se presente, in un suo clone
        return this.getPostById(id).map(p -> p.clone());
    }


    /*
     * REQUIRES: ids != NULL
     * THROWS: se ids == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna una mappa <id, Optional<Post>>, poiché ogni id ∈ ids potrebbe non essere un post del social network
     */
    private Map<String, Optional<Post>> getPostsById(Set<String> ids) {
        if (ids == null) {
            throw new NullPointerException();
        } else {
            Map<String, Optional<Post>> posts = new HashMap<String, Optional<Post>>();

            // invoco il metodo d'istanza getPostById su ogni id ricevuto in input
            // e aggiungo il risultato nella mappa da restituire
            ids.forEach(id -> posts.put(id, this.getPostById(id)));

            return posts;
        }
    }


    /*
     * REQUIRES: name != NULL
     * THROWS: se name == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna true se name è il nome di un utente del social, altrimenti false
     */
    public boolean userBelongsToSocial(String name) {
        if (name == null)
            throw new NullPointerException();
        else
            return this.users.containsKey(name);
    }


    /*
     * REQUIRES: id != NULL
     * THROWS: se id == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna true se id è l'id di un post del social, altrimenti false
     */
    public boolean postBelongsToSocial(String id) {
        if (id == null)
            throw new NullPointerException();
        else
            return this.posts.containsKey(id);
    }


    /*
     * REQUIRES: ps != NULL && ps.contains(null) == false
     * THROWS: se ps == NULL || ps.contains(null) == true lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna la rete sociale derivante dalla lista di Post in ingresso
     */
    public static Map<String, Set<String>> guessFollowers(List<Post> ps) {
        if (ps == null) {
            throw new NullPointerException();
        } else {

            Map<String, Set<String>> socialNetwork = new HashMap<String, Set<String>>();

            // ricavo tutti gli utenti possibili: gli autori dei post, chi è stato menzionato in essi
            // e chi ha lasciato like ad essi
            ps.stream().map(p -> p.getAuthor()).forEach(a -> socialNetwork.put(a, new HashSet<String>()));
            ps.stream().flatMap(p -> p.getFollowers().stream()).filter(u -> !socialNetwork.containsKey(u))
                    .forEach(u -> socialNetwork.put(u, new HashSet<String>()));
            ps.stream().flatMap(p -> p.getMentions().stream()).filter(u -> !socialNetwork.containsKey(u))
                    .forEach(u -> socialNetwork.put(u, new HashSet<String>()));

            // per ogni post, dai suoi followers, ricavo chi segue chi
            // riempiendo di conseguenza la rete social da restituire.
            // map(utente) = set di utenti che esso segue
            // perciò, per ogni utente follower di un post, egli deve avere tra i seguiti (non i seguaci) l'autore del post
            ps.forEach(p -> p.getFollowers().stream().forEach(f -> socialNetwork.get(f).add(p.getAuthor())));

            return socialNetwork;
        }
    }


    /*
     * EFFECTS: ritorna la lista degli utenti (i loro name) che hanno più follower che seguiti
     */
    public List<String> influencers() {

        List<String> influencers = new ArrayList<String>();

        this.users.forEach((userName, user) -> {
            if (user.getFollowers().size() > user.getFollowing().size())
                influencers.add(userName);
        });

        return influencers;

    }


    /*
     * REQUIRES: followers != NULL && followers.containsKey(null) == false && followers.containsValue(null) == false
     *           && ∀ i ∈ [0, followers.entrySet().size()) => followers.entrySet().toArray()[i].getValue() != null
     *           && ∀ i ∈ [0, followers.entrySet().size()) => ∀ u ∈ followers.entrySet().toArray()[i] => followers.containsKey(u) == true
     * THROWS: se sono dei presenti dei null viene lanciata una NullPointerException (eccezione unchecked)
     *         se nei set è presente un utente non appartenente alla rete sociale followers viene lanciata
     *         una MicroBlogUserDoesNotExist exception (eccezione checked)
     * EFFECTS: ritorna la lista degli utenti (i loro name) della rete sociale che hanno più follower che seguiti
     */
    public static List<String> influencers(Map<String, Set<String>> followers) throws MicroBlogUserDoesNotExist {
        if (followers == null) {
            throw new NullPointerException();
        } else {
            // per ogni utente memorizzo la differenza tra il numero dei suoi seguaci e il numero di utenti che egli segue
            Map<String, Integer> follows = new HashMap<String, Integer>();

            List<String> influencers = new ArrayList<String>();

            // inizializzo ogni differenza in follows a 0 per ogni utente trovato come chiave della rete sociale followers
            followers.forEach((u, __) -> follows.put(u, 0));

            // per ogni entry in followers
            for (Map.Entry<String, Set<String>> entry : followers.entrySet()) {
                // estraggo l'utente
                String u = entry.getKey();

                if(u == null) throw new NullPointerException();
                else {

                    // estraggo il set degli utenti che egli segue
                    Set<String> flws = entry.getValue();

                    // per ognuno (f) di questi ultimi (flws)
                    for (String f : flws) {
                        if(f == null) throw new NullPointerException();
                        else if (!follows.containsKey(f)) {
                            // mi aspetto che ogni utente della rete sociale followers compaia come chiave
                            // al limite come chiave avente associata un set vuoto (ovvero un utente che non segue nessun altro utente)
                            throw new MicroBlogUserDoesNotExist(f);
                        } else {
                            // l'utente f è seguito da u, perciò la sua differenza tra seguaci e seguiti aumenta di uno
                            // perché abbiamo un seguace in più
                            follows.compute(f, (__, v) -> v + 1);

                            // l'utente u segue l'utente f, perciò la sua differenza tra seguaci e seguiti diminuisce di uno
                            // perché abbiamo un utente seguito in più
                            follows.compute(u, (__, v) -> v - 1);
                        }
                    }
                }
            }

            // alla fine solo gli utenti aventi più seguaci che seguiti,
            // ovvero gli utenti per i quali la differenza tra seguaci e seguiti è > 0,
            // sono considerabili influencer
            follows.forEach((u, r) -> {
                if (r > 0) influencers.add(u);
            });

            return influencers;
        }
    }


    /*
     * EFFECTS: ritorna tutti gli utenti menzionati (taggati) nella rete sociale
     */
    public Set<String> getMentionedUsers() {

        HashSet<String> allMentionedUsers = new HashSet<String>();

        this.posts.forEach((postId, post) -> {
            post.getMentions().stream().forEach(allMentionedUsers::add);
        });

        return allMentionedUsers;
    }


    /*
     * REQUIRES: ps != NULL && ps.containsKey(null) == false
     * THROWS: se ps == NULL || ps.containsKey(null) == true viene lanciata una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna tutti gli utenti menzionati (taggati) nei post in ps
     */
    public static Set<String> getMentionedUsers(List<Post> ps) {
        if (ps == null) {
            throw new NullPointerException();
        } else {
            HashSet<String> allMentionedUsers = new HashSet<String>();

            for (Post p : ps) {
                p.getMentions().stream().forEach(allMentionedUsers::add);
            }

            return allMentionedUsers;
        }
    }


    /*
     * REQUIRES: name != NULL && this.users.containsKey(name) == true
     * THROWS: se name == NULL viene lanciata una NullPointerException (eccezione unchecked)
     *         se name non è il name di un utente facente parte del social network viene lanciata
     *         una eccezione MicroBlogUserDoesNotExist exception (eccezione checked)
     * EFFECTS: ritorna la lista di post nel social network scritti dall'utente name
     *          purché egli faccia parte del social
     */
    public List<Post> writtenBy(String name) throws MicroBlogUserDoesNotExist {
        if (name == null)
            throw new NullPointerException();
        else {

            User u = this.getUserByName(name)
                        .orElseThrow(() -> new MicroBlogUserDoesNotExist(name));

            List<Post> userPosts = new ArrayList<Post>();

            // se l'utente fa parte del social,
            // sicuramente tutti i suoi post fanno parte del social
            this.getPostsById(u.getWrittenPosts())
                .entrySet().forEach(e -> e.getValue().ifPresent(userPosts::add));

            return userPosts;
        }
    }


    /*
     * REQUIRES: ps != null && ps.contains(null) == false && name != null
     * THROWS: se ps == NULL || ps.contains(null) == true || name == null
     *         viene lanciata una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna la lista di post scritti dall'utente name filtrando la lista ps in ingresso
     */
    public static List<Post> writtenBy(List<Post> ps, String name) {
        if (ps == null || name == null)
            throw new NullPointerException();
        else {
            List<Post> userPost = new ArrayList<Post>();

            // filtro la lista di post in base all'autore
            ps.stream()
                .filter(p -> p.getAuthor() == name)
                .forEach(userPost::add);

            return userPost;

        }
    }


    /*
     * REQUIRES: words != null && words.contains(null) == false
     * THROWS: se words == NULL || words.contains(null) == true
     *         viene lanciata una NullPointerException (eccezione unchecked)
     * EFFECTS: ritorna la lista di post presenti nel social che contengono almeno una delle parole listate in words
     */
    public List<Post> containing(List<String> words) {
        if (words == null)
            throw new NullPointerException();
        else {
            // creo un set con le parole da cercare, in modo che la ricerca sia computazionalmente efficiente
            Set<String> wordsToSearch = words.stream().collect(Collectors.toSet());

            List<Post> posts = new ArrayList<Post>();

            for(Map.Entry<String, Post> entry: this.posts.entrySet()) {
                Post post = entry.getValue();
                // estraggo le parole di ogni post
                String[] postWords = post.getText().split("\\s+");

                for(int i = 0; i < postWords.length; i++) {
                    String word = postWords[i];
                    if(word == null) throw new NullPointerException();
                    else if(wordsToSearch.contains(word)) {
                        // se il post contiene una delle parole ricercate lo inserisco nella lista dei post da tornare
                        // e passo a controllare il post successivo
                        posts.add(post.clone());
                        break;
                    }
                }
            }

            return posts;
        }
    }


    /*
     * REQUIRES: name != null && this.users.containsKey(name) == false
     * THROWS: se name == NULL
     *         viene lanciata una NullPointerException (eccezione unchecked)
     *         se il name scelto è già posseduto da un altro utente della rete
     *         viene lanciata una MicroBlogUserNameUnavailable exception (eccezione checked)
     * MODIFIES: this
     * EFFECTS: crea un nuovo utente nel social network
     */
    public String createUser(String name) throws MicroBlogUserNameUnavailable {
        if (name == null)
            throw new NullPointerException();
        else {
            if (this.getUserByName(name).isPresent()) {
                throw new MicroBlogUserNameUnavailable(name);
            } else {
                User newUser = this.userPostFactory.createUser(name);

                // inserisco il nuovo utente nel social network
                this.users.put(newUser.getName(), newUser);

                return newUser.getName();
            }
        }
    }


    /*
     * REQUIRES: authorName != null && text != null && this.users.containsKey(authorName) == true
     * THROWS: se authorName == null || text == null
     *         viene lanciata una NullPointerException (eccezione unchecked)
     *         se l'autore scelto non è presente all'interno del social network
     *         viene lanciata una MicroBlogUserDoesNotExist exception (eccezione checked)
     * MODIFIES: this
     * EFFECTS: crea un nuovo post nel social network
     */
    public String createPost(String authorName, StringMin1Max140 text)
            throws MicroBlogUserDoesNotExist {
        return this.createPost(authorName, text, new HashSet<String>());
    }



    /*
     * REQUIRES: authorName != null && text != null && mentions != null && mentions.contains(null) == false && this.users.containsKey(authorName) == true
     *           && ∀ u ∈ mentions => this.users.containsKey(u) == true
     * THROWS: se authorName == null || text == null || mentions == null || mentions.contains(null) == true
     *         viene lanciata una NullPointerException (eccezione unchecked)
     *         se l'autore scelto o uno degli utenti taggati non sono presenti all'interno del social network
     *         viene lanciata una MicroBlogUserDoesNotExist exception (eccezione checked)
     * MODIFIES: this
     * EFFECTS: crea un nuovo post nel social network
     */
    public String createPost(String authorName, StringMin1Max140 text, Set<String> mentions)
            throws MicroBlogUserDoesNotExist {
        if (authorName == null || text == null || mentions == null)
            throw new NullPointerException();
        else {
            // ottengo l'autore o, in caso, lancio l'eccezione MicroBlogUserDoesNotExist nel caso egli non esistesse
            Optional<User> maybeAuthor = this.getUserByName(authorName);
            User author = maybeAuthor.orElseThrow(() -> new MicroBlogUserDoesNotExist(authorName));

            // ottengo i vari utenti menzionati, controllando opportunamente
            Map<String, Optional<User>> rawMentionedUsers = this.getUsersByName(mentions);
            HashSet<User> mentionedUsers = new HashSet<User>();

            if(rawMentionedUsers.containsValue(Optional.empty())) {
                // in questo caso un utente menzionato non fa parte del social network (ad esempio è stato menzionato l'utente null)
                throw new MicroBlogUserDoesNotExist();
            }

            // a questo punto è possibile estrarre in sicurezza gli user dagli optional
            rawMentionedUsers.entrySet().stream().map(e -> e.getValue().get()).forEach(mentionedUsers::add);

            // creo un nuovo post impostando autore e testo
            Post newPost = this.userPostFactory.createPost(author.getName(), text);

            // menziono gli utenti
            this.mentionUsersInPost(mentionedUsers, newPost);

            // inserisco il nuovo post in quelli creati da author
            this.addWrittenPostToUser(author, newPost);

            // aggiungo il post nel social network
            this.posts.put(newPost.getId(), newPost);

            return newPost.getId();
        }
    }


    /*
     * REQUIRES: us != null && p != null && us.contains(null) == false
     * THROWS: se us == null || p == null || us.contains(null) == true
     *         viene lanciata una NullPointerException (eccezione unchecked)
     * EFFECTS: aggiunge un set us di utenti negli utenti menzionati di un post p e
     *          aggiunge il post p nel set dei post nei quali ogni utente u ∈ us è menzionato
     */
    private void mentionUsersInPost(Set<User> us, Post p) {
        if (us == null || p == null)
            throw new NullPointerException();
        else {
            for (User u : us) {
                if(u == null) throw new NullPointerException();
                else {
                    p.addMention(u.getName());
                    u.addMention(p.getId());
                }
            }
        }
    }


    /*
     * REQUIRES: u != null && p != null
     * THROWS: se u == null || p == null
     *         viene lanciata una NullPointerException (eccezione unchecked)
     * EFFECTS: aggiunge un post p al set di post creati dall'utente u
     */
    private void addWrittenPostToUser(User u, Post p) {
        if (u == null || p == null)
            throw new NullPointerException();
        else {
            u.addWrittenPost(p.getId());
        }
    }


    /*
     * REQUIRES: user1 != null && user2 != null && this.users.contains(user1) == true && this.users.contains(user2) == true
     * THROWS: se user1 == null || user2 == null
     *         viene lanciata una NullPointerException (eccezione unchecked)
     *         se uno dei due user non appartiene al social network viene lanciata
     *         una MicroBlogUserDoesNotExist exception (eccezione checked)
     * EFFECTS: aggiunge l'utente user2 agli utenti seguiti dall'utente user1 e
     *          aggiunte l'utente user1 ai seguaci dell'utente user2
     */
    private void userFollowAnotherUser(String user1, String user2) throws MicroBlogUserDoesNotExist, MicroBlogUserCannotFollowItself {
        if (user1 == null || user2 == null)
            throw new NullPointerException();
        else if(user1 == user2) {
            throw new MicroBlogUserCannotFollowItself();
        } else {
            // estraggo i due utenti dal social network
            User u1 = this.getUserByName(user1).orElseThrow(() -> new MicroBlogUserDoesNotExist(user1));
            User u2 = this.getUserByName(user2).orElseThrow(() -> new MicroBlogUserDoesNotExist(user2));

            u1.follow(u2.getName());
            u2.addFollower(u1.getName());
        }
    }


    /*
     * REQUIRES: user != null && pId != null && this.users.contains(user) == true && this.posts.contains(pId) == true
     *           && user != this.posts.get(pId).getAuthor()
     * THROWS: se user == null || pId == null
     *         viene lanciata una NullPointerException (eccezione unchecked)
     *         se l'user non appartiene al social network viene lanciata
     *         una MicroBlogUserDoesNotExist exception (eccezione checked)
     *         se il post non appartiene al social network viene lanciata
     *         una MicroBlogPostDoesNotExist exception (eccezione checked)
     *         se l'autore del post è l'user viene lanciata una
     *         una MicroBlogUserCannotFollowItself exception (eccezione checked)
     * EFFECTS: inserisce il post nei post ai quali l'utente ha messo like,
     *          inserisce l'utente negli utenti che hanno messo like (che seguono) il post e
     *          imposta una relazione follower-following tra lo user e l'autore del post
     */
    public void userLikeAPost(String user, String pId)
            throws MicroBlogUserDoesNotExist, MicroBlogUserCannotFollowItself, MicroBlogPostDoesNotExist {
        if (user == null || pId == null)
            throw new NullPointerException();
        else {
            // estraggo utente e post dal social network
            User u = this.getUserByName(user).orElseThrow(() -> new MicroBlogUserDoesNotExist(user));
            Post p = this.getPostById(pId).orElseThrow(() -> new MicroBlogPostDoesNotExist(pId));

            if (p.getAuthor() == u.getName()) {
                throw new MicroBlogUserCannotFollowItself(u.getName());
            } else {
                // inserisco il post p nei post ai quali l'utente u ha messo like
                u.likePost(p.getId());

                // inserisco l'utente u negli utenti che hanno messo like (che seguono) il post p
                p.addFollower(u.getName());

                // l'utente u deve quindi seguire l'autore del post p
                this.userFollowAnotherUser(u.getName(), p.getAuthor());
            }
        }
    }
}
