package gr.aueb.cf.todoapp.helpers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePrefs {

    public static void saveToken(Context con,String token){
        SharedPreferences sharedPreferences = con.getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("token", token);
        myEdit.apply();

    }
    public static String getToken(Context con){
        SharedPreferences sharedPreferences = con.getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }
}
