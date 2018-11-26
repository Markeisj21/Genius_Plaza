package com.markeisjones.myusers.models;

public class UserModel {

    private String mId;
    private String mFirstname;
    private String mLastname;
    private String mImageurl;



    public UserModel(String id, String firstname, String lastname, String imageurl) {
        mId = id;
        mFirstname = firstname;
        mLastname = lastname;
        mImageurl = imageurl;
    }

    public String getId() {
        return mId;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public String getImageurl() {
        return mImageurl;
    }


}

