package ir.pitok.cafe.utils.customs;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

public class CafeFragment extends Fragment {

    protected Context allContext;
    protected Activity allActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        allActivity = getActivity();
        allContext = getContext();
    }

}
