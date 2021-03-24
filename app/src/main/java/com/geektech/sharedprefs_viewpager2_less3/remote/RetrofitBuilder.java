package com.geektech.sharedprefs_viewpager2_less3.remote;

import com.geektech.sharedprefs_viewpager2_less3.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private RetrofitBuilder(){}

    private static API instance;

    public static API getInstance(){
        if (instance == null){
            instance = createAPI();
        }
        return  instance;
    }

    private static API createAPI() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(API.class);
    }
}
