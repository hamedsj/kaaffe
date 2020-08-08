package ir.pitok.cafe.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ir.pitok.cafe.R;
import ir.pitok.cafe.databinding.ProfileBinding;
import ir.pitok.cafe.models.pojo.MessageModel;
import ir.pitok.cafe.models.pojo.ProfileModel;
import ir.pitok.cafe.models.repositories.Requests;
import ir.pitok.cafe.utils.customs.CafeFragment;
import ir.pitok.cafe.viewModels.ProfileViewModel;
import ir.pitok.cafe.views.Interfaces.BackCallListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utils.CafeUtilities.dp2Px;
import static ir.pitok.cafe.utils.CafeUtilities.getApiToken;
import static ir.pitok.cafe.utils.CafeUtilities.handleFailor;
import static ir.pitok.cafe.utils.CafeUtilities.isValidUsername;

public class ProfileFrag extends CafeFragment {

    private BackCallListener backCallListener;
    private ProfileBinding binding;
    private ProfileViewModel pvm;
    private Requests requests;

    public static ProfileFrag newInstance(BackCallListener backCallListener) {
        Bundle args = new Bundle();
        ProfileFrag fragment = new ProfileFrag();
        fragment.setArguments(args);
        fragment.backCallListener = backCallListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        pvm = new ProfileViewModel(allContext);
        binding.setVM(pvm);
        init();
        setOnClickListeners();
        return binding.getRoot();
    }

    private void init() {
        ViewCompat.setTranslationZ(binding.PRFShadow,dp2Px(32));
        ViewCompat.setTranslationZ(binding.PRFEditLoading,dp2Px(32));
        binding.PRFToolbar.TLable.setText(getString(R.string.edit_profile));
        requests = new Requests();
        loadData();
    }

    private void loadData() {
        requests.getProfile(getApiToken(allContext), new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
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
                binding.PRFUsernameEt.setText(response.body().getData().getUsername());
                binding.PRFPhoneNumberEt.setText(response.body().getData().getPhone_number());
                binding.PRFPhoneNumberEt.setInputType(InputType.TYPE_NULL);
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                handleFailor(t,allContext);
            }
        });
    }

    private void setOnClickListeners() {
        binding.PRFToolbar.TBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCallListener.onBackCall();
            }
        });
        binding.PRFEditBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataForEdit();
            }
        });
    }

    private void sendDataForEdit() {
        showLoading();
        String username = binding.PRFUsernameEt.getText().toString();
        if (!isValidUsername(username)){
            Toast.makeText(allContext,"نام کاربری تنها می تواند شامل حروف و اعداد انگلیسی و علامت های . و ـ باشد",Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(getResources().getInteger(R.integer.shake_duration)).playOn(binding.PRFUsernameEt);
            hideLoading();
            return;
        }
        requests.editProfile(getApiToken(allContext), username, new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
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
                if (status==409 && message.equals("duplicate_username")){
                    Toast.makeText(allContext,"تغییرات انجام نشدند؛ نام کاربری قبلا استفاده شده است",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (status!=200){
                    Toast.makeText(allContext,"خطا در روند دریافت اطلاعات؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(allContext,"اطلاعات کاربری با موفقیت اصلاح شدند",Toast.LENGTH_SHORT).show();
                loadData();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                hideLoading();
                handleFailor(t,allContext);
            }
        });
    }

    private void showLoading() {
        binding.PRFEditBt.setText("");
        binding.PRFEditLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.PRFEditLoading.setVisibility(View.INVISIBLE);
        binding.PRFEditBt.setText(getString(R.string.edit));
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
