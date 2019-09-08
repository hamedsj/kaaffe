package ir.pitok.cafe.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ir.pitok.cafe.R;
import ir.pitok.cafe.models.responseModels.LoginWithGoogleModel;
import ir.pitok.cafe.utility.CafeUtilities;
import ir.pitok.cafe.databinding.SignUpLoginBinding;
import ir.pitok.cafe.repositories.Requests;
import ir.pitok.cafe.models.responseModels.ConfirmCodeModel;
import ir.pitok.cafe.models.responseModels.LoginModel;
import ir.pitok.cafe.models.responseModels.MessageModel;
import ir.pitok.cafe.utility.customs.CafeFragment;
import ir.pitok.cafe.viewModels.SignUpLoginViewModel;
import ir.pitok.cafe.views.Interfaces.LoadMapFragmentListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.pitok.cafe.utility.CafeUtilities.apiTokenExist;
import static ir.pitok.cafe.utility.CafeUtilities.en2prText;
import static ir.pitok.cafe.utility.CafeUtilities.getAgent;
import static ir.pitok.cafe.utility.CafeUtilities.getFcmToken;
import static ir.pitok.cafe.utility.CafeUtilities.googleSignInRequestCode;
import static ir.pitok.cafe.utility.CafeUtilities.handleFailor;
import static ir.pitok.cafe.utility.CafeUtilities.hashPassword;
import static ir.pitok.cafe.utility.CafeUtilities.setApiToken;

public class SignUpLoginFrag extends CafeFragment {

    private Requests requests;
    private String lastUsernameBeforeSendConfirmCode ="";
    private SignUpLoginViewModel signUpLoginViewModel;
    private GoogleSignInClient gClient;
    private SignUpLoginBinding nonLocalBinding;
    private LoadMapFragmentListener loadMapListener;


