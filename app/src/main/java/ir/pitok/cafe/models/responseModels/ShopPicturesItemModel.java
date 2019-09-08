package ir.pitok.cafe.models.responseModels;

import com.google.gson.annotations.SerializedName;

public class ShopPicturesItemModel {

    @SerializedName("link")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
