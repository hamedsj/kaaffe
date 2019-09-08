package ir.pitok.cafe.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.DrawerBinding;
import ir.pitok.cafe.utility.customs.CafeFragment;
import ir.pitok.cafe.views.Interfaces.LoadAboutFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadMessagesFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadOrdersFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadProfileFragmentListener;

public class DrawerFrag extends CafeFragment {

    private DrawerBinding binding;

    public static DrawerFrag newInstance() {
        Bundle args = new Bundle();
        DrawerFrag fragment = new DrawerFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigation_drawer, container, false);
//        PermissionsViewModel pvm = new PermissionsViewModel(allContext);
//        binding.setPermissionVm(pvm);
        setOnClickListeners();
//        init();
        return binding.getRoot();
    }

    private void init() {
    }

    public void setOnOrdersClick(final LoadOrdersFragmentListener loadOrdersFragmentListener){
        binding.NDOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadOrdersFragmentListener.onLoadOrdersListener();
            }
        });
    }

    public void setOnMessagesClick(final LoadMessagesFragmentListener loadMessagesFragmentListener){
        binding.NDMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMessagesFragmentListener.onLoadMessagesListener();
            }
        });
    }

    public void setOnAboutClick(final LoadAboutFragmentListener loadAboutFragmentListener){
        binding.NDAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAboutFragmentListener.onLoadAboutListener();
            }
        });
    }

    public void setOnProfileClick(final LoadProfileFragmentListener loadProfileFragmentListener){
        binding.NDProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfileFragmentListener.onLoadProfileListener();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners() {
        binding.NDRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        binding.NDExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
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
