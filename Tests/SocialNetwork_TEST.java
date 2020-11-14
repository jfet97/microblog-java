package Tests;

import MicroBlog.Interfaces.Post;
import MicroBlog.Interfaces.UserPostFactory;
import MicroBlog.Utils.StringMin1Max140;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import MicroBlog.SocialNetwork;
import MicroBlog.Exceptions.MicroBlogUserNameUnavailable;
import MicroBlog.Implementations.MicroBlogUserPostFactory;

public class SocialNetwork_TEST {
    public static void main(String[] args) {

        UserPostFactory upf = new MicroBlogUserPostFactory();

        SocialNetwork sn = new SocialNetwork(upf);

        try {

            final String USER_NOT_PRESENT = "USER_NOT_PRESENT";
            final String POST_NOT_PRESENT = "POST_NOT_PRESENT";
            StringMin1Max140 blogPost = StringMin1Max140.create("A nice microblog post.").get();
            StringMin1Max140 blogPost2 = StringMin1Max140.create("Another nice microblog post.").get();

            if (sn.getClonedPostById(POST_NOT_PRESENT).isPresent()) {
                throw new Exception(
                        "A post that is not present inside the social seems to be there. The method getClonedPostById() is wrong.");
            }

            if (sn.userBelongsToSocial(USER_NOT_PRESENT)) {
                throw new Exception("A user that is not present inside the social seems to be there.");
            }

            String jfet;
            try {
                jfet = sn.createUser("jfet");
            } catch (MicroBlogUserNameUnavailable e) {
                throw new Exception("it should not throw MicroBlogUserNameUnavailable");
            }

            if (!sn.userBelongsToSocial(jfet)) {
                throw new Exception("A user that is present inside the social seems not to be there.");
            }

            String jfetPost;
            try {
                jfetPost = sn.createPost(jfet, blogPost);
            } catch (Exception e) {
                throw new Exception("it should not throw MicroBlogUserNameUnavailable nor MicroBlogUserDoesNotExist");
            }

            if (!sn.postBelongsToSocial(jfetPost)) {
                throw new Exception("A post that is present inside the social seems not to be there.");
            }

            List<Post> jfetPostList = sn.writtenBy(jfet);
            Post jfetPostFromSN = jfetPostList.get(0);

            if (jfetPostFromSN.getId() != jfetPost) {
                throw new Exception("writtenBy() does not work properly");
            }

            String user1 = sn.createUser("user1");
            String user2 = sn.createUser("user2");
            String user3 = sn.createUser("user3");
            String user4 = sn.createUser("user4");
            String user5 = sn.createUser("user5");
            String user6 = sn.createUser("user6");
            String user7 = sn.createUser("user7");

            HashSet<String> user1Mentions = new HashSet<String>();
            user1Mentions.add(user2);
            user1Mentions.add(user3);
            HashSet<String> user6Mentions = new HashSet<String>();
            user6Mentions.add(user6);
            user6Mentions.add(user7);

            String user1Post1 = sn.createPost(user1, blogPost, user1Mentions);
            String user4Post1 = sn.createPost(user4, blogPost);
            String user6Post1 = sn.createPost(user6, blogPost, user6Mentions);
            String user6Post2 = sn.createPost(user6, blogPost2);

            sn.userLikeAPost(user4, user1Post1);
            sn.userLikeAPost(user5, user1Post1);

            sn.userLikeAPost(user1, user4Post1);
            sn.userLikeAPost(user2, user4Post1);
            sn.userLikeAPost(user3, user4Post1);
            sn.userLikeAPost(user5, user4Post1);

            sn.userLikeAPost(user7, user6Post1);

            sn.userLikeAPost(user1, user6Post2);
            sn.userLikeAPost(user2, user6Post2);
            sn.userLikeAPost(user3, user6Post2);
            sn.userLikeAPost(user4, user6Post2);
            sn.userLikeAPost(user5, user6Post2);
            sn.userLikeAPost(user7, user6Post2);

            // map[a] definisce l’insieme delle persone seguite nella rete sociale
            // dall’utente a.

            List<Post> postList1 = new ArrayList<Post>();
            postList1.addAll(sn.writtenBy(user1));
            postList1.addAll(sn.writtenBy(user4));
            postList1.addAll(sn.writtenBy(user6));
            Map<String, Set<String>> derivedSocialNetwork1 = SocialNetwork.guessFollowers(postList1);

            boolean dsn1T = true;
            dsn1T = dsn1T && !derivedSocialNetwork1.containsKey(jfet);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user1).contains(user4);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user1).contains(user6);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user2).contains(user4);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user2).contains(user6);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user3).contains(user4);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user3).contains(user6);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user4).contains(user1);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user4).contains(user6);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user5).contains(user1);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user5).contains(user4);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user5).contains(user6);
            dsn1T = dsn1T && derivedSocialNetwork1.get(user6).isEmpty();
            dsn1T = dsn1T && derivedSocialNetwork1.get(user7).contains(user6);

            // System.out.println(derivedSocialNetwork1);
            if (!dsn1T) {
                throw new Exception("static guessFollowers() does not work properly - 1");
            }

            List<Post> postList2 = new ArrayList<Post>();
            postList2.addAll(sn.writtenBy(user1));
            postList2.addAll(sn.writtenBy(user4));
            Map<String, Set<String>> derivedSocialNetwork2 = SocialNetwork.guessFollowers(postList2);

            boolean dsn2T = true;
            dsn2T = dsn2T && !derivedSocialNetwork2.containsKey(jfet);
            dsn2T = dsn2T && derivedSocialNetwork2.get(user1).contains(user4);
            dsn2T = dsn2T && derivedSocialNetwork2.get(user2).contains(user4);
            dsn2T = dsn2T && derivedSocialNetwork2.get(user3).contains(user4);
            dsn2T = dsn2T && derivedSocialNetwork2.get(user4).contains(user1);
            dsn2T = dsn2T && derivedSocialNetwork2.get(user5).contains(user1);
            dsn2T = dsn2T && derivedSocialNetwork2.get(user5).contains(user4);
            dsn2T = dsn2T && !derivedSocialNetwork2.containsKey(user6);
            dsn2T = dsn2T && !derivedSocialNetwork2.containsKey(user7);

            // System.out.println(derivedSocialNetwork2);
            if (!dsn2T) {
                throw new Exception("static guessFollowers() does not work properly - 2");
            }

            Post user1Post1Cloned = sn.getClonedPostById(user1Post1).orElseThrow(() -> new Exception(
                    "A post that is present inside the social seems not to be there. The method getClonedPostById() is wrong."));
            if (user1Post1Cloned.getId() != user1Post1) {
                throw new Exception("getClonedPostById() does not work properly");
            }

            boolean mF = true;
            Set<String> mentions = sn.getMentionedUsers();
            // System.out.println(mentions);
            mF = mF && mentions.size() == 4;
            mF = mF && mentions.contains(user2);
            mF = mF && mentions.contains(user3);
            mF = mF && mentions.contains(user6);
            mF = mF && mentions.contains(user7);

            if (!mF) {
                throw new Exception("getMentionedUsers() does not work properly - 1");
            }

            boolean mF1 = true;
            Set<String> mentions1 = SocialNetwork.getMentionedUsers(postList1);
            // System.out.println(mentions1);
            mF1 = mF1 && mentions1.size() == 4;
            mF1 = mF1 && mentions1.contains(user2);
            mF1 = mF1 && mentions1.contains(user3);
            mF1 = mF1 && mentions1.contains(user6);
            mF1 = mF1 && mentions1.contains(user7);

            if (!mF1) {
                throw new Exception("static getMentionedUsers() does not work properly - 2");
            }

            boolean mF2 = true;
            Set<String> mentions2 = SocialNetwork.getMentionedUsers(postList2);
            // System.out.println(mentions2);
            mF2 = mF2 && mentions2.size() == 2;
            mF2 = mF2 && mentions2.contains(user2);
            mF2 = mF2 && mentions2.contains(user3);

            if (!mF2) {
                throw new Exception("static getMentionedUsers() does not work properly - 3");
            }

            boolean iF = true;
            List<String> influencers = sn.influencers();
            // System.out.println(influencers);
            iF = iF && influencers.size() == 2;
            iF = iF && influencers.contains(user4);
            iF = iF && influencers.contains(user6);

            if (!iF) {
                throw new Exception("influencers() does not work properly - 1");
            }

            boolean iF1 = true;
            List<String> influencers1 = SocialNetwork.influencers(derivedSocialNetwork1);
            // System.out.println(influencers1);
            iF1 = iF1 && influencers1.size() == 2;
            iF1 = iF1 && influencers1.contains(user4);
            iF1 = iF1 && influencers1.contains(user6);

            if (!iF1) {
                throw new Exception("static influencers() does not work properly - 2");
            }

            boolean iF2 = true;
            List<String> influencers2 = SocialNetwork.influencers(derivedSocialNetwork2);
            // System.out.println(influencers2);
            iF2 = iF2 && influencers2.size() == 2;
            iF2 = iF2 && influencers2.contains(user1);
            iF2 = iF2 && influencers2.contains(user4);

            if (!iF2) {
                throw new Exception("static influencers() does not work properly - 3");
            }

            boolean wB_a = true;
            List<Post> wB1 = sn.writtenBy(user1);
            List<Post> wB2 = sn.writtenBy(user2);
            List<Post> wB3 = sn.writtenBy(user3);
            List<Post> wB4 = sn.writtenBy(user4);
            List<Post> wB5 = sn.writtenBy(user5);
            List<Post> wB6 = sn.writtenBy(user6);
            List<Post> wB7 = sn.writtenBy(user7);

            wB_a = wB_a && wB1.size() == 1;
            wB_a = wB_a && wB2.size() == 0;
            wB_a = wB_a && wB3.size() == 0;
            wB_a = wB_a && wB4.size() == 1;
            wB_a = wB_a && wB5.size() == 0;
            wB_a = wB_a && wB6.size() == 2;
            wB_a = wB_a && wB7.size() == 0;
            wB_a = wB_a && wB1.contains(sn.getClonedPostById(user1Post1).get());
            wB_a = wB_a && wB4.contains(sn.getClonedPostById(user4Post1).get());
            wB_a = wB_a && wB6.contains(sn.getClonedPostById(user6Post1).get());
            wB_a = wB_a && wB6.contains(sn.getClonedPostById(user6Post1).get());

            if (!wB_a) {
                throw new Exception("writtenBy() does not work properly - 1");
            }

            boolean wB_1 = true;
            List<Post> wB1_1 = SocialNetwork.writtenBy(postList1, user1);
            List<Post> wB2_1 = SocialNetwork.writtenBy(postList1, user2);
            List<Post> wB3_1 = SocialNetwork.writtenBy(postList1, user3);
            List<Post> wB4_1 = SocialNetwork.writtenBy(postList1, user4);
            List<Post> wB5_1 = SocialNetwork.writtenBy(postList1, user5);
            List<Post> wB6_1 = SocialNetwork.writtenBy(postList1, user6);
            List<Post> wB7_1 = SocialNetwork.writtenBy(postList1, user7);

            wB_1 = wB_1 && wB1_1.size() == 1;
            wB_1 = wB_1 && wB2_1.size() == 0;
            wB_1 = wB_1 && wB3_1.size() == 0;
            wB_1 = wB_1 && wB4_1.size() == 1;
            wB_1 = wB_1 && wB5_1.size() == 0;
            wB_1 = wB_1 && wB6_1.size() == 2;
            wB_1 = wB_1 && wB7_1.size() == 0;
            wB_1 = wB_1 && wB1.contains(sn.getClonedPostById(user1Post1).get());
            wB_1 = wB_1 && wB4.contains(sn.getClonedPostById(user4Post1).get());
            wB_1 = wB_1 && wB6.contains(sn.getClonedPostById(user6Post1).get());
            wB_1 = wB_1 && wB6.contains(sn.getClonedPostById(user6Post1).get());

            if (!wB_1) {
                throw new Exception("static writtenBy() does not work properly - 2");
            }

            boolean wB_2 = true;
            List<Post> wB1_2 = SocialNetwork.writtenBy(postList2, user1);
            List<Post> wB2_2 = SocialNetwork.writtenBy(postList2, user2);
            List<Post> wB3_2 = SocialNetwork.writtenBy(postList2, user3);
            List<Post> wB4_2 = SocialNetwork.writtenBy(postList2, user4);
            List<Post> wB5_2 = SocialNetwork.writtenBy(postList2, user5);
            List<Post> wB6_2 = SocialNetwork.writtenBy(postList2, user6);
            List<Post> wB7_2 = SocialNetwork.writtenBy(postList2, user7);

            wB_2 = wB_2 && wB1_2.size() == 1;
            wB_2 = wB_2 && wB2_2.size() == 0;
            wB_2 = wB_2 && wB3_2.size() == 0;
            wB_2 = wB_2 && wB4_2.size() == 1;
            wB_2 = wB_2 && wB5_2.size() == 0;
            wB_2 = wB_2 && wB6_2.size() == 0;
            wB_2 = wB_2 && wB7_2.size() == 0;
            wB_2 = wB_2 && wB1.contains(sn.getClonedPostById(user1Post1).get());
            wB_2 = wB_2 && wB4.contains(sn.getClonedPostById(user4Post1).get());

            if (!wB_2) {
                throw new Exception("static writtenBy() does not work properly - 3");
            }

            List<String> lw1 = new ArrayList<String>();
            lw1.add("A");
            lw1.add("Another");
            List<Post> lw1R = sn.containing(lw1);

            boolean lw1RB = true;
            lw1RB = lw1RB && lw1R.size() == 5;
            lw1RB = lw1RB && lw1R.contains(sn.getClonedPostById(jfetPost).get());
            lw1RB = lw1RB && lw1R.contains(sn.getClonedPostById(user1Post1).get());
            lw1RB = lw1RB && lw1R.contains(sn.getClonedPostById(user4Post1).get());
            lw1RB = lw1RB && lw1R.contains(sn.getClonedPostById(user6Post1).get());
            lw1RB = lw1RB && lw1R.contains(sn.getClonedPostById(user6Post2).get());

            List<String> lw2 = new ArrayList<String>();
            lw2.add("A");
            List<Post> lw2R = sn.containing(lw2);

            boolean lw2RB = true;
            lw2RB = lw2RB && lw2R.size() == 4;
            lw2RB = lw2RB && lw2R.contains(sn.getClonedPostById(jfetPost).get());
            lw2RB = lw2RB && lw2R.contains(sn.getClonedPostById(user1Post1).get());
            lw2RB = lw2RB && lw2R.contains(sn.getClonedPostById(user4Post1).get());
            lw2RB = lw2RB && lw2R.contains(sn.getClonedPostById(user6Post1).get());

            List<String> lw3 = new ArrayList<String>();
            lw3.add("Another");
            List<Post> lw3R = sn.containing(lw3);

            boolean lw3RB = true;
            lw3RB = lw3RB && lw3R.size() == 1;
            lw3RB = lw3RB && lw3R.contains(sn.getClonedPostById(user6Post2).get());

            List<String> lw4 = new ArrayList<String>();
            List<Post> lw4R = sn.containing(lw4);

            boolean lw4RB = true;
            lw4RB = lw4RB && lw4R.size() == 0;

            if (!lw1RB || !lw2RB || !lw3RB || !lw4RB) {
                throw new Exception("containing() does not work properly");
            }

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
            e.printStackTrace();
        }

    }
}
