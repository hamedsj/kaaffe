package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChoosedOrdersDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("orders")
    private List<ChoosedOrdersItemDataModel> orders;

    public List<ChoosedOrdersItemDataModel> getOrders() {
        return orders;
    }

    public void setOrders(List<ChoosedOrdersItemDataModel> orders) {
        this.orders = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
