package ir.pitok.cafe.models.pojo;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.pojo.dataModels.ProfileDataModel;

public class ProfileModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private ProfileDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ProfileDataModel getData() {
        return data;
    }

    public void setData(ProfileDataModel data) {
        this.data = data;
    }
}
