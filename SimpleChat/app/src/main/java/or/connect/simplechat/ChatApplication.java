package or.connect.simplechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import or.connect.simplechat.Models.Message;

/**
 * Created by moulib on 3/8/15.
 */
public class ChatApplication extends Application {

    public static final String APP_ID="Isp6xt0kmUD7raPZVqrWsraru2OQp18CQiMUtC62";
    public static final String CLIENT_KEY = "nno1kv4VY4yyFvR6ULE17bZJhB6GuOvXpSDTeFpW";

    @Override
    public void onCreate() {
        super.onCreate();

        // Init Code
        Parse.enableLocalDatastore(this);
        // Register the data model with parse
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this, APP_ID, CLIENT_KEY);
    }
}