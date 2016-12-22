package com.hassanmashraful.democonstract.Content;

/**
 * Created by Hassan M.Ashraful on 11/16/2016.
 */

public class FormData {

    private String title, comment;
    private Boolean status;

    public FormData(String title){
        this.title = title;
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
}
