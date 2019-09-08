package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import ir.pitok.cafe.BR;

public class PermissionsViewModel extends BaseObservable {
    private Context context;

    public PermissionsViewModel(Context context) {
        this.context = context;
    }

}
