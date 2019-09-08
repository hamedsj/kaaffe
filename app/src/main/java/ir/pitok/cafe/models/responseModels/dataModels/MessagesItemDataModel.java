package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

public class MessagesItemDataModel {

    @SerializedName("message_id")
    private int message_id;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("text")
    private String text;

    @SerializedName("lable")
    private String lable;

    @SerializedName("link")
    private String link;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
