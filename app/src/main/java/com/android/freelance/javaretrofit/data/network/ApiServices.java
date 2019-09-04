package com.android.freelance.javaretrofit.data.network;

import com.android.freelance.javaretrofit.data.network.responses.DefaultResponse;
import com.android.freelance.javaretrofit.data.network.responses.LogInResponse;
import com.android.freelance.javaretrofit.data.network.responses.UsersResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServices {

    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("school") String school);


    @FormUrlEncoded
    @POST("userlogin")
    Call<LogInResponse> userLogIn(
            @Field("email") String email,
            @Field("password") String password);

    @GET("allusers")
    Call<UsersResponse> fetchAllUsers();

    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Call<LogInResponse> updateUser(
            @Path("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("school") String school);

    @FormUrlEncoded
    @PUT("updatepassword")
    Call<DefaultResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("email") String email);

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteUser(
            @Path("id") int id);
}
