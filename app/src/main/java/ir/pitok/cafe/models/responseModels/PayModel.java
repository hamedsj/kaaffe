package ir.pitok.cafe.models.responseModels;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.responseModels.dataModels.PayDataModel;

public class PayModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private PayDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PayDataModel getData() {
        return data;
    }

    public void setData(PayDataModel data) {
        this.data = data;
    }

}
