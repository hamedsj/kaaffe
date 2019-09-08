package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

public class ChoosedOrdersViewModel extends BaseObservable {
    private Context context;
    private List<Integer> itemsId;
//  #################################
    private int item_id;
    private String name;
    private String picture;
    private int count;
    private int price;
//  #################################
    private List<ChoosedOrdersViewModel> ordersList = new ArrayList<>();

    public ChoosedOrdersViewModel(Context context, List<Integer> itemsId) {
        this.context = context;
        this.itemsId = itemsId;
    }

    public List<Integer> getItemsId() {
        return itemsId;
    }

    public ChoosedOrdersViewModel(int item_id, String name, String picture, int price, int count) {
        this.item_id = item_id;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.count = count;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ChoosedOrdersViewModel> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<ChoosedOrdersViewModel> ordersList) {
        this.ordersList = ordersList;
    }

    public void addNewOrderToOrdersList(ChoosedOrdersViewModel o) {
        this.ordersList.add(o);
    }

    public void changeOrderCountInOrdersList(int index, int count) {
        this.ordersList.set(index,new ChoosedOrdersViewModel(this.ordersList.get(index).item_id, this.ordersList.get(index).name, this.ordersList.get(index).picture, this.ordersList.get(index).price, count));
    }

    public void deleteOrdeerFromOrdersList(int index) {
        this.ordersList.remove(index);
    }


}
