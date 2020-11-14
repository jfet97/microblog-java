package MicroBlog.Exceptions;

public class MicroBlogPostDoesNotExist extends Exception {
    public MicroBlogPostDoesNotExist() {
        this("");
    }

    public MicroBlogPostDoesNotExist(String errorMessage) {
        super(errorMessage);
    }
}
