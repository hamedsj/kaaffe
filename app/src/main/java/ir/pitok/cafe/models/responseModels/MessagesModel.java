package ir.pitok.cafe.models.responseModels;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.responseModels.dataModels.MessagesDataModel;
import ir.pitok.cafe.models.responseModels.dataModels.OrdersDataModel;

public class MessagesModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private MessagesDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MessagesDataModel getData() {
        return data;
    }

    public void setData(MessagesDataModel data) {
        this.data = data;
    }

}
