package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("orders")
    private List<OrdersItemDataModel> orders;

    public List<OrdersItemDataModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersItemDataModel> orders) {
        this.orders = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
