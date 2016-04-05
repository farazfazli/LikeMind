package pw.likemind.likemind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

import pw.likemind.likemind.auth.MeetupAuthActivity;
import pw.likemind.likemind.interfaces.MeetupApiClient;
import pw.likemind.likemind.model.MeetupMemberResponse;
import pw.likemind.likemind.model.MemberInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getCanonicalName();

    private String mMemberId;

    private TextView mTitle;
    private TextView mSubtitle;

    private Button mMeetupButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ArrayList<String> mGroupsArrayList;
    private ArrayAdapter<String> mGroupsArrayAdapter;
    private ListView mGroupsListView;

    private HashMap<String, Boolean> mGroups;

    private Firebase mFirebaseRef;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView) findViewById(R.id.title);
        mSubtitle = (TextView) findViewById(R.id.subtitle);

        mFirebaseRef = new Firebase(Constants.FIREBASE_BASE_URL);

        mGroupsArrayList = new ArrayList<>();
        mGroupsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mGroupsArrayList);
        mGroupsListView = (ListView) findViewById(R.id.groupsList);

        mGroupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String group = ((TextView) view).getText().toString();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.GROUP_NAME_SELECTED, group);
                intent.putExtra(Constants.MEMBER_ID, mMemberId);
                startActivity(intent);
            }
        });

        mGroupsListView.setAdapter(mGroupsArrayAdapter);

        sharedPreferences = getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mMeetupButton = (Button) findViewById(R.id.meetupButton);
        mMeetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(Constants.FIRST_TIME, true); // Button has been pressed at least once
                editor.apply();
                Intent intent = new Intent(MainActivity.this, MeetupAuthActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MEETUP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MeetupApiClient client = retrofit.create(MeetupApiClient.class);


        String token = sharedPreferences.getString(MeetupAuthActivity.TOKEN, "");

        Call<MeetupMemberResponse> call = client.getUser(token);
        call.enqueue(new Callback<MeetupMemberResponse>() {
            @Override
            public void onResponse(Call<MeetupMemberResponse> call, Response<MeetupMemberResponse> response) {
                Log.i(TAG, "Received!");
                if (response.body() != null && response.body().getTopics() != null) {
                    mMeetupButton.setVisibility(View.INVISIBLE);
                    mGroupsListView.setVisibility(View.VISIBLE);
                    mTitle.animate().translationY(-mSubtitle.getHeight() - 20);
                    mSubtitle.animate().translationY(-mSubtitle.getHeight() - 20);
                    mMemberId = response.body().getId().toString();

                    HashMap<String, Boolean> t = new HashMap<String, Boolean>();

                    t.put(mMemberId, true);
                    MemberInfo memberInfo = new MemberInfo(response.body().getName(), mMemberId, response.body().getPhoto().getThumbLink());
                    mGroups = new HashMap<>();
                    for (int i = 0; i < response.body().getTopics().size(); i++) {
                        String name = response.body().getTopics().get(i).getName();
                        Log.e(TAG, name);
                        mGroups.put(name, true);
                        mFirebaseRef.child("groups").child(name).child("members").setValue(t);
                        mGroupsArrayList.add(name);
                        mFirebaseRef.child("info").child(mMemberId).setValue(memberInfo);
                    }
                    mGroupsArrayAdapter.notifyDataSetChanged();
                    mFirebaseRef.child("users").child(mMemberId).setValue(mGroups);
                    mFirebaseRef.push();
                    Log.i(TAG, response.raw().toString());
                } else {
                    Log.i(TAG, "Received -- Rollover");
                    Intent intent = new Intent(MainActivity.this, MeetupAuthActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<MeetupMemberResponse> call, Throwable t) {
                Log.e(TAG, "Failure!");
                t.printStackTrace();
                if ((sharedPreferences.getBoolean(Constants.FIRST_TIME, false))) {
                    Log.e(TAG, "Failure -- Rollover");
                    Intent intent = new Intent(MainActivity.this, MeetupAuthActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
}