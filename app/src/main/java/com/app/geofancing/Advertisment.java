package com.app.geofancing;

import java.io.Serializable;

/**
 * Created by beyond on 14-Apr-17.
 */

public class Advertisment implements Serializable {

    String title;
    String discription;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
