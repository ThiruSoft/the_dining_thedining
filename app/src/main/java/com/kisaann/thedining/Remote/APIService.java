package com.kisaann.thedining.Remote;

import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyNewResponce;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAOb4uy1Y:APA91bEqTfVwcPRfZ3Dcl1fNHDUPEZm-esubrYt628tY6hy8tfLp6qWULnzaT4XeJLwkwB0IhUhLZc3TnxlCI313E2dtH6VLBYa0ft_ABkh4ULVVpZnuZUU_pJSOCSTSkFMu1EEptJcr"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);


    @POST("fcm/send")
    Call<MyNewResponce> sendNewNotification(@Body Sender body);
    // https://fcm.googleapis.com/
   /* @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);*/
}
