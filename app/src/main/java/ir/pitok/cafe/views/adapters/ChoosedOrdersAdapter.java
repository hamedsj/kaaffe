package ir.pitok.cafe.views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ItemChoosedOrderBinding;
import ir.pitok.cafe.viewModels.ChoosedOrdersViewModel;
import ir.pitok.cafe.views.Interfaces.DeleteListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

import static ir.pitok.cafe.utils.CafeUtilities.en2prText;

public class ChoosedOrdersAdapter extends RecyclerView.Adapter<ChoosedOrdersAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        ItemChoosedOrderBinding binding;
        rowViewHolder(ItemChoosedOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(ChoosedOrdersViewModel viewModel){
            this.binding.setVM(viewModel);
            this.binding.executePendingBindings();
        }

        public ItemChoosedOrderBinding getBinding() {
            return binding;
        }
    }

    private List<ChoosedOrdersViewModel> rows;
    private RecyclerViewClickListener listener;
    private DeleteListener deleteListener;
    public ChoosedOrdersAdapter(List<ChoosedOrdersViewModel> rows, RecyclerViewClickListener listener, DeleteListener deleteListener) {
        this.rows = rows;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemChoosedOrderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_choosed_order, parent,false);
        final rowViewHolder rvh = new rowViewHolder(binding);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(rvh.getAdapterPosition());
            }
        });
        return rvh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull rowViewHolder holder, final int position){
        ChoosedOrdersViewModel row = rows.get(position);
        holder.bind(row);
        if (position==rows.size()-1) {
            holder.binding.ICODivider.setVisibility(View.INVISIBLE);
        }
        holder.binding.ICOName.setText(row.getName());
        holder.binding.ICOPrice.setText(en2prText( "قیمت : "  + row.getPrice() + " تومان"));
        holder.binding.ICOCount.setText(en2prText(row.getCount() + " عدد"));
        Picasso.get().load(row.getPicture()).into(holder.binding.ICOPicture);
        holder.binding.ICODelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteListener.onDeleteListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
