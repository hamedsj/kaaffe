package ir.pitok.cafe.models.pojo;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.pojo.dataModels.NearbyShopsDataModel;

public class NearbyShopsModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private NearbyShopsDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NearbyShopsDataModel getData() {
        return data;
    }

    public void setData(NearbyShopsDataModel data) {
        this.data = data;
    }

}
