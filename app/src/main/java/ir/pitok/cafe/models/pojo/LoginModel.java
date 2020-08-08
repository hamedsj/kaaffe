package ir.pitok.cafe.models.pojo;

import com.google.gson.annotations.SerializedName;

import ir.pitok.cafe.models.pojo.dataModels.TokenDataModel;

public class LoginModel {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private TokenDataModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TokenDataModel getData() {
        return data;
    }

    public void setData(TokenDataModel data) {
        this.data = data;
    }
}
