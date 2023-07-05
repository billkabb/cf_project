package gr.aueb.cf.todoapp;

import java.util.List;

import gr.aueb.cf.todoapp.models.Todo;
import gr.aueb.cf.todoapp.models.TodoList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface APIInterface {
    @GET("api/")
    Call<TodoList> getTodoList(@Header("Authorization") String token );

//    @PATCH("api/{id}/")
//    @FormUrlEncoded
//    Call<TodoList.TodoItem> patchTodo(@Header("Authorization") String token,
//                                      @Path("id") String id,
//                                      @Field("title") String title,
//                                      @Field("description") String description,
//                                      @Field("complete") boolean complete);
    @PATCH("api/{id}/")

    Call<Todo> patchTodo(@Header("Authorization") String token,
                         @Path("id") String id,
                         @Body Todo todo);


}
