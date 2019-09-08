package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.pitok.cafe.models.responseModels.ShopPicturesItemModel;

public class ShopDetailDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("main_picture")
    private String mainPicture;

    @SerializedName("details")
    private String details;

    @SerializedName("work_time")
    private String wrok_time;

    @SerializedName("pictures")
    private List<ShopPicturesItemModel> pictures;

    @SerializedName("menu")
    private List<ItemShopMenuDataModel> menu;

    public List<ItemShopMenuDataModel> getMenu() {
        return menu;
    }

    public void setMenu(List<ItemShopMenuDataModel> menu) {
        this.menu = menu;
    }

    public List<ShopPicturesItemModel> getPictures() {
        return pictures;
    }

    public void setPictures(List<ShopPicturesItemModel> pictures) {
        this.pictures = pictures;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWrok_time() {
        return wrok_time;
    }

    public void setWrok_time(String wrok_time) {
        this.wrok_time = wrok_time;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
