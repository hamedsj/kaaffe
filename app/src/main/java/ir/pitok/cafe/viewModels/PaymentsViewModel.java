package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;

import java.util.List;

public class PaymentsViewModel extends BaseObservable {
    private Context context;
    private List<Integer> itemsId;

    public PaymentsViewModel(Context context, List<Integer> itemsId) {
        this.context = context;
        this.itemsId = itemsId;
    }

    public List<Integer> getItemsId() {
        return itemsId;
    }
}
