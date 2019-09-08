package ir.pitok.cafe.viewModels;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import ir.pitok.cafe.BR;

public class SignUpLoginViewModel extends BaseObservable {
    private Context context;
    private String confirmCodeText="";

    public SignUpLoginViewModel(Context context) {
        this.context = context;
    }

    @Bindable
    public String getConfirmCodeText() {
        return confirmCodeText;
    }

    public void setConfirmCodeText(String confirmCodeText) {
        this.confirmCodeText = confirmCodeText;
        notifyPropertyChanged(BR.confirmCodeText);
    }
}
