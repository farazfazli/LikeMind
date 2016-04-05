package pw.likemind.likemind.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Self {

    @SerializedName("common")
    @Expose
    private Common common;

    /**
     * 
     * @return
     *     The common
     */
    public Common getCommon() {
        return common;
    }

    /**
     * 
     * @param common
     *     The common
     */
    public void setCommon(Common common) {
        this.common = common;
    }

}
