package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;

public class MainViewModel extends BaseObservable {
    private Context context;
    public MainViewModel(Context context) {
        this.context = context;
    }
}
