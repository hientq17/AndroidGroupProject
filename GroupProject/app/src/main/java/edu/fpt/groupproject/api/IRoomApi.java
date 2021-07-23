package edu.fpt.groupproject.api;

import edu.fpt.groupproject.model.Room;
import edu.fpt.groupproject.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface IRoomApi {
//    @POST("/api/user/login")
//    Call<Object> login(@Body User user);
//    @POST("/api/user/signup")
//    Call<Object> signup(@Body User user);
//    @GET("/api/user/get-user-by-username")
//    Call<User> getUser(@Query("username") String username, @Query("token") String token);
    @GET("/api/room/get-top-rooms")
    Call<List<Room>> getTopRooms();
    @GET("/api/room/get-list-all-rooms")
    Call<List<Room>> getListAllRooms();
}
