package ir.pitok.cafe.repositories;

import ir.pitok.cafe.models.responseModels.ConfirmCodeModel;
import ir.pitok.cafe.models.responseModels.LoginModel;
import ir.pitok.cafe.models.responseModels.LoginWithGoogleModel;
import ir.pitok.cafe.models.responseModels.MessageModel;
import ir.pitok.cafe.models.responseModels.MessagesModel;
import ir.pitok.cafe.models.responseModels.NearbyShopsModel;
import ir.pitok.cafe.models.responseModels.ChoosedOrdersModel;
import ir.pitok.cafe.models.responseModels.OrdersModel;
import ir.pitok.cafe.models.responseModels.PayModel;
import ir.pitok.cafe.models.responseModels.PaymentModel;
import ir.pitok.cafe.models.responseModels.ProfileModel;
import ir.pitok.cafe.models.responseModels.SearchModel;
import ir.pitok.cafe.models.responseModels.ShopDetailModel;
import retrofit2.Callback;

public class Requests {

    private ApiInterface apiInterface;

    public Requests() {
        if (apiInterface == null){
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
        }
    }

    public void signup(String username, String pass, String phoneNumber, String agent, String fcm_token, Callback<MessageModel> callback){
        apiInterface.signup(username,pass,phoneNumber,agent,fcm_token).enqueue(callback);
    }

    public void login(String username, String pass, String agent, String fcm_token, Callback<LoginModel> callback){
        apiInterface.login(username,pass,agent,fcm_token).enqueue(callback);
    }

    public void confirmCode(String username, int code, Callback<ConfirmCodeModel> callback){
        apiInterface.confirmCode(username,code).enqueue(callback);
    }

    public void forgetPass(String usernameOrPhoneNumber, Callback<MessageModel> callback){
        apiInterface.forgetPass(usernameOrPhoneNumber).enqueue(callback);
    }

    public void loginWithGoogle(String username, String email, String agent, String fcm_token, Callback<LoginWithGoogleModel> callback){
        apiInterface.loginWithGoogle(username,email,agent,fcm_token).enqueue(callback);
    }

    public void serach(String api_token, String search_text, Callback<SearchModel> callback){
        apiInterface.search(api_token,search_text).enqueue(callback);
    }

    public void getNearbyCoffees(String api_token, String lat, String lng, Callback<NearbyShopsModel> callback){
        apiInterface.getNearbyCoffees(api_token,lat,lng).enqueue(callback);
    }

    public void getShopDetails(String api_token, int shop_id, Callback<ShopDetailModel> callback){
        apiInterface.getShopDetails(api_token,shop_id).enqueue(callback);
    }

    public void getOrdersList(String api_token, String orders, Callback<ChoosedOrdersModel> callback){
        apiInterface.getOrdersList(api_token,orders).enqueue(callback);
    }

    public void getOrdersAmount(String api_token, String orders, String discount_code, Callback<PaymentModel> callback){
        apiInterface.getOrdersAmount(api_token,orders,discount_code).enqueue(callback);
    }

    public void payOrderList(String api_token, String orders, String discount_code, Callback<PayModel> callback){
        apiInterface.payOrderList(api_token,orders,discount_code).enqueue(callback);
    }

    public void getUserOrders(String api_token,Callback<OrdersModel> callback){
        apiInterface.getUserOrders(api_token).enqueue(callback);
    }

    public void getMessages(String api_token,Callback<MessagesModel> callback){
        apiInterface.getMessages(api_token).enqueue(callback);
    }

    public void getProfile(String api_token,Callback<ProfileModel> callback){
        apiInterface.getProfile(api_token).enqueue(callback);
    }

    public void editProfile(String api_token,String username,Callback<MessageModel> callback){
        apiInterface.editProfile(api_token,username).enqueue(callback);
    }

}
