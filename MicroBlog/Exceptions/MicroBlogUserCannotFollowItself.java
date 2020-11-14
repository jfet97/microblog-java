package MicroBlog.Exceptions;

public class MicroBlogUserCannotFollowItself extends Exception {

    public MicroBlogUserCannotFollowItself() {
        this("");
    }

    public MicroBlogUserCannotFollowItself(String errorMessage) {
        super(errorMessage);
    }
}
