package edu.fpt.groupproject.api;

import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.common.ReturnToken;
import edu.fpt.groupproject.model.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IUserApi {
    @POST("/api/user/login")
    Call<ReturnToken> login(@Body User user);
    @POST("/api/user/signup")
    Call<ReturnModel> signup(@Body User user);
    @GET("/api/user/get-user-by-username")
    Call<User> getUser(@Query("username") String username, @Query("token") String token);
}
