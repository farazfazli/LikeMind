package pw.likemind.likemind;

/**
 * Created by Faraz on 3/24/16.
 */
public class Message {
    String author;
    String message;

    public Message() {

    }

    public Message(String author, String message) {
        this.author = author;
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
