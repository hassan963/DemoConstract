package com.constructefile.democonstract.content;

/**
 * Created by Hassan M.Ashraful on 12/22/2016.
 */

public class SpinnerData {

    String id, model, serial;

    public SpinnerData(String id, String model, String serial) {
        this.id = id;
        this.model = model;
        this.serial = serial;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
