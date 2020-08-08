package ir.pitok.cafe.models.pojo.dataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessagesDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("messages")
    private List<MessagesItemDataModel> messages;

    public List<MessagesItemDataModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesItemDataModel> messages) {
        this.messages = messages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
