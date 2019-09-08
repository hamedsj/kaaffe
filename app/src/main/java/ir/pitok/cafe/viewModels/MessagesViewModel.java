package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;


public class MessagesViewModel extends BaseObservable {

    private Context context;
//    ###########################
    private int message_id;
    private String lable;
    private String text;
    private String link;
    private String date;
    private String time;
//    ###########################
    private List<MessagesViewModel> messages_list;

    public MessagesViewModel(Context context) {
        this.context = context;
        this.messages_list = new ArrayList<>();
    }

    public MessagesViewModel(int message_id, String lable, String text, String link, String date, String time) {
        this.message_id = message_id;
        this.lable = lable;
        this.text = text;
        this.link = link;
        this.date = date;
        this.time = time;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

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

    public List<MessagesViewModel> getMessages_list() {
        return messages_list;
    }

    public void setMessages_list(List<MessagesViewModel> messages_list) {
        this.messages_list = messages_list;
    }

    public void clearMessagesList(){
        messages_list.clear();
    }

    public void addNewItemToOrdersList(MessagesViewModel mvm){
        messages_list.add(mvm);
    }

    public void removeItemFromToMessagesList(int index){
        messages_list.remove(index);
    }

}
