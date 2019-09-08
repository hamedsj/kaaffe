package ir.pitok.cafe.views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ItemOrderBinding;
import ir.pitok.cafe.viewModels.OrdersViewModel;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

import static ir.pitok.cafe.utility.CafeUtilities.en2prText;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        ItemOrderBinding binding;
        rowViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(OrdersViewModel viewModel){
            this.binding.setVM(viewModel);
            this.binding.executePendingBindings();
        }

        public ItemOrderBinding getBinding() {
            return binding;
        }
    }

    private List<OrdersViewModel> rows;
    private RecyclerViewClickListener listener;
    public OrdersAdapter(List<OrdersViewModel> rows, RecyclerViewClickListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemOrderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_order, parent,false);
        final rowViewHolder rvh = new rowViewHolder(binding);
//        binding.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onClickListener(rvh.getAdapterPosition());
//            }
//        });
        return rvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull rowViewHolder holder, final int position){
        OrdersViewModel row = rows.get(position);
        holder.bind(row);
        holder.binding.IOCount.setText(en2prText(row.getOrders_count() + " عدد"));
        holder.binding.IOAmount.setText(en2prText(row.getPrice() + " تومان"));
        holder.binding.IODate.setText(en2prText(row.getDate()));
        holder.binding.IOLocation.setText(en2prText(row.getShop_name()));
        holder.binding.IOTime.setText(en2prText(row.getTime()));
        holder.binding.IODetails.setOnClickListener(new View.OnClickListener() {
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
