package edu.fpt.groupproject.api;

import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.room.AddOrUpdateRoom;
import edu.fpt.groupproject.model.room.RoomBase64;
import edu.fpt.groupproject.model.room.Room;
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
    @GET("/api/room/get-list-booked-rooms-by-username")
    Call<List<Room>> getListBookedRoomsByUsername(@Query("username") String username, @Query("token") String token);
    @GET("/api/room/get-list-rooms-by-author")
    Call<List<Room>> getListRoomsByAuthor(@Query("author") String author, @Query("token") String token);
    @POST("/api/room/insert-or-update-room")
    Call<ReturnModel> insertOrUpdateRoom(@Body AddOrUpdateRoom room, @Query("token") String token);
    @GET("/api/room/get-room-by-id")
    Call<Room> getRoomById(@Query("id") int id);
    @POST("/api/room/delete-room")
    Call<ReturnModel> deleteRoom(@Query("id") int id, @Query("token") String token);
}
