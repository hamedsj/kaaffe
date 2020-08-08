package ir.pitok.cafe.models.pojo;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.pojo.dataModels.ShopDetailDataModel;

public class ShopDetailModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private ShopDetailDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ShopDetailDataModel getData() {
        return data;
    }

    public void setData(ShopDetailDataModel data) {
        this.data = data;
    }

}
