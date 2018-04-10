package com.techkshetrainfo.cltenotes.Api;

import com.techkshetrainfo.cltenotes.Models.LoginResponse;
import com.techkshetrainfo.cltenotes.Models.NotificationResponse;
import com.techkshetrainfo.cltenotes.Models.PaymentResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by savsoft-3 on 17/12/16.
 */

public interface ApiInterface {


//    @GET("get_magazines/{year}")
//    Call<MagazineYear> get_amgazine_year(@Path("year") int year);
//
//    @GET("get_magazines/")
//    Call<MagazineResponse> get_amgazine();

    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> login_user(@Field("email") String useremail, @Field("password") String password);

    @FormUrlEncoded
    @POST("register/")
    Call<LoginResponse> register_user(@Field("email") String useremail, @Field("password") String password, @Field("full_name") String name, @Field("mobile_no") String phone);

    @FormUrlEncoded
    @POST("payment_history/")
    Call<PaymentResponse> get_payment(@Field("email") String useremail);

//    @FormUrlEncoded
//    @POST("get_reviews/")
//    Call<ReviewDataResposnse> get_reviews(@Field("mid") String mid);

    @FormUrlEncoded
    @POST("add_rating/")
    Call<LoginResponse> post_reviews(@Field("mid") String mid,@Field("email") String user_email, @Field("review") String review, @Field("rating") String rating);

//    @GET("get_years")
//    Call<MagazineYears> get_magazineYears();

    @GET("get_notifications/")
    Call<NotificationResponse> getnotification();

    @GET("addview//{mid}")
    Call<LoginResponse> update_view(@Path("mid") String value);

    @FormUrlEncoded
    @POST("change_password/")
    Call<LoginResponse> change_password(@Field("email") String user_email,@Field("old_password") String old_password,@Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("reset_password/")
    Call<LoginResponse> reset_password(@Field("email") String email);
}
