package edu.fpt.groupproject.api;

import java.util.List;

import edu.fpt.groupproject.model.message.AddOrUpdateMessage;
import edu.fpt.groupproject.model.message.MessageDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IApiMessage {

    @GET("message/get-list-messages-by-inbox")
    Call<List<MessageDetail>> getListMessagesByInbox(@Query("inbox") int inboxId,
                                                     @Query("token") String token);

    @GET("message/get-list-messages-by-id")
    Call<List<MessageDetail>> getListMessagesById(@Query("id") int id,
                                                  @Query("token") String token);

    @GET("message/insert-or-update-message")
    Call<List<MessageDetail>> insertOrUpdateMessage(@Body AddOrUpdateMessage model,
                                                    @Query("token") String token);

    @GET("message/delete-message")
    Call<List<MessageDetail>> deleteMessage(@Query("id") int id,
                                            @Query("token") String token);
}
