package MicroBlog.Exceptions;

public class MicroBlogUserDoesNotExist extends Exception {
    public MicroBlogUserDoesNotExist() {
        this("");
    }

    public MicroBlogUserDoesNotExist(String errorMessage) {
        super(errorMessage);
    }
}
