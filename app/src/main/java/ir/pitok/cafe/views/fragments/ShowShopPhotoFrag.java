package ir.pitok.cafe.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ShowShopPhotoBinding;
import ir.pitok.cafe.utility.customs.CafeFragment;
import ir.pitok.cafe.viewModels.ShowPictureViewModel;
import ir.pitok.cafe.views.Interfaces.BackCallListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.adapters.ShowPicturesAdapter;

public class ShowShopPhotoFrag extends CafeFragment {

    private ShowShopPhotoBinding binding;
    private int position;
    private List<ShowPictureViewModel> list;
    private ShowPicturesAdapter adapter;
    private BackCallListener backCallListener;
    private int activePosition;

    public static ShowShopPhotoFrag newInstance(List<ShowPictureViewModel> list, int position,BackCallListener backCallListener) {
        Bundle args = new Bundle();
        ShowShopPhotoFrag fragment = new ShowShopPhotoFrag();
        fragment.setArguments(args);
        fragment.position = position;
        fragment.list = list;
        fragment.backCallListener = backCallListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_shop_photo, container, false);
        setListeners();
        init();
        return binding.getRoot();
    }

    private void init() {
        LinearLayoutManager llm = new LinearLayoutManager(allContext,LinearLayoutManager.HORIZONTAL,true);
        binding.SSPPictures.setLayoutManager(llm);
        Picasso.get().load(list.get(position).getLink()).into(binding.SSPImage);
        activePosition = position;
        setPicturesAdapter();
    }

    private void setPicturesAdapter() {
        adapter = new ShowPicturesAdapter(list, new RecyclerViewClickListener() {
            @Override
            public void onClickListener(final int position) {
                if (activePosition==position)return;
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        YoYo.with(Techniques.FadeOut).duration(200).playOn(binding.SSPImage);
                        binding.SSPImage.setImageBitmap(bitmap);
                        YoYo.with(Techniques.FadeIn).duration(200).playOn(binding.SSPImage);
                        activePosition = position;

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                binding.SSPImage.setTag(target);
                Picasso.get().load(list.get(position).getLink()).into(target);
            }
        });
        binding.SSPPictures.setAdapter(adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
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
