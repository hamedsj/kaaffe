package ir.pitok.cafe.views.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.SearchResultBinding;
import ir.pitok.cafe.viewModels.SearchViewModel;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.rowViewHolder> {

    public class rowViewHolder extends RecyclerView.ViewHolder{
        SearchResultBinding binding;
        public rowViewHolder(SearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(SearchViewModel viewModel){
            this.binding.setSearchResultVM(viewModel);
            this.binding.executePendingBindings();
        }

        public SearchResultBinding getBinding() {
            return binding;
        }
    }

    private List<SearchViewModel> rows;
    private RecyclerViewClickListener listener;
    public SearchResultsAdapter(List<SearchViewModel> rows, RecyclerViewClickListener listener) {
        this.rows = rows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public rowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final SearchResultBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_search_result, parent,false);
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
        SearchViewModel row = rows.get(position);
        holder.bind(row);
        if (position == rows.size()-1) holder.binding.SRLine.setVisibility(View.INVISIBLE);

        if (row.getType().equals("coffee")){
            holder.binding.SRIcon.setImageResource(R.drawable.coffee);
        }else{
            holder.binding.SRIcon.setImageResource(R.drawable.place);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
