package ir.pitok.cafe.models.responseModels;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.responseModels.dataModels.OrdersDataModel;

public class OrdersModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private OrdersDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrdersDataModel getData() {
        return data;
    }

    public void setData(OrdersDataModel data) {
        this.data = data;
    }

}
