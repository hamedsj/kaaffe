package ir.pitok.cafe.viewModels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.BR;

import static ir.pitok.cafe.utility.CafeUtilities.getCornerFromOffset;

public class MapViewModel extends BaseObservable {

    private Context context;
    //###########################
    private int shop_bootomSheetBehaviorState;
    private View shop_bottomSheet;
    private String shop_workTime;
    private String  shop_details;
    private String shop_link;
    private String shop_orders;
    private List<Integer> active_orders = new ArrayList<>();
    //###########################
    private Activity activity;
    //###########################
    private int menu_bootomSheetBehaviorState;
    private View menu_bottomSheet;
    private int menu_item_id;
    private String menu_name;
    private String menu_category;
    private String  menu_price;
    private String menu_link;

    public MapViewModel(Context context) {
        this.context = context;
    }

    public MapViewModel(Context context, View shop_bottomSheet, Activity activity, View menu_bottomSheet) {
        this.context = context;
        this.shop_bottomSheet = shop_bottomSheet;
        this.activity = activity;
        this.menu_bottomSheet = menu_bottomSheet;
    }

    public MapViewModel(String shop_link) {
        this.shop_link = shop_link;
    }

    public MapViewModel(int menu_item_id,String menu_name, String menu_price, String menu_link, String menu_category) {
        this.menu_item_id = menu_item_id;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_link = menu_link;
        this.menu_category = menu_category;
    }

    private void changeShopBottomSheetBehavior() {
        BottomSheetBehavior<View> viewBottomSheetBehavior = BottomSheetBehavior.from(shop_bottomSheet);
        viewBottomSheetBehavior.setState(shop_bootomSheetBehaviorState);
        viewBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                RoundedImageView mainPic = bottomSheet.findViewById(R.id.CSBSMainPicture);
                mainPic.setCornerRadius(getCornerFromOffset(slideOffset));
            }
        });
    }

    private void changeMenuBottomSheetBehavior() {
        BottomSheetBehavior<View> viewBottomSheetBehavior = BottomSheetBehavior.from(menu_bottomSheet);
        viewBottomSheetBehavior.setState(menu_bootomSheetBehaviorState);
        viewBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                TextView see_menu = bottomSheet.findViewById(R.id.SMBSSeeMenu);
                TextView catgory = bottomSheet.findViewById(R.id.SMBSCategoryTv);
                see_menu.setAlpha(1-slideOffset);
                catgory.setAlpha(slideOffset);
            }
        });
    }

    @Bindable
    public int getShop_bootomSheetBehaviorState() {
        return shop_bootomSheetBehaviorState;
    }

    public void setShop_bootomSheetBehaviorState(int shop_bootomSheetBehaviorState) {
        this.shop_bootomSheetBehaviorState = shop_bootomSheetBehaviorState;
        Log.e("Aaaaa",this.shop_bootomSheetBehaviorState+"");
        changeShopBottomSheetBehavior();
    }

    @Bindable
    public int getMenu_bootomSheetBehaviorState() {
        return menu_bootomSheetBehaviorState;
    }

    public void setMenu_bootomSheetBehaviorState(int menu_bootomSheetBehaviorState) {
        this.menu_bootomSheetBehaviorState = menu_bootomSheetBehaviorState;
        changeMenuBottomSheetBehavior();
    }


    @Bindable
    public String getShop_workTime() {
        return shop_workTime;
    }

    public void setWorkTime(String workTime) {
        this.shop_workTime = workTime;
    }

    @Bindable
    public String getShop_details() {
        return shop_details;
    }

    public void setDetails(String details) {
        this.shop_details = details;
    }

    public String getShop_link() {
        return shop_link;
    }

    public void setShop_link(String shop_link) {
        this.shop_link = shop_link;
    }

    @Bindable
    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    @Bindable
    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    @Bindable
    public String getMenu_link() {
        return menu_link;
    }

    public void setMenu_link(String menu_link) {
        this.menu_link = menu_link;
    }

    @Bindable
    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }

    @Bindable
    public int getMenu_item_id() {
        return menu_item_id;
    }

    public void setMenu_item_id(int menu_item_id) {
        this.menu_item_id = menu_item_id;
    }

    @Bindable
    public String getShop_orders() {
        return shop_orders;
    }

    public void setShop_orders(String shop_orders) {
        this.shop_orders = shop_orders;
    }

    public List<Integer> getActive_orders() {
        return active_orders;
    }

    public void setActive_orders(List<Integer> active_orders) {
        this.active_orders = active_orders;
    }

    public int getActive_orders_count() {
        return active_orders.size();
    }

    public void setNew_active_order(Integer item) {
        this.active_orders.add(item);
    }
}
