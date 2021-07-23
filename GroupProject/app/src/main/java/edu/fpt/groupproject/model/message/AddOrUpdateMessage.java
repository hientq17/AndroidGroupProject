package edu.fpt.groupproject.model.message;

public class AddOrUpdateMessage {
    public int inboxId;
    public String content;
    public String type;
    public String time;

    public AddOrUpdateMessage(int inboxId, String content, String type, String time) {
        this.inboxId = inboxId;
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public int getInboxId() {
        return inboxId;
    }

    public void setInboxId(int inboxId) {
        this.inboxId = inboxId;
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
