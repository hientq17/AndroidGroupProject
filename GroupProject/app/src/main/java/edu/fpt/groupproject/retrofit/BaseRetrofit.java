package edu.fpt.groupproject.retrofit;

import edu.fpt.groupproject.constant.SysConstant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {

    public static Retrofit RetrofitBuilder(){
        return new Retrofit.Builder()
                .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
