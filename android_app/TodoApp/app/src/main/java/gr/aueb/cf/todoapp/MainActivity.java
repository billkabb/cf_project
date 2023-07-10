package gr.aueb.cf.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import gr.aueb.cf.todoapp.activities.Login;
import gr.aueb.cf.todoapp.activities.TodoAdd;
import gr.aueb.cf.todoapp.adapters.MyAdapter;
import gr.aueb.cf.todoapp.helpers.APIInterface;
import gr.aueb.cf.todoapp.helpers.RetrofitInstance;
import gr.aueb.cf.todoapp.helpers.SavePrefs;
import gr.aueb.cf.todoapp.models.TodoList;
import gr.aueb.cf.todoapp.models.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    static String token ;//= "token c93749a397ad7c4b25022f102d63c8646e86cd89";
    private TextView mTextView;
    private TextView mLogout;
    private ImageButton mImageBtn;
    ArrayList<TodoList.TodoItem> data;
    private RecyclerView recyclerView;
    private  MyAdapter myAdapter;
    private static Token mToken;
    Thread thread;
    int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SavePrefs.getToken(this).equals("")){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
//            thread.interrupt();
            finish();
        }
        mToken = new Token(SavePrefs.getToken(this));
        mTextView = findViewById(R.id.textView);
        mLogout = findViewById(R.id.logoutTv);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePrefs.saveToken(getContext(),"");
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                thread.interrupt();
                finish();

            }
        });
        mImageBtn = findViewById(R.id.imageBtn);
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Click!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TodoAdd.class);
                startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        if (!SavePrefs.getToken(this).equals("")) {
            apiCall("token "+SavePrefs.getToken(this));
//         overkill?
            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!thread.isInterrupted()) {
                            Thread.sleep(30000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    apiCall("token "+SavePrefs.getToken(getContext()));
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

        thread.start();
        }

    }
    public Context getContext() {
        return this;
    }
    public void apiCall(String token){
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        // create interface
        APIInterface apiService = retrofit.create(APIInterface.class);
        apiService.getTodoList(token).enqueue(new Callback<TodoList>() {
            @Override
            public void onResponse(Call<TodoList> call, Response<TodoList> response) {
                TodoList resp = response.body();
                String username = resp.username;
//                List<TodoList.TodoItem> data = resp.data;
                data = new ArrayList<>(resp.data);
//                recyclerView = findViewById(R.id.recyclerView);
                myAdapter = new MyAdapter(data);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myAdapter);

                String result = username;

                mTextView.setText("Hello " + result);



            }

            @Override
            public void onFailure(Call<TodoList> call, Throwable t) {
                mTextView.setText(t.toString());
            }
        });


    }

    // make api call after alertDialog
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        apiCall("token "+mToken.getToken());

//        myAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE) {
//            if(resultCode == Activity.RESULT_OK){
////                String result=data.getStringExtra("result");
//
//                Token tempToken = (Token) data.getSerializableExtra("result");
//                mToken.setToken(tempToken.getToken());
//                Toast.makeText(this, ""+mToken.getToken(), Toast.LENGTH_SHORT).show();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                // Write your code if there's no result
//            }
//        }
//    }
    public static Token getToken(){
        return mToken;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SavePrefs.getToken(this).equals("")){
            //Toast.makeText(this, SavePrefs.getToken(this), Toast.LENGTH_SHORT).show();
        }
    }
}