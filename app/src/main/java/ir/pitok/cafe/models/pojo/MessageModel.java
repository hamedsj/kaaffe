package ir.pitok.cafe.models.pojo;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.pojo.dataModels.MessageDataModel;

public class MessageModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private MessageDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MessageDataModel getData() {
        return data;
    }

    public void setData(MessageDataModel data) {
        this.data = data;
    }
}
