package edu.fpt.groupproject.model.book;

import java.io.Serializable;

public class Book implements Serializable {
    protected int id;
    protected String userId;
    protected int roomId;
    protected String time;
    protected String phone;
    protected String note;
    protected boolean status = true;

    public Book() {
    }

    public Book(String userId, int roomId, String time, String phone, String note) {
        this.userId = userId;
        this.roomId = roomId;
        this.time = time;
        this.phone = phone;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
