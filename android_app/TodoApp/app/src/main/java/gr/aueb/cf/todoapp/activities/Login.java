package gr.aueb.cf.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gr.aueb.cf.todoapp.helpers.APIInterface;
import gr.aueb.cf.todoapp.MainActivity;
import gr.aueb.cf.todoapp.R;
import gr.aueb.cf.todoapp.helpers.RetrofitInstance;
import gr.aueb.cf.todoapp.helpers.SavePrefs;
import gr.aueb.cf.todoapp.models.Token;
import gr.aueb.cf.todoapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {
    EditText usernameET;
    EditText passwordET;
    Button loginBtn;
    String username;
    String password;
    TextView registerHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameET = findViewById(R.id.loginUsernameEt);
        passwordET = findViewById(R.id.loginPasswordEt);
        loginBtn = findViewById(R.id.loginBtn);
        registerHere = findViewById(R.id.registerHere);
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameET.getText().toString().equals("") || passwordET.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "Please enter Username and Password", Toast.LENGTH_SHORT).show();
                } else {
                    username = usernameET.getText().toString();
                    password = passwordET.getText().toString();
                    apiCall(username, password);
                }
            }
        });
    }

    public Context getContext() {
        return this;
    }

    public void apiCall(String user, String pass) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        // create interface
        APIInterface apiService = retrofit.create(APIInterface.class);
        // new
        User myUser = new User(user, pass);
        apiService.userLogin(myUser)
                .enqueue(new Callback<Token>() {

                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {

                        try {
                            Token myToken = response.body();
                            SavePrefs.saveToken(getContext(), myToken.getToken());
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException e){
                            Toast.makeText(Login.this, "Wrong Username Or Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(Login.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}