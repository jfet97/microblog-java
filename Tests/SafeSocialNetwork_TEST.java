package Tests;

import MicroBlog.SafeSocialNetwork;
import MicroBlog.Implementations.MicroBlogUserPostFactory;
import MicroBlog.Interfaces.UserPostFactory;
import MicroBlog.Utils.StringMin1Max140;

public class SafeSocialNetwork_TEST {
    public static void main(String[] args) {

        try {

            UserPostFactory upf = new MicroBlogUserPostFactory();

            SafeSocialNetwork ssn = new SafeSocialNetwork(upf);

            ssn.createUser("user1");
            ssn.createUser("user2");

            String badPost = ssn.createPost("user1", StringMin1Max140.create("bad text").get());

            ssn.reportPost(badPost);

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