    public static SignUpLoginFrag newInstance(LoadMapFragmentListener loadMapListener) {
        Bundle args = new Bundle();
        SignUpLoginFrag fragment = new SignUpLoginFrag();
        fragment.setArguments(args);
        fragment.loadMapListener = loadMapListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SignUpLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_login,container,false);
        signUpLoginViewModel = new SignUpLoginViewModel(allContext);
        binding.setSLViewModel(signUpLoginViewModel);
        setOnClickListeners(binding);
        init(binding);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode==googleSignInRequestCode){
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);

                loginToAppWithGoogleInformation(account.getDisplayName().replace(" ",""),account.getEmail());
            } catch (ApiException e) {
                e.printStackTrace();
            }

        }

    }

    private void init(SignUpLoginBinding binding) {
        if (apiTokenExist(allContext)){
            loadMapListener.onLoadMapFragment();
        }
        requests = new Requests();
        GoogleSignInOptions gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(allContext, gOptions);
        binding.SLConfirmCodeLoading.setVisibility(View.GONE);
        binding.SLForgetPassLoading.setVisibility(View.GONE);
        binding.SLLoginLoading.setVisibility(View.GONE);
        binding.SLSignUpLoading.setVisibility(View.GONE);
        ViewCompat.setTranslationZ(binding.SLConfirmCodeLoading,1);
        ViewCompat.setTranslationZ(binding.SLForgetPassLoading,1);
        ViewCompat.setTranslationZ(binding.SLLoginLoading,1);
        ViewCompat.setTranslationZ(binding.SLSignUpLoading,1);
        nonLocalBinding = binding;
        allActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setOnClickListeners(final SignUpLoginBinding binding){
        binding.SLSignUpDarkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpLoading(binding);
                final String username = binding.SLSignUpUsernameEt.getText().toString().trim();
                final String phoneNumber = binding.SLSignUpPhoneNumberEt.getText().toString().trim();
                String pass = binding.SLSignUpPassEt.getText().toString().trim();
                String confirmPass = binding.SLSignUpConfirmPassEt.getText().toString().trim();

                if (username.isEmpty() || phoneNumber.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()){
                    Toast.makeText(allContext,"وارد کردن تمامی اطلاعات فوق برای ثبت نام لازم است",Toast.LENGTH_SHORT).show();
                    hideSignUpLoading(binding);
                    return;
                }
                if (!CafeUtilities.isValidUsername(username)){
                    Toast.makeText(allContext,"نام کاربری تنها می تواند شامل حروف و اعداد انگلیسی و علامت های . و ـ باشد",Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(getResources().getInteger(R.integer.shake_duration)).playOn(binding.SLSignUpUsernameEt);
                    hideSignUpLoading(binding);
                    return;
                }
                if (!CafeUtilities.isValidPhoneNumber(phoneNumber)){
                    Toast.makeText(allContext,"شماره همراه خود را درست وارد کنید. مانند : 09123456789",Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(getResources().getInteger(R.integer.shake_duration)).playOn(binding.SLSignUpPhoneNumberEt);
                    hideSignUpLoading(binding);
                    return;
                }
                if (!CafeUtilities.isValidPassword(pass)){
                    Toast.makeText(allContext,"رمز عبور باید شامل حداقل ۶ کاراکتر باشد",Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(getResources().getInteger(R.integer.shake_duration)).playOn(binding.SLSignUpPassEt);
                    hideSignUpLoading(binding);
                    return;
                }
                if (!pass.equals(confirmPass)){
                    Toast.makeText(allContext,"رمز عبور با تکرار آن مطابقت ندارد",Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(getResources().getInteger(R.integer.shake_duration)).playOn(binding.SLSignUpConfirmPassEt);
                    hideSignUpLoading(binding);
                    return;
                }

                requests.signup(username, hashPassword(pass), phoneNumber, getAgent(allContext), getFcmToken(allContext), new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        hideSignUpLoading(binding);
                        if (!response.isSuccessful()){
                            Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = response.body().getStatus();
                        String message = response.body().getData().getMessage();
                        if (status==409){
                            if (message.equals("duplicate_username")){
                                Toast.makeText(allContext,"این نام کاربری قبلا استفاده شده است",Toast.LENGTH_SHORT).show();
                            }else if (message.equals("duplicate_phone_number")){
                                Toast.makeText(allContext,"این شماره همراه قبلا استفاده شده است",Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if (status!=200){
                            Toast.makeText(allContext,"خطا در روند ثبت نام؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(allContext,"ثبت نام با موفقیت انجام شد",Toast.LENGTH_SHORT).show();
                        lastUsernameBeforeSendConfirmCode = username;
                        signUpLoginViewModel.setConfirmCodeText(en2prText("ارسال کد تایید برای "+ phoneNumber));
                        showConfirmCodeCard(binding);
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        hideSignUpLoading(binding);
                        handleFailor(t,allContext);
                    }
                });

            }
        });
        binding.SLLoginDarkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginLoading(binding);
                String username = binding.SLLoginUsernameEt.getText().toString().trim();
                String pass = binding.SLLoginPassEt.getText().toString().trim();
                if (username.isEmpty() || pass.isEmpty()){
                    Toast.makeText(allContext,"برای وارد شدن به اطلاعات فوق نیاز است",Toast.LENGTH_SHORT).show();
                    return;
                }
                requests.login(username, hashPassword(pass), getAgent(allContext), getFcmToken(allContext), new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        hideLoginLoading(binding);
                        if (!response.isSuccessful()){
                            Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = response.body().getStatus();
                        String message = response.body().getData().getMessage();
                        if (status==404){
                            Toast.makeText(allContext,"نام کاربری یا رمز عبور اشتباه است",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (status==405 && message.equals("not_this_way")){
                            Toast.makeText(allContext,"حساب کاربری شما از این روش ثبت نشده است؛ لطفا توسط حساب گوگل وارد شوید",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (status!=200){
                            Toast.makeText(allContext,"خطا در ورود به اپلیکیشن؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(allContext,"با موفقیت وارد شدید",Toast.LENGTH_SHORT).show();
                        setApiToken(response.body().getData().getApi_token(),allContext);
                        loadMapListener.onLoadMapFragment();
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        hideLoginLoading(binding);
                        handleFailor(t,allContext);
                    }
                });
            }
        });

        binding.SLForgetPassDarkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgetPassLoading(binding);
                String usernameOrPhoneNumber = binding.SLForgetPassUsernameEt.getText().toString();
                if (usernameOrPhoneNumber.isEmpty()){
                    Toast.makeText(allContext,"برای بازیابی رمز عبور به اطلاعات فوق نیاز داریم",Toast.LENGTH_SHORT).show();
                    return;
                }
                requests.forgetPass(usernameOrPhoneNumber, new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        hideForgetPassLoading(binding);
                        if (!response.isSuccessful()){
                            Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = response.body().getStatus();
                        String message = response.body().getData().getMessage();
                        if (status==404){
                            if (message.equals("not_found")){
                                Toast.makeText(allContext,"هیچ حسابی با این اطلاعات یافت نشد",Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if (status!=200){
                            Toast.makeText(allContext,"خطا در روند بازیابی رمز عبور؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(allContext,"رمز عبور شما بازیابی شد؛ رمز عبور جدید برای شما ارسال خواهد شد",Toast.LENGTH_SHORT).show();
                        showLoginCardFromForgetPass(binding);
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        hideForgetPassLoading(binding);
                        handleFailor(t,allContext);
                    }
                });
            }
        });

        binding.SLConfirmCodeDarkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmCodeLoading(binding);
                if (binding.SLConfirmCodeEt.getText().toString().isEmpty()){
                    Toast.makeText(allContext,"لطفا کد تایید را وارد کنید",Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = Integer.parseInt(binding.SLConfirmCodeEt.getText().toString());
                requests.confirmCode(lastUsernameBeforeSendConfirmCode, code, new Callback<ConfirmCodeModel>() {
                    @Override
                    public void onResponse(Call<ConfirmCodeModel> call, Response<ConfirmCodeModel> response) {
                        hideConfirmCodeLoading(binding);
                        if (!response.isSuccessful()){
                            Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = response.body().getStatus();
                        if (status!=200){
                            Toast.makeText(allContext,"خطا در ورود به اپلیکیشن؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        setApiToken(response.body().getData().getApi_token(),allContext);
                        loadMapListener.onLoadMapFragment();
                    }

                    @Override
                    public void onFailure(Call<ConfirmCodeModel> call, Throwable t) {
                        hideConfirmCodeLoading(binding);
                        handleFailor(t,allContext);
                    }
                });
            }
        });

        binding.SLSignUpStrokeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginCardFromSignUp(binding);
            }
        });

        binding.SLLoginStrokeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpCardFromLogin(binding);
            }
        });

        binding.SLLoginForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgetPassCard(binding);
            }
        });

        binding.SLForgetPassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginCardFromForgetPass(binding);
            }
        });

        binding.SLConfirmCodeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpCardFromConfirmCode(binding);
            }
        });

        binding.SLLoginWithGoogleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle();
            }
        });

        binding.SLSignUpWithGoogleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle();
            }
        });

    }

    private void showLoginLoading(SignUpLoginBinding binding){
        binding.SLLoginDarkBt.setText("");
        binding.SLLoginLoading.setVisibility(View.VISIBLE);
        binding.SLLoginForgetPass.setEnabled(false);
        binding.SLLoginStrokeBt.setEnabled(false);
        binding.SLLoginWithGoogleBt.setEnabled(false);
        binding.SLLoginDarkBt.setEnabled(false);
    }

    private void hideLoginLoading(SignUpLoginBinding binding){
        binding.SLLoginDarkBt.setText(getString(R.string.login));
        binding.SLLoginLoading.setVisibility(View.GONE);
        binding.SLLoginForgetPass.setEnabled(true);
        binding.SLLoginStrokeBt.setEnabled(true);
        binding.SLLoginWithGoogleBt.setEnabled(true);
        binding.SLLoginDarkBt.setEnabled(true);
    }

    private void showConfirmCodeLoading(SignUpLoginBinding binding){
        binding.SLConfirmCodeDarkBt.setText("");
        binding.SLConfirmCodeLoading.setVisibility(View.VISIBLE);
        binding.SLConfirmCodeDarkBt.setEnabled(false);
        binding.SLConfirmCodeBack.setEnabled(false);
    }

    private void hideConfirmCodeLoading(SignUpLoginBinding binding){
        binding.SLConfirmCodeDarkBt.setText(getString(R.string.send_confirm_code));
        binding.SLConfirmCodeLoading.setVisibility(View.GONE);
        binding.SLConfirmCodeDarkBt.setEnabled(true);
        binding.SLConfirmCodeBack.setEnabled(true);
    }

    private void showForgetPassLoading(SignUpLoginBinding binding){
        binding.SLForgetPassDarkBt.setText("");
        binding.SLForgetPassLoading.setVisibility(View.VISIBLE);
        binding.SLForgetPassDarkBt.setEnabled(false);
        binding.SLForgetPassBack.setEnabled(false);
    }

    private void hideForgetPassLoading(SignUpLoginBinding binding){
        binding.SLForgetPassDarkBt.setText(getString(R.string.recovery_password));
        binding.SLForgetPassLoading.setVisibility(View.GONE);
        binding.SLForgetPassDarkBt.setEnabled(true);
        binding.SLForgetPassBack.setEnabled(true);
    }

    private void showSignUpLoading(SignUpLoginBinding binding){
        binding.SLSignUpDarkBt.setText("");
        binding.SLSignUpLoading.setVisibility(View.VISIBLE);
        binding.SLSignUpDarkBt.setEnabled(false);
        binding.SLSignUpStrokeBt.setEnabled(false);
        binding.SLSignUpWithGoogleBt.setEnabled(false);
    }

    private void hideSignUpLoading(SignUpLoginBinding binding){
        binding.SLSignUpDarkBt.setText(getString(R.string.signup));
        binding.SLSignUpLoading.setVisibility(View.GONE);
        binding.SLSignUpDarkBt.setEnabled(true);
        binding.SLSignUpStrokeBt.setEnabled(true);
        binding.SLSignUpWithGoogleBt.setEnabled(true);
    }

    private void showSignUpGoogleLoading(SignUpLoginBinding binding){
        binding.SLSignUpWithGoogleBt.setText("");
        binding.SLSignUpGoogleLoading.setVisibility(View.VISIBLE);
        binding.SLSignUpDarkBt.setEnabled(false);
        binding.SLSignUpStrokeBt.setEnabled(false);
        binding.SLSignUpWithGoogleBt.setEnabled(false);
    }

    private void hideSignUpGoogleLoading(SignUpLoginBinding binding){
        binding.SLSignUpWithGoogleBt.setText(getString(R.string.login_with_google));
        binding.SLSignUpGoogleLoading.setVisibility(View.GONE);
        binding.SLSignUpDarkBt.setEnabled(true);
        binding.SLSignUpStrokeBt.setEnabled(true);
        binding.SLSignUpWithGoogleBt.setEnabled(true);
    }

    private void showLoginGoogleLoading(SignUpLoginBinding binding){
        binding.SLLoginWithGoogleBt.setText("");
        binding.SLLoginGoogleLoading.setVisibility(View.VISIBLE);
        binding.SLLoginForgetPass.setEnabled(false);
        binding.SLLoginStrokeBt.setEnabled(false);
        binding.SLLoginWithGoogleBt.setEnabled(false);
        binding.SLLoginDarkBt.setEnabled(false);
    }

    private void hideLoginGoogleLoading(SignUpLoginBinding binding){
        binding.SLLoginWithGoogleBt.setText(getString(R.string.login_with_google));
        binding.SLLoginGoogleLoading.setVisibility(View.GONE);
        binding.SLLoginForgetPass.setEnabled(true);
        binding.SLLoginStrokeBt.setEnabled(true);
        binding.SLLoginWithGoogleBt.setEnabled(true);
        binding.SLLoginDarkBt.setEnabled(true);
    }

    private void showLoginCardFromSignUp(final SignUpLoginBinding binding) {
        YoYo.with(Techniques.FadeOut).duration(getAnimDuration()).playOn(binding.SLSignUpCard);
        binding.SLLoginCard.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(getAnimDuration()).playOn(binding.SLLoginCard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SLSignUpCard.setVisibility(View.INVISIBLE);
            }
        }, getAnimDuration());
    }

    private void showSignUpCardFromLogin(final SignUpLoginBinding binding) {
        YoYo.with(Techniques.FadeOut).duration(getAnimDuration()).playOn(binding.SLLoginCard);
        binding.SLSignUpCard.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(getAnimDuration()).playOn(binding.SLSignUpCard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SLLoginCard.setVisibility(View.INVISIBLE);
            }
        }, getAnimDuration());
    }

    private void showSignUpCardFromConfirmCode(final SignUpLoginBinding binding) {
        YoYo.with(Techniques.FadeOut).duration(getAnimDuration()).playOn(binding.SLConfirmCodeCard);
        binding.SLSignUpCard.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(getAnimDuration()).playOn(binding.SLSignUpCard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SLConfirmCodeCard.setVisibility(View.INVISIBLE);
            }
        }, getAnimDuration());
    }

    private void showLoginCardFromForgetPass(final SignUpLoginBinding binding) {
        YoYo.with(Techniques.FadeOut).duration(getAnimDuration()).playOn(binding.SLForgetPassCard);
        binding.SLLoginCard.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(getAnimDuration()).playOn(binding.SLLoginCard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SLForgetPassCard.setVisibility(View.INVISIBLE);
            }
        }, getAnimDuration());
    }

    private void showForgetPassCard(final SignUpLoginBinding binding) {
        YoYo.with(Techniques.FadeOut).duration(getAnimDuration()).playOn(binding.SLLoginCard);
        binding.SLForgetPassCard.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(getAnimDuration()).playOn(binding.SLForgetPassCard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SLLoginCard.setVisibility(View.INVISIBLE);
            }
        }, getAnimDuration());
    }

    private void showConfirmCodeCard(final SignUpLoginBinding binding) {
        YoYo.with(Techniques.FadeOut).duration(getAnimDuration()).playOn(binding.SLSignUpCard);
        binding.SLConfirmCodeCard.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(getAnimDuration()).playOn(binding.SLConfirmCodeCard);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SLSignUpCard.setVisibility(View.INVISIBLE);
            }
        }, getAnimDuration());
    }

    private void loginWithGoogle() {
        if (nonLocalBinding.SLLoginCard.getVisibility()==View.VISIBLE){
            showLoginGoogleLoading(nonLocalBinding);
        }else{
            showSignUpGoogleLoading(nonLocalBinding);
        }
        gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (nonLocalBinding.SLLoginCard.getVisibility()==View.VISIBLE){
                    hideLoginGoogleLoading(nonLocalBinding);
                }else{
                    hideSignUpGoogleLoading(nonLocalBinding);
                }
                Intent intent = gClient.getSignInIntent();
                startActivityForResult(intent, googleSignInRequestCode);
            }
        });
    }

    private int getAnimDuration(){
        return getResources().getInteger(R.integer.signup_fragment_anim_duration);
    }

    private void loginToAppWithGoogleInformation(String name, String email) {
        if (nonLocalBinding.SLLoginCard.getVisibility()==View.VISIBLE){
            showLoginGoogleLoading(nonLocalBinding);
        }else{
            showSignUpGoogleLoading(nonLocalBinding);
        }

        requests.loginWithGoogle(name, email, getAgent(allContext), getFcmToken(allContext), new Callback<LoginWithGoogleModel>() {
            @Override
            public void onResponse(Call<LoginWithGoogleModel> call, Response<LoginWithGoogleModel> response) {
                if (nonLocalBinding.SLLoginCard.getVisibility()==View.VISIBLE){
                    hideLoginGoogleLoading(nonLocalBinding);
                }else{
                    hideSignUpGoogleLoading(nonLocalBinding);
                }
                if (!response.isSuccessful()){
                    Toast.makeText(allContext,"مشکل در ارتباط با سرور",Toast.LENGTH_SHORT).show();
                    return;
                }
                int status = response.body().getStatus();
                String message = response.body().getData().getMessage();
                if (status==405 && message.equals("not_this_way")){
                    Toast.makeText(allContext,"حساب کاربری شما از این روش ثبت نشده است",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (status!=200){
                    Toast.makeText(allContext,"خطا در ورود به اپلیکیشن؛ لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(allContext,"با موفقیت وارد شدید",Toast.LENGTH_SHORT).show();
                setApiToken(response.body().getData().getApi_token(),allContext);
                loadMapListener.onLoadMapFragment();
            }

            @Override
            public void onFailure(Call<LoginWithGoogleModel> call, Throwable t) {
                if (nonLocalBinding.SLLoginCard.getVisibility()==View.VISIBLE){
                    hideLoginGoogleLoading(nonLocalBinding);
                }else{
                    hideSignUpGoogleLoading(nonLocalBinding);
                }
                handleFailor(t,allContext);
            }
        });

    }

}
