package edu.fpt.groupproject.api;

import java.util.List;

import edu.fpt.groupproject.model.common.ReturnToken;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IApiRoom {

    @Multipart
    @POST("/api/room/test-from-flie")
    Call<ReturnToken> testUpload(@Part List<MultipartBody.Part> files);
}
