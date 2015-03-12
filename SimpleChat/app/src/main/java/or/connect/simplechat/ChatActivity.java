package or.connect.simplechat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import or.connect.simplechat.Adapter.ChatListAdapter;
import or.connect.simplechat.Models.Message;


public class ChatActivity extends Activity {

    public static final String USER_ID_KEY = "userId";

    private EditText etMessage;
    private Button btSend;

    private ListView lvChat;
    private ArrayList<Message> chats;
    private ChatListAdapter aChats;

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;


    private static final String TAG = ChatActivity.class.getName();
    private static String sUserId;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

        // Run the runnable object every 100 ms.
        handler.postDelayed(runnable, 100);
    }

    // Define the runnable
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 100);
        }
    };

    private void refreshMessages() {
        receiveMessage();
    }


    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);

        chats = new ArrayList<Message>();
        aChats = new ChatListAdapter(ChatActivity.this, sUserId, chats);

       lvChat.setAdapter(aChats);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                Message message = new Message();
                message.setUserId(sUserId);
                message.setBody(body);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        //Toast.makeText(ChatActivity.this, "Successfully created message on Parse", Toast.LENGTH_SHORT).show();
                        receiveMessage();
                    }
                });
                etMessage.setText("");
            }
        });
    }

    // Query message from Parse and load them to the chat adapter
    private void receiveMessage() {
        //Construct a query
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        // Configure limits and sort criteria
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");

        //Run the query to fetch messages
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    chats.clear();
                    chats.addAll(messages);
                    aChats.notifyDataSetChanged();
                    //lvChat.invalidate(); // Redraw the listview - Hmmm!
                } else {
                    Log.d("messages", "Error: " + e.getMessage());
                }
            }
        });
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous login failed: " + e.toString());
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
