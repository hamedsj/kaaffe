package ir.pitok.cafe.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ShopPictureBinding;
import ir.pitok.cafe.databinding.ShowPictureBinding;
import ir.pitok.cafe.viewModels.MapViewModel;
import ir.pitok.cafe.viewModels.ShowPictureViewModel;
import ir.pitok.cafe.viewModels.ShowShopPhotoViewModel;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

import static ir.pitok.cafe.utility.CafeUtilities.dp2Px;

public class ShowPicturesAdapter extends RecyclerView.Adapter<ShowPicturesAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        ShowPictureBinding binding;
        public rowViewHolder(ShowPictureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(ShowPictureViewModel viewModel){
            this.binding.setVM(viewModel);
            this.binding.executePendingBindings();
        }

        public ShowPictureBinding getBinding() {
            return binding;
        }
    }

    private List<ShowPictureViewModel> rows;
    private RecyclerViewClickListener listener;
    public ShowPicturesAdapter(List<ShowPictureViewModel> rows, RecyclerViewClickListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ShowPictureBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_show_pictures, parent,false);
        final rowViewHolder rvh = new rowViewHolder(binding);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(rvh.getAdapterPosition());
            }
        });
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull rowViewHolder holder, int position){
        ShowPictureViewModel row = rows.get(position);
        holder.bind(row);
        Picasso.get().load(row.getLink()).into(holder.binding.ISPImage);
        ViewCompat.setTranslationZ(holder.binding.getRoot(),dp2Px(32));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
