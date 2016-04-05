package pw.likemind.likemind.model;

/**
 * Created by Faraz on 4/4/16.
 */
public class MemberInfo {
    private String name;
    private String memberId;
    private String thumbnailLink;
    public MemberInfo() {
    }

    public MemberInfo(String name, String memberId, String thumbnailLink) {
        this.name = name;
        this.memberId = memberId;
        this.thumbnailLink = thumbnailLink;
    }

    public String getName() {return name;}

    public String getMemberId() {
        return memberId;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }
}
