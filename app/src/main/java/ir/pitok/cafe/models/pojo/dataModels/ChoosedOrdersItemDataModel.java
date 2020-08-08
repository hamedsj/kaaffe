package ir.pitok.cafe.models.pojo.dataModels;

import com.google.gson.annotations.SerializedName;

public class ChoosedOrdersItemDataModel {

    @SerializedName("name")
    private String name;

    @SerializedName("picture")
    private String picture;

    @SerializedName("item_id")
    private int item_id;

    @SerializedName("price")
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
