package com.hassanmashraful.democonstract.Content;

/**
 * Created by Hassan M.Ashraful on 12/22/2016.
 */

public class SpinnerData {

    String model, serial;

    public SpinnerData(String model, String serial) {
        this.model = model;
        this.serial = serial;
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
