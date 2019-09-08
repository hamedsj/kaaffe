package ir.pitok.cafe.utility.customs;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CafeDialogFragment extends BottomSheetDialogFragment {

    protected Context allContext;
    protected Activity allActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        allActivity = getActivity();
        allContext = getContext();
    }

}
