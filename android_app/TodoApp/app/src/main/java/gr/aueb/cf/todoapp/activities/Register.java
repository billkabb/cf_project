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

public class Register extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText password1;
    private TextView loginHere;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.registerUsernameEt);
        password = findViewById(R.id.registerPasswordEt);
        password1 = findViewById(R.id.registerPassword1Et);
        loginHere = findViewById(R.id.loginHere);
        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(password1.getText().toString())){
                    Toast.makeText(v.getContext(), "Password not match", Toast.LENGTH_SHORT).show();
                }else{
                    if (password.getText().toString().trim().length() >5

                            && username.getText().toString().trim().length()> 3){
                        apiCall(username.getText().toString().trim(),password.getText().toString().trim());
                    }else{
                        Toast.makeText(v.getContext(), "Username or Password too short", Toast.LENGTH_SHORT).show();
                    }
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
        apiService.userRegister(myUser)
                .enqueue(new Callback<Token>() {

                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {

                        try {
                            Token myToken = response.body();
                            SavePrefs.saveToken(getContext(), myToken.getToken());
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException e){
                            Toast.makeText(Register.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(Register.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}