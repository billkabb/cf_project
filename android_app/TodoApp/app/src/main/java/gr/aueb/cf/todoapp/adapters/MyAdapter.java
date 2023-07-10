package gr.aueb.cf.todoapp.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.aueb.cf.todoapp.helpers.APIInterface;
import gr.aueb.cf.todoapp.MainActivity;
import gr.aueb.cf.todoapp.R;
import gr.aueb.cf.todoapp.helpers.RetrofitInstance;
import gr.aueb.cf.todoapp.activities.TodoDetail;
import gr.aueb.cf.todoapp.models.Todo;
import gr.aueb.cf.todoapp.models.TodoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private ArrayList<TodoList.TodoItem> myList;
    private static String token = "";
    AlertDialog.Builder builder;


    public MyAdapter(ArrayList<TodoList.TodoItem> myList) {
        this.myList = myList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodoList.TodoItem todoItem = myList.get(position);

        if (!todoItem.complete){
            holder.completeRV.setImageResource(R.drawable.ic_not_complete);

        }else {
            holder.completeRV.setImageResource(R.drawable.ic_complete);
            holder.titleRv.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            holder.titleRv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.titleRv.setTextColor(Color.GRAY);
        }
        holder.titleRv.setText(todoItem.title);

        // TODO: 4/7/2023 implement onClick title and delete

        holder.titleRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TodoDetail.class);
                intent.putExtra("TODO",todoItem);
                v.getContext().startActivity(intent);
            }
        });

        holder.deleteRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                //Toast.makeText(v.getContext(), "Delete: "+todoItem.id, Toast.LENGTH_SHORT).show();
                builder.setMessage(Html.fromHtml("<font color='#5A5A5A'>Are you sure you want to delete "+"<b>\'"+todoItem.title+"\'</b></font>", Html.FROM_HTML_MODE_LEGACY))
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#FF6B35'>Yes</font>", Html.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                apiCall(token,todoItem, holder.getAdapterPosition());
//                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#FF6B35'>No</font>", Html.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(v.getContext(),"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(Html.fromHtml("<font color='#5A5A5A'>Todo Delete</font>", Html.FROM_HTML_MODE_LEGACY));
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView completeRV;
        private TextView titleRv;
        private ImageView deleteRV;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            completeRV = itemView.findViewById(R.id.completeRV);
            titleRv = itemView.findViewById(R.id.titleRV);
            deleteRV = itemView.findViewById(R.id.deleteRV);
        }
    }
    public void apiCall(String token, TodoList.TodoItem item, int pos){
        token = "token "+MainActivity.getToken().getToken();
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        // create interface
        APIInterface apiService = retrofit.create(APIInterface.class);
        // new
        Todo myTodo = new Todo(item.id,item.title,item.description,item.complete, item.created, item.user);
        apiService.deleteTodo(token,String.valueOf(myTodo.getId()))
                .enqueue(new Callback<Todo>() {
                    @Override
                    public void onResponse(Call<Todo> call, Response<Todo> response) {
                        notifyItemRemoved(pos);
                    }
                    @Override
                    public void onFailure(Call<Todo> call, Throwable t) {
                    }
                });

    }
}
