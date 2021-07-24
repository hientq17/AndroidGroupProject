package edu.fpt.groupproject.model.user;

import java.io.Serializable;

public class UserBooking implements Serializable {
    protected String name;
    protected String phone;
    protected String time;

    public UserBooking() {
    }

    public UserBooking(String name, String phone, String time) {
        this.name = name;
        this.phone = phone;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}