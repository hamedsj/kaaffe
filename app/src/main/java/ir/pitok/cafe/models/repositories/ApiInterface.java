package ir.pitok.cafe.models.repositories;

import ir.pitok.cafe.models.pojo.ConfirmCodeModel;
import ir.pitok.cafe.models.pojo.LoginModel;
import ir.pitok.cafe.models.pojo.LoginWithGoogleModel;
import ir.pitok.cafe.models.pojo.MessageModel;
import ir.pitok.cafe.models.pojo.MessagesModel;
import ir.pitok.cafe.models.pojo.NearbyShopsModel;
import ir.pitok.cafe.models.pojo.ChoosedOrdersModel;
import ir.pitok.cafe.models.pojo.OrdersModel;
import ir.pitok.cafe.models.pojo.PayModel;
import ir.pitok.cafe.models.pojo.PaymentModel;
import ir.pitok.cafe.models.pojo.ProfileModel;
import ir.pitok.cafe.models.pojo.SearchModel;
import ir.pitok.cafe.models.pojo.ShopDetailModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("signup")
    @FormUrlEncoded
    Call<MessageModel> signup(@Field("username") String Username,
                              @Field("pass") String pass,
                              @Field("phone_number") String phoneNumber,
                              @Field("agent") String agent,
                              @Field("fcm_token") String fcm_token);

    @POST("login")
    @FormUrlEncoded
    Call<LoginModel> login(@Field("username") String Username,
                           @Field("pass") String pass,
                           @Field("agent") String agent,
                           @Field("fcm_token") String fcm_token);

    @POST("confirmCode")
    @FormUrlEncoded
    Call<ConfirmCodeModel> confirmCode(@Field("username") String Username,
                                       @Field("confirm_code") int code);

    @POST("forgetPass")
    @FormUrlEncoded
    Call<MessageModel> forgetPass(@Field("username_or_phone_number") String UsernameOrPhoneNumebr);

    @POST("loginWithGoogle")
    @FormUrlEncoded
    Call<LoginWithGoogleModel> loginWithGoogle(@Field("username") String Username,
                                               @Field("email") String email,
                                               @Field("agent") String agent,
                                               @Field("fcm_token") String fcm_token);

    @POST("search")
    @FormUrlEncoded
    Call<SearchModel> search(@Field("api_token") String api_token,
                             @Field("search_text") String search_text);

    @POST("getNearbyCoffees")
    @FormUrlEncoded
    Call<NearbyShopsModel> getNearbyCoffees(@Field("api_token") String api_token,
                                            @Field("lat") String lat,
                                            @Field("lng") String lng);

    @POST("getShopDetails")
    @FormUrlEncoded
    Call<ShopDetailModel> getShopDetails(@Field("api_token") String api_token,
                                         @Field("shop_id") int shop_id);

    @POST("getOrdersList")
    @FormUrlEncoded
    Call<ChoosedOrdersModel> getOrdersList(@Field("api_token") String api_token,
                                           @Field("orders") String orders);

    @POST("getOrdersAmount")
    @FormUrlEncoded
    Call<PaymentModel> getOrdersAmount(@Field("api_token") String api_token,
                                       @Field("orders") String orders,
                                       @Field("discount_code") String discount_code);

    @POST("payOrderList")
    @FormUrlEncoded
    Call<PayModel> payOrderList(@Field("api_token") String api_token,
                                @Field("orders") String orders,
                                @Field("discount_code") String discount_code);

    @POST("getUserOrders")
    @FormUrlEncoded
    Call<OrdersModel> getUserOrders(@Field("api_token") String api_token);

    @POST("getMessages")
    @FormUrlEncoded
    Call<MessagesModel> getMessages(@Field("api_token") String api_token);

    @POST("editProfile")
    @FormUrlEncoded
    Call<MessageModel> editProfile(@Field("api_token") String api_token,
                                   @Field("username") String username);

    @POST("getProfile")
    @FormUrlEncoded
    Call<ProfileModel> getProfile(@Field("api_token") String api_token);

}