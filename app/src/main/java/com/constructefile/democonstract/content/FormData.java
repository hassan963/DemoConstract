package com.constructefile.democonstract.content;

/**
 * Created by Hassan M.Ashraful on 11/16/2016.
 */

public class FormData {

    private String title, comment;
    private Boolean status;
    private int id;

    public FormData(String title, int id){
        this.title = title;
        this.id = id;
        this.comment = "";
        this.status = false;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
