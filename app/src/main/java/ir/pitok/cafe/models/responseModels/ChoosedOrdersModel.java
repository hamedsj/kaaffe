package ir.pitok.cafe.models.responseModels;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.responseModels.dataModels.ChoosedOrdersDataModel;

public class ChoosedOrdersModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private ChoosedOrdersDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ChoosedOrdersDataModel getData() {
        return data;
    }

    public void setData(ChoosedOrdersDataModel data) {
        this.data = data;
    }

}
