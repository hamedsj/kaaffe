package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ChoosedOrdersBinding;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.models.pojo.ChoosedOrdersModel;
import ir.pitok.cafe.models.pojo.dataModels.ChoosedOrdersItemDataModel;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.ChoosedOrdersViewModel;

import ir.pitok.cafe.views.Interfaces.DeleteListener;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentListener;
import ir.pitok.cafe.views.Interfaces.LoadPaymentsFragmentListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.adapters.ChoosedOrdersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.ItemsIdListToJsonArray;
import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;
import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;

public class ChoosedOrdersFrag extends CafeFragment {

    private ChoosedOrdersBinding binding;
    private List<Integer> items_id;
    private Requests requests;
    private ChoosedOrdersAdapter adapter;
    private ChoosedOrdersViewModel ovm;
    private LoadMapFragmentListener loadMapFragmentListener;
    private LoadPaymentsFragmentListener loadPaymentsFragmentListener;


    public static ChoosedOrdersFrag newInstance(List<Integer> items_id, LoadMapFragmentListener loadMapFragmentListener, LoadPaymentsFragmentListener loadPaymentsFragmentListener) {
        Bundle args = new Bundle();
        ChoosedOrdersFrag fragment = new ChoosedOrdersFrag();
        fragment.setArguments(args);
        fragment.items_id = items_id;
        fragment.loadMapFragmentListener = loadMapFragmentListener;
        fragment.loadPaymentsFragmentListener = loadPaymentsFragmentListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choosed_orders, container, false);
        ovm = new ChoosedOrdersViewModel(allContext,items_id);
        binding.setVM(ovm);
        setOnClickListeners();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.COFToolbar.TLable.setText(getString(R.string.orders));
        requests = new Requests();
        binding.COFOrdersRv.setLayoutManager(new LinearLayoutManager(allContext));
        ViewCompat.setTranslationZ(binding.COFShadow,dp2Px(32));
        ViewCompat.setTranslationZ(binding.COFShadowDown,dp2Px(32));
        checkButtonEnable();
        loadOrders();
    }

    private void loadOrders() {
        showLoading();
        requests.getOrdersList(getApiToken(allContext), ItemsIdListToJsonArray(items_id), new Callback<ChoosedOrdersModel>() {
            @Override
            public void onResponse(Call<ChoosedOrdersModel> call, Response<ChoosedOrdersModel> response) {
                hideLoading();
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
                    Toast.makeText(allContext,"خطا در روند دریافت اطلاعات؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ChoosedOrdersItemDataModel> orders = response.body().getData().getOrders();
                for (ChoosedOrdersItemDataModel order : orders){
                    boolean check = false;
                    for(int i=0;i<ovm.getOrdersList().size();i++){
                        if (order.getItem_id()!=ovm.getOrdersList().get(i).getItem_id()) continue;
                        ovm.changeOrderCountInOrdersList(i,ovm.getOrdersList().get(i).getCount()+1);
                        check = true;
                        break;
                    }
                    if (!check)
                        ovm.addNewOrderToOrdersList(new ChoosedOrdersViewModel(order.getItem_id(),order.getName(),order.getPicture(),order.getPrice(),1));
                }
                checkButtonEnable();
                setOrdersAdapter();

            }

            @Override
            public void onFailure(Call<ChoosedOrdersModel> call, Throwable t) {
                hideLoading();
                handleFailor(t,allContext);
            }
        });
    }

    private void setOrdersAdapter() {
        adapter = new ChoosedOrdersAdapter(ovm.getOrdersList(), new RecyclerViewClickListener() {
            @Override
            public void onClickListener(int position) {

            }
        }, new DeleteListener() {
            @Override
            public void onDeleteListener(int position) {
                if (ovm.getOrdersList().get(position).getCount()!=1){
                    ovm.changeOrderCountInOrdersList(position,ovm.getOrdersList().get(position).getCount()-1);
                    adapter.notifyItemChanged(position);
                }else{
                    ovm.deleteOrdeerFromOrdersList(position);
                    adapter.notifyItemRemoved(position);
                    checkButtonEnable();
                }
            }
        });
        binding.COFOrdersRv.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        binding.COFToolbar.TBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMapFragmentListener.onLoadMapFragment();
            }
        });
        binding.COFOkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPaymentsFragmentListener.onLoadPaymentsListener(ovm.getItemsId());
            }
        });
    }

    private void showLoading() {
        binding.COFLoading.setVisibility(View.VISIBLE);
        binding.COFOrdersRv.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        binding.COFLoading.setVisibility(View.INVISIBLE);
        binding.COFOrdersRv.setVisibility(View.VISIBLE);
    }

    private void checkButtonEnable(){
        if (ovm.getOrdersList().size()==0){
            binding.COFOkOrders.setEnabled(false);
            binding.COFOkOrders.setBackgroundColor(ContextCompat.getColor(allContext,R.color.colorSecondaryText));
        }else{
            binding.COFOkOrders.setEnabled(true);
            binding.COFOkOrders.setBackgroundColor(ContextCompat.getColor(allContext,android.R.color.holo_green_dark));
        }
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
