package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.SearchBinding;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.models.pojo.SearchModel;
import ir.pitok.cafe.models.pojo.dataModels.SearchResultItemDataModel;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.SearchViewModel;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentInLatLngListener;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.Interfaces.SetAdapterListener;
import ir.pitok.cafe.views.adapters.SearchResultsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;

public class SearchFrag extends CafeFragment{

    private Requests requests;
    private List<SearchViewModel> items = new ArrayList<>();
    private SearchViewModel svm;
    private LoadMapFragmentInLatLngListener loadMapInLatLngListener;
    private LoadMapFragmentListener loadMapListener;
    private SearchResultsAdapter adapter;

    public static SearchFrag newInstance(LoadMapFragmentInLatLngListener loadMapInLatLngListener, LoadMapFragmentListener loadMapListener) {
        Bundle args = new Bundle();
        SearchFrag fragment = new SearchFrag();
        fragment.setArguments(args);
        fragment.loadMapInLatLngListener = loadMapInLatLngListener;
        fragment.loadMapListener = loadMapListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        svm = new SearchViewModel(allContext, new SetAdapterListener() {
            @Override
            public void onDataChanged() {
                setResultsAdapter(binding);
            }
        });
        binding.setSearchVM(svm);
        setOnClickListeners(binding);
        init(binding);
        return binding.getRoot();
    }

    private void init(SearchBinding binding) {
        requests = new Requests();
    }

    private void setOnClickListeners(final SearchBinding binding) {
        binding.SCSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(binding);
                    return true;
                }
                return false;
            }
        });

        binding.SCSearchIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(binding);
            }
        });

        binding.SCBackIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMapListener.onLoadMapFragment();
            }
        });
    }

    private void search(final SearchBinding binding) {
        showLoading(binding);
        binding.SCResultsRv.setLayoutManager(new LinearLayoutManager(allContext));
        String searchText = binding.SCSearchEt.getText().toString().trim();
        if (searchText.isEmpty())return;
        requests.serach(getApiToken(allContext), searchText, new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                hideLoading(binding);
                if (!response.isSuccessful()){
                    Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                    return;
                }
                int status = response.body().getStatus();
                String message = response.body().getData().getMessage();
                if (status==401 && message.equals("api_token")){
                    //todo: login again
                    return;
                }
                if (status!=200){
                    Toast.makeText(allContext,"خطا در روند جستجو؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<SearchResultItemDataModel> result_items = response.body().getData().getResults();
                items.clear();
                for (SearchResultItemDataModel result_item: result_items){
                    items.add(new SearchViewModel(result_item.getName(),result_item.getType(),result_item.getAddress(),result_item.getLat(),result_item.getLng()));
                }
                svm.setItems(items);
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                hideLoading(binding);
                handleFailor(t,allContext);
            }
        });
    }

    private void showLoading(SearchBinding binding) {
        binding.SCLoading.setVisibility(View.VISIBLE);
        binding.SCLableResults.setVisibility(View.INVISIBLE);
        binding.SCResultsRv.setVisibility(View.INVISIBLE);
    }

    private void hideLoading(SearchBinding binding) {
        binding.SCLoading.setVisibility(View.INVISIBLE);
        binding.SCLableResults.setVisibility(View.VISIBLE);
        binding.SCResultsRv.setVisibility(View.VISIBLE);
    }

    private void setResultsAdapter(SearchBinding binding){
        adapter = new SearchResultsAdapter(svm.getItems(), new RecyclerViewClickListener() {
            @Override
            public void onClickListener(int position) {
                SearchViewModel item = svm.getItems().get(position);
//                if (!item.getType().equals("coffee")) {
                    loadMapInLatLngListener.onLoadMapFragment(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()));
//                }
            }
        });
        binding.SCResultsRv.setAdapter(adapter);
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
