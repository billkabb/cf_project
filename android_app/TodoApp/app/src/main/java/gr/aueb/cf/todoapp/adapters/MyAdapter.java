package gr.aueb.cf.todoapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gr.aueb.cf.todoapp.R;
import gr.aueb.cf.todoapp.TodoDetail;
import gr.aueb.cf.todoapp.models.TodoList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private ArrayList<TodoList.TodoItem> myList;

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
        }
        holder.titleRv.setText(todoItem.title);

        // TODO: 4/7/2023 implement onClick title and delete

        holder.titleRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String title = todoItem.getTitle().toString();
//                String id = todoItem.getId().toString();
//                String description = todoItem.getDescription();
//                String complete = todoItem.getComplete();

                Intent intent = new Intent(v.getContext(), TodoDetail.class);
//                intent.putExtra("TITLE", title);
//                intent.putExtra("ID", id);
//                intent.putExtra("DESCRIPTION", description);
//                intent.putExtra("COMPLETE", complete);
                intent.putExtra("TODO",todoItem);
                v.getContext().startActivity(intent);






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
}
