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
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.MessagesBinding;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.models.pojo.MessagesModel;
import ir.pitok.cafe.models.pojo.dataModels.MessagesItemDataModel;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.MessagesViewModel;
import ir.pitok.cafe.views.Interfaces.BackCallListener;
import ir.pitok.cafe.views.Interfaces.RecyclerViewClickListener;
import ir.pitok.cafe.views.adapters.MessagesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;
import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.getStandardUrl;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;

public class MessagesFrag extends CafeFragment {

    private MessagesBinding binding;
    private Requests requests;
    private MessagesViewModel mvm;
    private MessagesAdapter adapter;
    private BackCallListener backCallListener;

    public static MessagesFrag newInstance(BackCallListener backCallListener) {
        Bundle args = new Bundle();
        MessagesFrag fragment = new MessagesFrag();
        fragment.setArguments(args);
        fragment.backCallListener = backCallListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        mvm = new MessagesViewModel(allContext);
        binding.setVM(mvm);
        setOnClickListeners();
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.MSFToolbar.TLable.setText(getString(R.string.messages));
        requests = new Requests();
        binding.MSFMessagesRv.setLayoutManager(new LinearLayoutManager(allContext));
        ViewCompat.setTranslationZ(binding.MSFShadow,dp2Px(32));
        loadOrders();
    }

    private void loadOrders() {
        showLoading();
        requests.getMessages(getApiToken(allContext), new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
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
                mvm.clearMessagesList();
                List<MessagesItemDataModel> messages = response.body().getData().getMessages();
                for (MessagesItemDataModel msg : messages){
                    mvm.addNewItemToOrdersList(new MessagesViewModel(msg.getMessage_id(),msg.getLable(),msg.getText(),msg.getLink(),msg.getDate(),msg.getTime()));
                }
                setMessagesAdapter();

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                hideLoading();
                handleFailor(t,allContext);
            }
        });
    }

    private void setMessagesAdapter() {
        adapter = new MessagesAdapter(mvm.getMessages_list(), new RecyclerViewClickListener() {
            @Override
            public void onClickListener(int position) {
                String link = mvm.getMessages_list().get(position).getLink();
                if (link.isEmpty())return;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getStandardUrl(link))));

            }
        });
        binding.MSFMessagesRv.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        binding.MSFToolbar.TBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCallListener.onBackCall();
            }
        });
    }

    private void showLoading() {
        binding.MSFLoading.setVisibility(View.VISIBLE);
        binding.MSFMessagesRv.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        binding.MSFLoading.setVisibility(View.INVISIBLE);
        binding.MSFMessagesRv.setVisibility(View.VISIBLE);
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
