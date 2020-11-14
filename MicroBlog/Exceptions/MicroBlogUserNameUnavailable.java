package MicroBlog.Exceptions;

public class MicroBlogUserNameUnavailable extends Exception {
    public MicroBlogUserNameUnavailable(String errorMessage) {
        super(errorMessage);
    }
}
