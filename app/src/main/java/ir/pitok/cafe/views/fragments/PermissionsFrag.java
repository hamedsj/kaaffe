package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.PermissionsBinding;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.PermissionsViewModel;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentListener;

import static ir.pitok.cafe.utils.CafeUtilities.checkPermissions;
import static ir.pitok.cafe.utils.CafeUtilities.getPermissions;

public class PermissionsFrag extends CafeFragment {

    private LoadMapFragmentListener mapListener;

    public static PermissionsFrag newInstance(LoadMapFragmentListener mapListener) {
        Bundle args = new Bundle();
        PermissionsFrag fragment = new PermissionsFrag();
        fragment.setArguments(args);
        fragment.mapListener = mapListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PermissionsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_permissions, container, false);
        PermissionsViewModel pvm = new PermissionsViewModel(allContext);
        binding.setPermissionVm(pvm);
        setOnClickListeners(binding);
        init(binding);
        return binding.getRoot();
    }

    private void init(PermissionsBinding binding) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPermissions(allContext,allActivity);
            }
        },1000);
    }

    private void setOnClickListeners(PermissionsBinding binding) {
        binding.PRAllowBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions(allContext)){
                    getPermissions(allContext,allActivity);
                    return;
                }
                mapListener.onLoadMapFragment();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
