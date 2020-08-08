package ir.pitok.cafe.models.pojo.dataModels;

import com.google.gson.annotations.SerializedName;

public class ProfileDataModel {
    @SerializedName("message")
    private String message;

    @SerializedName("username")
    private String username;

    @SerializedName("phone_number")
    private String phone_number;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
