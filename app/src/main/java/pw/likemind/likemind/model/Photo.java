
package pw.likemind.likemind.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("highres_link")
    @Expose
    private String highresLink;
    @SerializedName("photo_id")
    @Expose
    private Integer photoId;
    @SerializedName("photo_link")
    @Expose
    private String photoLink;
    @SerializedName("thumb_link")
    @Expose
    private String thumbLink;

    /**
     * 
     * @return
     *     The highresLink
     */
    public String getHighresLink() {
        return highresLink;
    }

    /**
     * 
     * @param highresLink
     *     The highres_link
     */
    public void setHighresLink(String highresLink) {
        this.highresLink = highresLink;
    }

    /**
     * 
     * @return
     *     The photoId
     */
    public Integer getPhotoId() {
        return photoId;
    }

    /**
     * 
     * @param photoId
     *     The photo_id
     */
    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    /**
     * 
     * @return
     *     The photoLink
     */
    public String getPhotoLink() {
        return photoLink;
    }

    /**
     * 
     * @param photoLink
     *     The photo_link
     */
    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    /**
     * 
     * @return
     *     The thumbLink
     */
    public String getThumbLink() {
        return thumbLink;
    }

    /**
     * 
     * @param thumbLink
     *     The thumb_link
     */
    public void setThumbLink(String thumbLink) {
        this.thumbLink = thumbLink;
    }

}
