package MicroBlog.Exceptions;

public class MicroBlogPostMentionsLocked extends Exception {
    public MicroBlogPostMentionsLocked(String errorMessage) {
        super(errorMessage);
    }
}
