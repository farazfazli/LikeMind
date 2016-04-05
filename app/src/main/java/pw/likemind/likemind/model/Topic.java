
package pw.likemind.likemind.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Topic {

    @SerializedName("urlkey")
    @Expose
    private String urlkey;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    /**
     * 
     * @return
     *     The urlkey
     */
    public String getUrlkey() {
        return urlkey;
    }

    /**
     * 
     * @param urlkey
     *     The urlkey
     */
    public void setUrlkey(String urlkey) {
        this.urlkey = urlkey;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
