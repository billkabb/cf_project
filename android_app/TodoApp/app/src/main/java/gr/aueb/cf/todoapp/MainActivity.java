package gr.aueb.cf.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gr.aueb.cf.todoapp.adapters.MyAdapter;
import gr.aueb.cf.todoapp.models.TodoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class MainActivity extends AppCompatActivity {
    static String token = "token c93749a397ad7c4b25022f102d63c8646e86cd89";
    private TextView mTextView;
    ArrayList<TodoList.TodoItem> data;
    private RecyclerView recyclerView;
    static MyAdapter myAdapter;
    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);


        apiCall(token);

        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(30000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                apiCall(token);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

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

    @Override
    protected void onResume() {
        super.onResume();
        apiCall(token);
    }
}