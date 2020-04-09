package com.httpslibrary;



import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HttpClient {

    class Builder {
        public static HttpClient getHttpService() {
            return HttpUtils.getInstance().getBuilder("").build().create(HttpClient.class);
        }
    }

    @FormUrlEncoded
    @POST("/zntk/app/verify")
    Call<ResponseBody> getAppVerify(@FieldMap Map<String, String> map);



    @Multipart
    @POST("/file/upload")
    Observable<ResponseBody> uploadFile(@Part List<MultipartBody.Part> requestBodyMap);



}