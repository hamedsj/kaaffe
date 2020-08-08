package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.AboutBinding;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.AboutViewModel;
import ir.pitok.cafe.views.Interfaces.BackCallListener;

public class AboutFrag extends CafeFragment {

    private BackCallListener backCallListener;
    private AboutBinding binding;

    public static AboutFrag newInstance(BackCallListener backCallListener) {
        Bundle args = new Bundle();
        AboutFrag fragment = new AboutFrag();
        fragment.setArguments(args);
        fragment.backCallListener = backCallListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        AboutViewModel pvm = new AboutViewModel(allContext);
        binding.setVM(pvm);
        setOnClickListeners();
        init();
        return binding.getRoot();
    }

    private void init() {

    }

    private void setOnClickListeners() {
        binding.ABInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/hamedsj5"));
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/hamedsj5")));
                }
            }
        });

        binding.ABTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=hamedsj5"));
                try {
                    startActivity(intent);
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://t.me/hamedsj5")));
                }
            }
        });

        binding.ABBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCallListener.onBackCall();
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
