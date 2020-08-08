package ir.pitok.cafe.models.pojo.dataModels;

import com.google.gson.annotations.SerializedName;

public class TokenDataModel {
    @SerializedName("api_token")
    private String api_token;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}
