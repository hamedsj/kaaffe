package ir.pitok.cafe.models.pojo.dataModels;

import com.google.gson.annotations.SerializedName;

public class OrdersItemDataModel {

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;


    @SerializedName("shop_name")
    private String shop_name;

    @SerializedName("order_id")
    private int order_id;

    @SerializedName("price")
    private int price;

    @SerializedName("orders_count")
    private int orders_count;


    @SerializedName("shop_id")
    private int shop_id;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrders_count() {
        return orders_count;
    }

    public void setOrders_count(int orders_count) {
        this.orders_count = orders_count;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }
}
