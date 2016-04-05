package pw.likemind.likemind;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Faraz on 4/4/16.
 */
public class MeetupActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
