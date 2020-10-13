package com.saman.groceryshopping.retrofit;

import com.saman.groceryshopping.Models.MDForgetPass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroServices {

    @FormUrlEncoded
    @POST("password/reset")
    Call<MDForgetPass> resetPass(@Field("phone") String phone,
                                 @Field("password") String password);
}
