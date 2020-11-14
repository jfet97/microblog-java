package Tests;

import MicroBlog.Implementations.MicroBlogUser;
import MicroBlog.Interfaces.User;
import MicroBlog.Implementations.MicroBlogPost;
import MicroBlog.Interfaces.Post;
import MicroBlog.Utils.StringMin1Max140;

public class MicroBlogUser_TEST {
    public static void main(String[] args) {

        try {
            User user = new MicroBlogUser("jfet");

            if (user.getName() != "jfet") {
                throw new Exception("getName() returns a wrong username");
            }

            if (user.getFollowers().size() != 0 || user.getFollowing().size() != 0
                    || user.getMentionsInPosts().size() != 0 || user.getLikedPosts().size() != 0
                    || user.getWrittenPosts().size() != 0) {
                throw new Exception("Wrong user initialization");
            }

            User user2 = new MicroBlogUser("mosfet");

            user.follow(user2.getName());
            user2.addFollower(user.getName());
            if (user.getFollowing().size() != 1 || !user.getFollowing().contains(user2.getName())) {
                throw new Exception("follow() does not work properly");
            }
            if (user2.getFollowers().size() != 1 || !user2.getFollowers().contains(user.getName())) {
                throw new Exception("getFollowers() does not work properly");
            }

            if (user.equals(user2) || user2.equals(user)) {
                throw new Exception("equals() does not work properly");
            }
            if (!user.equals(user) || !user2.equals(user2)) {
                throw new Exception("equals() does not work properly");
            }

            Post postOfUser = new MicroBlogPost("jfet", StringMin1Max140.create("Hi mum!").get());

            user.addWrittenPost(postOfUser.getId());
            if (user.getWrittenPosts().size() != 1 || !user.getWrittenPosts().contains(postOfUser.getId())) {
                throw new Exception("addWrittenPost() does not work properly");
            }

            user2.likePost(postOfUser.getId());
            if (user2.getLikedPosts().size() != 1 || !user2.getLikedPosts().contains(postOfUser.getId())) {
                throw new Exception("likePost() does not work properly");
            }

            Post postOfUser2 = new MicroBlogPost("mosfet", StringMin1Max140.create("My name is Dodoino!").get());

            user.addMention(postOfUser2.getId());
            if (user.getMentionsInPosts().size() != 1 || !user.getMentionsInPosts().contains(postOfUser2.getId())) {
                throw new Exception("addMention() does not work properly");
            }

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
        }

    }
}
