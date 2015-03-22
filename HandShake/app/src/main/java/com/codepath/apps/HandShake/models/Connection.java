package com.codepath.apps.HandShake.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by moulib on 2/21/15.
 */
@Table(name = "Connections")
public class Connection extends Model {
    // display_tweets attributes
    @Column(name = "uId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String uId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "industry")
    private String industry;

    @Column(name = "description")
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    public String getuId() {
        return uId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIndustry() {
        return industry;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    // Default Constructor
    public Connection() {
        super();
    }

    // De-serialize the json into the Java Object
    public static Connection connFromJsonObj(JSONObject connJson) {
        Connection connection = new Connection();

        try {
            connection.firstName = connJson.getString("firstName");
            connection.lastName = connJson.getString("lastName");
            connection.industry = connJson.getString("industry");
            try {
                connection.imageUrl = connJson.getString("pictureUrl");
            } catch (JSONException e) {
                connection.imageUrl = "";
            }
            connection.uId = connJson.getString("id");
            connection.description = connJson.getString("headline");

       } catch (JSONException e) {
           e.printStackTrace();
           return null;
       }
       return connection;
   }

   // Return a set of tweets from a JSON array
   public static ArrayList<Connection> fromJsonArray(JSONArray jsonArr) {
       ArrayList<Connection> arrList = new ArrayList<>();

       for (int i=0; i < jsonArr.length(); i++) {
           try {
               JSONObject jobj= jsonArr.getJSONObject(i);
               Connection connection = Connection.connFromJsonObj(jobj);
               if (connection != null) {
                   arrList.add(connection);
               }
           } catch (JSONException e) {
               e.printStackTrace();
               continue; // if one display_tweets fails, continue parsing the rest!
           }
       }
       return arrList;
   }
}
