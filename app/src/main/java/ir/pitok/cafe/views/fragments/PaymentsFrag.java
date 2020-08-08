package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.PaymentsBinding;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.models.pojo.PayModel;
import ir.pitok.cafe.models.pojo.PaymentModel;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.PaymentsViewModel;
import ir.pitok.cafe.views.Interfaces.LoadChoosedOrdersFragmentListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.ItemsIdListToJsonArray;
import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;
import static ir.pitok.cafe.utils.CafeUtilities.en2prText;
import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.getStandardUrl;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;

public class PaymentsFrag extends CafeFragment {

    private List<Integer> items_id;
    private LoadChoosedOrdersFragmentListener loadChoosedOrdersFragmentListener;
    private PaymentsBinding binding;
    private PaymentsViewModel pvm;
    private Requests requests;
    private String lastDiscountCode = "";

    public static PaymentsFrag newInstance(List<Integer> items_id, LoadChoosedOrdersFragmentListener loadChoosedOrdersFragmentListener) {
        Bundle args = new Bundle();
        PaymentsFrag fragment = new PaymentsFrag();
        fragment.setArguments(args);
        fragment.items_id = items_id;
        fragment.loadChoosedOrdersFragmentListener = loadChoosedOrdersFragmentListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);
        pvm = new PaymentsViewModel(allContext,items_id);
        binding.setVM(pvm);
        setOnClickListeners();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.PFToolbar.TLable.setText(getString(R.string.payment));
        requests = new Requests();
        ViewCompat.setTranslationZ(binding.PFShadow,dp2Px(32));
        ViewCompat.setTranslationZ(binding.PFPayLoading,dp2Px(8));
        loadAmounts();
    }

    private void loadAmounts() {
        showLoading();
        lastDiscountCode = binding.PFDiscountEt.getText().toString();
        requests.getOrdersAmount(getApiToken(allContext), ItemsIdListToJsonArray(pvm.getItemsId()), lastDiscountCode, new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
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

                int tAmount = response.body().getData().getTotalAmounts();
                int discountAmount = response.body().getData().getDiscountAmount();
                int amountForPay = response.body().getData().getAmountForPay();

                binding.PFTotalAmount.setText(en2prText(amountForPay + " تومان"));
                binding.PFDiscountAmount.setText(en2prText(discountAmount + " تومان"));
                binding.PFAmountSum.setText(en2prText(tAmount + " تومان"));
                binding.PFUseDiscount.setEnabled(true);
                binding.PFPayBt.setEnabled(true);

            }

            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                hideLoading();
                handleFailor(t,allContext);
            }
        });

    }

    private void setOnClickListeners() {
        binding.PFToolbar.TBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChoosedOrdersFragmentListener.onLoadChoosedOrdersListener(pvm.getItemsId());
            }
        });
        binding.PFUseDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAmounts();
            }
        });

        binding.PFPayBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayLoading();
                requests.payOrderList(getApiToken(allContext), ItemsIdListToJsonArray(pvm.getItemsId()), lastDiscountCode, new Callback<PayModel>() {
                    @Override
                    public void onResponse(Call<PayModel> call, Response<PayModel> response) {
                        hidePayLoading();
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
                            Toast.makeText(allContext,"خطا در روند پرداخت؛ دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (message.equals("paid")){
                            ViewCompat.setBackground(binding.PFPayBt, ContextCompat.getDrawable(allContext,R.drawable.rc_full_round_gray));
                            binding.PFPayBt.setText(getString(R.string.paid));
                            return;
                        }

                        String url = getStandardUrl(response.body().getData().getUrl());
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    }

                    @Override
                    public void onFailure(Call<PayModel> call, Throwable t) {
                        hidePayLoading();
                        handleFailor(t,allContext);
                    }
                });
            }
        });
    }

    private void showLoading() {
        binding.PFAmountSumPb.setVisibility(View.VISIBLE);
        binding.PFDiscountAmountPb.setVisibility(View.VISIBLE);
        binding.PFTotalAmountPb.setVisibility(View.VISIBLE);
        binding.PFAmountSum.setVisibility(View.INVISIBLE);
        binding.PFDiscountAmount.setVisibility(View.INVISIBLE);
        binding.PFTotalAmount.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        binding.PFAmountSumPb.setVisibility(View.INVISIBLE);
        binding.PFDiscountAmountPb.setVisibility(View.INVISIBLE);
        binding.PFTotalAmountPb.setVisibility(View.INVISIBLE);
        binding.PFAmountSum.setVisibility(View.VISIBLE);
        binding.PFDiscountAmount.setVisibility(View.VISIBLE);
        binding.PFTotalAmount.setVisibility(View.VISIBLE);
    }

    private void showPayLoading() {
        binding.PFPayLoading.setVisibility(View.VISIBLE);
        binding.PFPayBt.setText("");
    }

    private void hidePayLoading() {
        binding.PFPayLoading.setVisibility(View.INVISIBLE);
        binding.PFPayBt.setText(getString(R.string.pay));
    }

    private void checkButtonEnable(){
//        if (ovm.getOrdersList().size()==0){
//            binding.OFOkOrders.setEnabled(false);
//            binding.OFOkOrders.setBackgroundColor(ContextCompat.getColor(allContext,R.color.colorSecondaryText));
//        }else{
//            binding.OFOkOrders.setEnabled(true);
//            binding.OFOkOrders.setBackgroundColor(ContextCompat.getColor(allContext,android.R.color.holo_green_dark));
//        }
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
