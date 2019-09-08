package ir.pitok.cafe.viewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.pitok.cafe.BR;
import ir.pitok.cafe.databinding.SearchBinding;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.Interfaces.SetAdapterListener;
import ir.pitok.cafe.views.adapters.SearchResultsAdapter;
import ir.pitok.cafe.views.fragments.SearchFrag;

public class SearchViewModel extends BaseObservable {
    private Context context;
    private String name;
    private String type;
    private String address;
    private String lat;
    private String lng;
    private List<SearchViewModel> items= new ArrayList<>();
    private SetAdapterListener setAdapterListener;

    public SearchViewModel(Context context, SetAdapterListener setAdapterListener) {
        this.context = context;
        this.setAdapterListener = setAdapterListener;
    }


    public SearchViewModel(String name, String type, String address, String lat, String lng) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public List<SearchViewModel> getItems() {
        return items;
    }

    public void setItems(List<SearchViewModel> items) {
        this.items = items;
        setAdapterListener.onDataChanged();
        notifyPropertyChanged(BR.items);
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
