package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class OrdersViewModel extends BaseObservable {

    private Context context;
//    ###########################
    private int order_id;
    private int price;
    private int orders_count;
    private String shop_name;
    private String date;
    private String time;
//    ###########################
    private List<OrdersViewModel> orders_list;

    public OrdersViewModel(Context context) {
        this.context = context;
        this.orders_list = new ArrayList<>();
    }

    public OrdersViewModel(int order_id, int price, int orders_count, String shop_name, String date, String time) {
        this.order_id = order_id;
        this.price = price;
        this.orders_count = orders_count;
        this.shop_name = shop_name;
        this.date = date;
        this.time = time;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrders_count() {
        return orders_count;
    }

    public void setOrders_count(int orders_count) {
        this.orders_count = orders_count;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
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

    public List<OrdersViewModel> getOrders_list() {
        return orders_list;
    }

    public void setOrders_list(List<OrdersViewModel> orders_list) {
        this.orders_list = orders_list;
    }

    public void clearOrdersList(){
        orders_list.clear();
    }

    public void addNewItemToOrdersList(OrdersViewModel ovm){
        orders_list.add(ovm);
    }

    public void removeItemFromToOrdersList(int index){
        orders_list.remove(index);
    }

}
