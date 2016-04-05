package pw.likemind.likemind;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

public class ChatActivity extends FirebaseLoginBaseActivity {

    private TextView mTitle;

    private ListView mChatListView;
    private EditText mInputField;
    private Button mSubmitButton;

    private Firebase mFirebaseRootRef;
    private Firebase mMessagesRef;
    private FirebaseListAdapter<Message> mFirebaseListAdapter;

    private String mAuthor;

    private boolean mLock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTitle = (TextView) findViewById(R.id.title);
        mChatListView = (ListView) findViewById(R.id.chat_list);
        mInputField = (EditText) findViewById(R.id.message_input);
        mSubmitButton = (Button) findViewById(R.id.submit_button);

        mInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mSubmitButton.callOnClick();
                return true;
            }
        });

        mFirebaseRootRef = new Firebase("https://fiery-heat-9768.firebaseio.com/"); // Create new Firebase object

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mInputField.getText().toString();

                if (messageText.trim().length() > 0) {
                    Message message = new Message(mAuthor, messageText); // create new message object
                    mMessagesRef.push().setValue(message); // push new message object
                    mInputField.setText("");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getExtras() != null) {
            mAuthor = getIntent().getExtras().getString(Constants.AUTHOR);
            createRoom(getIntent().getExtras().getString(Constants.ROOM_NAME));
        }
    }

    private void scrollListViewToBottom() {
        mChatListView.post(new Runnable() {
            @Override
            public void run() {
                mChatListView.setSelection(mFirebaseListAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected Firebase getFirebaseRef() {
        return mFirebaseRootRef;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFirebaseListAdapter != null) {
            mFirebaseListAdapter.cleanup(); // cleanup FirebaseListAdapter -- important
        }
    }

    private void createRoom(String roomName) {
        roomName = roomName.trim().toLowerCase();

        mMessagesRef = mFirebaseRootRef.child(roomName); // child of mFirebaseRootRef

        mChatListView.setVisibility(View.VISIBLE);
        mInputField.setVisibility(View.VISIBLE);
        mSubmitButton.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle("#" + roomName);

        Message message = new Message("[New User]", mAuthor + " has joined " + roomName + "!");
        mMessagesRef.push().setValue(message); // push new message object
        enterRoom();
    }

    private void enterRoom() {
        // FirebaseListAdapter listens live for data set changes and updates accordingly
        mFirebaseListAdapter = new FirebaseListAdapter<Message>(this, Message.class, android.R.layout.two_line_list_item, mMessagesRef) {
            @Override
            protected void populateView(View view, Message message, int i) {
                ((TextView) view.findViewById(android.R.id.text1)).setText(message.getAuthor());
                ((TextView) view.findViewById(android.R.id.text2)).setText(message.getMessage());
                if (mLock == true) {
                    scrollListViewToBottom();
                }
            }
        };
        mChatListView.setAdapter(mFirebaseListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_lock:
                if (mLock == true) {
                    mLock = false;
                    item.setTitle("Unlock");
                    item.setIcon(android.R.drawable.checkbox_off_background);
                } else {
                    mLock = true;
                }
                break;
        }
        return true;
    }
}
