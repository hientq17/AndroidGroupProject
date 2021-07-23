package edu.fpt.groupproject.api;

import java.util.List;

import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.book.AddOrUpdateBook;
import edu.fpt.groupproject.model.user.UserBooking;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IBookApi {
//    @POST("/api/user/login")
//    Call<Object> login(@Body User user);
//    @POST("/api/user/signup")
//    Call<Object> signup(@Body User user);
    @POST("/api/book/insert-or-update-book")
    Call<ReturnModel> insertOrUpdateBook(@Body AddOrUpdateBook addOrUpdateBook, @Query("token") String token);
    @GET("/api/book/get-list-user-booking")
    Call<List<UserBooking>> getListUserBooking(@Query("roomId") int roomId, @Query("token") String token);
}
