package Tests;

import MicroBlog.SafeSocialNetwork;
import MicroBlog.Implementations.MicroBlogUserPostFactory;
import MicroBlog.Interfaces.UserPostFactory;
import MicroBlog.Utils.StringMin1Max140;

public class SafeSocialNetwork_TEST {
    public static void main(String[] args) {

        try {

            // inizializzo il social network
            UserPostFactory upf = new MicroBlogUserPostFactory();
            SafeSocialNetwork ssn = new SafeSocialNetwork(upf);

            // creo due utenti
            ssn.createUser("user1");
            ssn.createUser("user2");

            // creo un post che verrà segnalato
            String badPost = ssn.createPost("user1", StringMin1Max140.create("bad text").get());

            // faccio il report del post
            ssn.reportPost(badPost);

            // controllo che il post sia stato effetivamente segnalato
            if(!ssn.isPostReported(badPost)) {
                throw new Exception("the report functionality does not work properly");
            }

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
            e.printStackTrace();
        }

    }
}
