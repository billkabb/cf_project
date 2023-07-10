package gr.aueb.cf.todoapp.helpers;

import java.util.List;

import gr.aueb.cf.todoapp.models.Todo;
import gr.aueb.cf.todoapp.models.TodoList;
import gr.aueb.cf.todoapp.models.Token;
import gr.aueb.cf.todoapp.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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


    @PATCH("api/{id}/")

    Call<Todo> patchTodo(@Header("Authorization") String token,
                         @Path("id") String id,
                         @Body Todo todo);

    @POST("api/")
    Call<Todo> postTodo(@Header("Authorization") String token ,
                        @Body Todo todo);

    @DELETE("api/{id}/")
    Call<Todo> deleteTodo(@Header("Authorization") String token ,
                          @Path("id") String id);

    @POST("api/login/")
    Call<Token> userLogin(@Body User user);

    @POST("api/register/")
    Call<Token> userRegister(@Body User user);

}
