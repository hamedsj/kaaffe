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
import ir.pitok.cafe.databinding.ItemMessageBinding;
import ir.pitok.cafe.viewModels.MessagesViewModel;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

import static ir.pitok.cafe.utils.CafeUtilities.en2prText;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        ItemMessageBinding binding;
        rowViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MessagesViewModel viewModel){
            this.binding.setVM(viewModel);
            this.binding.executePendingBindings();
        }

        public ItemMessageBinding getBinding() {
            return binding;
        }
    }

    private List<MessagesViewModel> rows;
    private RecyclerViewClickListener listener;
    public MessagesAdapter(List<MessagesViewModel> rows, RecyclerViewClickListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemMessageBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_message, parent,false);
        //        binding.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onClickListener(rvh.getAdapterPosition());
//            }
//        });
        return new rowViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull rowViewHolder holder, final int position){
        MessagesViewModel row = rows.get(position);
        holder.bind(row);
        holder.binding.IMSMessageLable.setText(en2prText(row.getLable()));
        holder.binding.IMSText.setText(en2prText(row.getText()));
        holder.binding.IMSDate.setText(en2prText(row.getDate()));
        holder.binding.IMSTime.setText(en2prText(row.getTime()));
        holder.binding.IMSDetails.setOnClickListener(new View.OnClickListener() {
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
