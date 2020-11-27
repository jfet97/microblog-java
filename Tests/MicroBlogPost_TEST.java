package Tests;

import MicroBlog.Implementations.MicroBlogUser;
import MicroBlog.Interfaces.User;
import MicroBlog.Implementations.MicroBlogPost;
import MicroBlog.Interfaces.Post;
import MicroBlog.Utils.StringMin1Max140;

public class MicroBlogPost_TEST {
    public static void main(String[] args) {

        try {

            // creo alcuni utenti
            User user = new MicroBlogUser("jfet");
            User user2 = new MicroBlogUser("mosfet");
            User user3 = new MicroBlogUser("bjt");

            // creo un post
            Post userPost = new MicroBlogPost(user.getName(), StringMin1Max140.create("Hello World!").get());

            // controllo che l'inizializzazione del post abbia avuto successo
            if (userPost.getId() == null || userPost.getTimestamp() == null || userPost.getAuthor() != user.getName()
                    || userPost.getFollowers().size() != 0 || userPost.getMentions().size() != 0) {
                throw new Exception("Wrong post initialization");
            }

            // controllo che la menzione degli utenti avvenga in modo corretto
            userPost.addMention(user2.getName());

            if (userPost.getMentions().size() != 1 || !userPost.getMentions().contains(user2.getName())) {
                throw new Exception("addMention() does not work properly");
            }

            if (userPost.isUserMentioned(user.getName()) || !userPost.isUserMentioned(user2.getName())
                    || userPost.isUserMentioned(user3.getName())) {
                throw new Exception("isUserMentioned() does not work properly");
            }

            // controllo che il sistema di following (like) avvenga in modo corretto
            userPost.addFollower(user3.getName());

            if (userPost.getFollowers().size() != 1 || !userPost.getFollowers().contains(user3.getName())) {
                throw new Exception("getFollowers() does not work properly");
            }

            // controllo del metodo equals
            if (!userPost.equals(userPost)) {
                throw new Exception("equals() does not work properly");
            }

            // controllo del metodo clone
            Post userPostCloned = userPost.clone();

            if (!userPost.equals(userPostCloned)) {
                throw new Exception("equals() does not work properly");
            }

            if (userPost.getId() != userPostCloned.getId() || userPost.getTimestamp() != userPostCloned.getTimestamp()
                    || userPost.getAuthor() != userPostCloned.getAuthor()
                    || userPost.getText() != userPostCloned.getText()
                    || !userPostCloned.isUserMentioned(user2.getName())
                    || !userPostCloned.getFollowers().contains(user3.getName())) {
                throw new Exception("clone() does not work properly");
            }

            System.out.println("MicroBlogPost: OK");

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
        }

    }
}
