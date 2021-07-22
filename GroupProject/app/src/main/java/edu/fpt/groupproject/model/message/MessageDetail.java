package edu.fpt.groupproject.model.message;

public class MessageDetail {
    public int id;
    public int inbox_id;
    public String content;
    public String type;
    public String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInbox_id() {
        return inbox_id;
    }

    public void setInbox_id(int inbox_id) {
        this.inbox_id = inbox_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
