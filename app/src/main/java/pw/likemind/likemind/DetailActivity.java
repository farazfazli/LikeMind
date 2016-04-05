package pw.likemind.likemind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private String name;
    private String id;

    private Firebase mFirebaseRef;

    private TextView mTitle;
    private ImageView mProfilePicture;

    private Button mYesButton;
    private Button mNoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.name);
        mProfilePicture = (ImageView) findViewById(R.id.thumbnail);

        mYesButton = (Button) findViewById(R.id.yes);
        mNoButton = (Button) findViewById(R.id.no);

        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SKIP USER
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            name = bundle.getString(Constants.GROUP_NAME_SELECTED);
            id = bundle.getString(Constants.MEMBER_ID);
        }

        mFirebaseRef = new Firebase(Constants.FIREBASE_BASE_URL).child("groups").child(name).child("members");
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                populateData(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void populateData(String key) {
        mFirebaseRef = new Firebase(Constants.FIREBASE_BASE_URL);
        mFirebaseRef.child("info").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String thumbnailLink = (String) dataSnapshot.child("thumbnailLink").getValue();
                String title = (String) dataSnapshot.child("name").getValue();

                System.out.println(mFirebaseRef.getKey());


                mTitle.setText(title);
                Picasso.with(DetailActivity.this).load(thumbnailLink).into(mProfilePicture);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
}
}