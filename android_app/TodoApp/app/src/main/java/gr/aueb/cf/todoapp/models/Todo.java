package gr.aueb.cf.todoapp.models;

import com.google.gson.annotations.SerializedName;

public class Todo {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("complete")
    private boolean complete;
    @SerializedName("created")
    private String created;
    @SerializedName("user")
    private int user;

    public Todo() {
    }

    public Todo(int id, String title, String description, boolean complete, String created, int user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.complete = complete;
        this.created = created;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "title='" + title + '\'' +
                '}';
    }
}
