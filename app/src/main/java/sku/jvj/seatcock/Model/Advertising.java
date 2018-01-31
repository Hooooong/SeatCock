package sku.jvj.seatcock.Model;

import java.io.Serializable;

/**
 * Created by Android Hong on 2016-10-18.
 */

public class Advertising implements Serializable {

    private String AdTitle;
    private String AdContent;
    private String AdPicture;

    public String getAdTitle() {
        return AdTitle;
    }
    public void setAdTitle(String adTitle) {
        AdTitle = adTitle;
    }
    public String getAdContent() {
        return AdContent;
    }
    public void setAdContent(String adContent) {
        AdContent = adContent;
    }
    public String getAdPicture() {
        return AdPicture;
    }
    public void setAdPicture(String adPicture) {
        AdPicture = adPicture;
    }
}
