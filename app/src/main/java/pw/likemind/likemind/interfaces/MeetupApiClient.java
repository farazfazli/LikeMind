package pw.likemind.likemind.interfaces;

import pw.likemind.likemind.model.MeetupMemberResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Faraz on 3/25/16.
 */
public interface MeetupApiClient {
    @GET("member/self/")
    Call<MeetupMemberResponse> getUser(@Query("access_token") String access_token);
}
