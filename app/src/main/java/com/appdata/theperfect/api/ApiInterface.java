package com.appdata.theperfect.api;


import com.appdata.theperfect.model.ResponseAppointmentTimeData;
import com.appdata.theperfect.model.ResponseCategoryData;
import com.appdata.theperfect.model.ResponseCountryData;
import com.appdata.theperfect.model.ResponseDefaultData;
import com.appdata.theperfect.model.ResponseOauthLogin;
import com.appdata.theperfect.model.ResponseRatingData;
import com.appdata.theperfect.model.ResponseSaveAppointment;
import com.appdata.theperfect.model.ResponseSellerListData;
import com.appdata.theperfect.model.ResponseUpdateAppointment;
import com.appdata.theperfect.model.ResponseZipData;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;

public interface ApiInterface {

    String BASE_URL = "https://mobile.trivaan.com/public/api/";

    @POST("user/signup")
    Call<ResponseOauthLogin>register(@Body RequestBody params);
    
    @POST("user/login")
    Call<ResponseOauthLogin>login(@Body RequestBody params);

    @POST("category/getcategory")
    Call<ResponseCategoryData>categoryData(@Body RequestBody params);

    @POST("getsellerlist")
    Call<ResponseSellerListData>getSellerList(@Body RequestBody params);

    @Multipart
    @POST("getappointmentdetails")
    Call<ResponseSellerListData>getAppointmentList(@PartMap() HashMap<String, RequestBody> partMa);

    @Multipart
    @POST("getdealdone")
    Call<ResponseSellerListData>getDealAppointmentList(@PartMap() HashMap<String, RequestBody> partMa);

    @Multipart
    @POST("zipcodeseller")
    Call<ResponseSellerListData>getListUsingZip(@PartMap() HashMap<String, RequestBody> partMa);

    @GET("appointmentdatetime")
    Call<ResponseAppointmentTimeData>appointmentDatetime();

    @GET("getzipcode")
    Call<ResponseZipData>getZipData();

    @POST("saveappointment")
    Call<ResponseSaveAppointment>saveappointment(@Body RequestBody params);

    @PUT("updateappointment")
    Call<ResponseUpdateAppointment>updateAppointment(@Body RequestBody params);

    @GET("getlocation")
    Call<ResponseCountryData>getlocation();

    @POST("api/SignUp/GetSignUpRecords")
    Call<ResponseOauthLogin>getSignUpBasic(@Body RequestBody params);

    @Multipart
    @POST("saveapprating")
    Call<ResponseRatingData>SaveRating(@PartMap() HashMap<String, RequestBody> partMa);

    @PUT("updateuser")
    Call<ResponseOauthLogin>updateUser(@Body RequestBody params);

    @POST("forgetpassword")
    Call<ResponseDefaultData>forgotPassword(@Body RequestBody params);

}
