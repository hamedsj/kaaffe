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
import ir.pitok.cafe.viewModels.MapViewModel;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;

public class ShopPicturesAdapter extends RecyclerView.Adapter<ShopPicturesAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        ShopPictureBinding binding;
        public rowViewHolder(ShopPictureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MapViewModel viewModel){
            this.binding.setVM(viewModel);
            this.binding.executePendingBindings();
        }

        public ShopPictureBinding getBinding() {
            return binding;
        }
    }

    private List<MapViewModel> rows;
    private RecyclerViewClickListener listener;
    public ShopPicturesAdapter(List<MapViewModel> rows, RecyclerViewClickListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ShopPictureBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_more_pictures, parent,false);
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
        MapViewModel row = rows.get(position);
        holder.bind(row);
        Picasso.get().load(row.getShop_link()).into(holder.binding.IMPImage);
        ViewCompat.setTranslationZ(holder.binding.getRoot(),dp2Px(32));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
