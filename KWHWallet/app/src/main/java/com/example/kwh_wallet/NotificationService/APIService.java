package com.example.kwh_wallet.NotificationService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA_Gth1k4:APA91bEoQj3rrz3QtJvMBxUVGF2qYHCuQFwtzmyCh1p4Vdfv9xWwXZ0J9sXTzhbYzMWbAPiRIODW5YhmRvvUPSRu2UyQ0FOgO77lSXCoBEHeZ4_R6NgjT4nRC0uFqz-KonKQd4lIDb3o"
    })

    @POST("cm/send")
    Call<Response> sendNotification(@Body Sender body);
}
