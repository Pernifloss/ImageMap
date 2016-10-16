package com.zechassault.cimi.imagemap.domain;

import android.app.Activity;

public class DemoActivity {
    String name;
    int imageResID;
    Class activity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResID() {
        return imageResID;
    }

    public void setImageResID(int imageResID) {
        this.imageResID = imageResID;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class<Activity> activity) {
        this.activity = activity;
    }

    public DemoActivity(String name, int imageResID, Class activity) {
        this.name = name;
        this.imageResID = imageResID;
        this.activity = activity;
    }
}
