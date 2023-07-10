package gr.aueb.cf.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import gr.aueb.cf.todoapp.helpers.APIInterface;
import gr.aueb.cf.todoapp.MainActivity;
import gr.aueb.cf.todoapp.R;
import gr.aueb.cf.todoapp.helpers.RetrofitInstance;
import gr.aueb.cf.todoapp.models.Todo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TodoAdd extends AppCompatActivity {
    private EditText titleET;

    private EditText descriptionET;
    private CheckBox completeCB;
    private Button submitBtn;
    private Todo myTodo = new Todo();

    static String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        titleET = findViewById(R.id.titleET);
        descriptionET = findViewById(R.id.descriptionET);
        completeCB = findViewById(R.id.completeCB);
        submitBtn = findViewById(R.id.submitBTN);
        token = "token "+ MainActivity.getToken().getToken();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TodoDetail.this, "Todo id: "+id, Toast.LENGTH_SHORT).show();
                myTodo.setTitle(titleET.getText().toString());
                myTodo.setDescription(descriptionET.getText().toString());
                myTodo.setComplete(completeCB.isChecked());
                // TODO: 5/7/2023 send the post response
                apiCall(token);

            }
        });
    }
    public void apiCall(String token){
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        APIInterface apiService = retrofit.create(APIInterface.class);
        apiService.postTodo(token,myTodo)
                .enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {

                    }
                });

    }
}
