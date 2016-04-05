package pw.likemind.likemind.model;

import java.util.ArrayList;

/**
 * Created by Faraz on 3/31/16.
 */
public class MemberIdGroupLink {
    private String memberId;
    private ArrayList<String> groupsList;

    public MemberIdGroupLink() {
    }

    public MemberIdGroupLink(String memberId, ArrayList<String> groupsList) {
        this.memberId = memberId;
        this.groupsList = groupsList;
    }

    public String getMemberId() {
        return memberId;
    }

    public ArrayList<String> getGroupsList() {
        return groupsList;
    }
}