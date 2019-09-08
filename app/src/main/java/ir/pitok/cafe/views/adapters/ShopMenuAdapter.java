package ir.pitok.cafe.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ShopMenuItemBinding;
import ir.pitok.cafe.viewModels.MapViewModel;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

public class ShopMenuAdapter extends RecyclerView.Adapter<ShopMenuAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        ShopMenuItemBinding binding;
        public rowViewHolder(ShopMenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MapViewModel viewModel){
            this.binding.setVM(viewModel);
            this.binding.executePendingBindings();
        }

        public ShopMenuItemBinding getBinding() {
            return binding;
        }
    }

    private List<MapViewModel> rows;
    private RecyclerViewClickListener listener;
    public ShopMenuAdapter(List<MapViewModel> rows, RecyclerViewClickListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ShopMenuItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_menu, parent,false);
        final rowViewHolder rvh = new rowViewHolder(binding);
//        binding.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onClickListener(rvh.getAdapterPosition());
//            }
//        });
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull rowViewHolder holder, final int position){
        MapViewModel row = rows.get(position);
        holder.bind(row);
        Picasso.get().load(row.getMenu_link()).into(holder.binding.IMImage);
        holder.binding.IMAddBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(position);
            }
        });
        holder.binding.IMName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
