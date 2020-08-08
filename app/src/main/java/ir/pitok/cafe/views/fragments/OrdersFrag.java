package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.OrdersBinding;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.models.pojo.OrdersModel;
import ir.pitok.cafe.models.pojo.dataModels.OrdersItemDataModel;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.OrdersViewModel;
import ir.pitok.cafe.views.Interfaces.BackCallListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.adapters.OrdersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;
import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;

public class OrdersFrag extends CafeFragment {

    private OrdersBinding binding;
    private Requests requests;
    private OrdersViewModel ovm;
    private OrdersAdapter adapter;
    private BackCallListener backCallListener;

    public static OrdersFrag newInstance(BackCallListener backCallListener) {
        Bundle args = new Bundle();
        OrdersFrag fragment = new OrdersFrag();
        fragment.setArguments(args);
        fragment.backCallListener = backCallListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        ovm = new OrdersViewModel(allContext);
        binding.setVM(ovm);
        setOnClickListeners();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.OFToolbar.TLable.setText(getString(R.string.orders));
        requests = new Requests();
        binding.OFOrdersRv.setLayoutManager(new LinearLayoutManager(allContext));
        ViewCompat.setTranslationZ(binding.OFShadow,dp2Px(32));
        loadOrders();
    }

    private void loadOrders() {
        showLoading();
        requests.getUserOrders(getApiToken(allContext), new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
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

                ovm.clearOrdersList();
                List<OrdersItemDataModel> orders = response.body().getData().getOrders();
                for (OrdersItemDataModel order : orders){
                    ovm.addNewItemToOrdersList(new OrdersViewModel(order.getOrder_id(),order.getPrice(),order.getOrders_count(),order.getShop_name(),order.getDate(),order.getTime()));
                }
                setOrdersAdapter();
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                hideLoading();
                handleFailor(t,allContext);
            }
        });
    }

    private void setOrdersAdapter() {
        adapter = new OrdersAdapter(ovm.getOrders_list(), new RecyclerViewClickListener() {
            @Override
            public void onClickListener(int position) {

            }
        });
        binding.OFOrdersRv.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        binding.OFToolbar.TBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCallListener.onBackCall();
            }
        });
    }

    private void showLoading() {
        binding.OFLoading.setVisibility(View.VISIBLE);
        binding.OFOrdersRv.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        binding.OFLoading.setVisibility(View.INVISIBLE);
        binding.OFOrdersRv.setVisibility(View.VISIBLE);
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
