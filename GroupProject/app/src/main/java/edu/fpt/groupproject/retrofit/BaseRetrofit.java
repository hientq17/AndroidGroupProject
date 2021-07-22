package edu.fpt.groupproject.retrofit;

import edu.fpt.groupproject.constants.SystemConst;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {

    public static Retrofit RetrofitBuilder(){
        return new Retrofit.Builder()
                .baseUrl(SystemConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
