package Tests;

import MicroBlog.Interfaces.*;
import MicroBlog.Implementations.*;
import MicroBlog.Utils.StringMin1Max140;

public class MicroBlogUserPostFactory_TEST {
    public static void main(String[] args) {

        try {

            // creo la factory
            UserPostFactory upf = new MicroBlogUserPostFactory();

            // creo un utente e un post
            User u = upf.createUser("jfet");
            Post p = upf.createPost(u.getName(), StringMin1Max140.create("Lorem ipsum.").get());

            // testo che la creazione sia avvenuta correttamente
            if(!(u instanceof MicroBlogUser) || !(p instanceof MicroBlogPost)) {
                throw new Exception("MicroBlogUserPostFactory should generate MicroBlog instances");
            }

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
        }

    }
}
