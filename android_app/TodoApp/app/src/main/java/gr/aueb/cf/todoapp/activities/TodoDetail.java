package gr.aueb.cf.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import gr.aueb.cf.todoapp.helpers.APIInterface;
import gr.aueb.cf.todoapp.MainActivity;
import gr.aueb.cf.todoapp.R;
import gr.aueb.cf.todoapp.helpers.RetrofitInstance;
import gr.aueb.cf.todoapp.models.Todo;
import gr.aueb.cf.todoapp.models.TodoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TodoDetail extends AppCompatActivity {

    private EditText titleET;

    private EditText descriptionET;
    private CheckBox completeCB;
    private Button submitBtn;

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

        Intent intent = getIntent();
        TodoList.TodoItem myTodo = (TodoList.TodoItem) intent.getSerializableExtra("TODO");


        int id = myTodo.getId();
        String title = myTodo.getTitle();
        String description = myTodo.getDescription();
        boolean complete = myTodo.getComplete();

        titleET.setText(title);
        descriptionET.setText(description);
        if (complete){
            completeCB.setChecked(true);
        }else {completeCB.setChecked(false);}

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TodoDetail.this, "Todo id: "+id, Toast.LENGTH_SHORT).show();
                myTodo.setTitle(titleET.getText().toString());
                myTodo.setDescription(descriptionET.getText().toString());
                myTodo.setComplete(completeCB.isChecked());
                // TODO: 5/7/2023 send the post response
                apiCall(token,myTodo);

            }
        });
    }
    public void apiCall(String token, TodoList.TodoItem item){
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        // create interface
        //Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show();
        APIInterface apiService = retrofit.create(APIInterface.class);
        // new
        Todo myTodo = new Todo(item.id,item.title,item.description,item.complete, item.created, item.user);
        apiService.patchTodo(token,String.valueOf(item.id),myTodo)
                .enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {
                        //Toast.makeText(TodoDetail.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}