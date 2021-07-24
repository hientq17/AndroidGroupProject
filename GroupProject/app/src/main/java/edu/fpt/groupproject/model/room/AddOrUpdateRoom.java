package edu.fpt.groupproject.model.room;

import edu.fpt.groupproject.model.book.Book;

public class AddOrUpdateRoom {
    protected RoomBase64 JInput;
    protected String Action;

    public RoomBase64 getJInput() {
        return JInput;
    }

    public void setJInput(RoomBase64 JInput) {
        this.JInput = JInput;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public AddOrUpdateRoom(RoomBase64 JInput, String action) {
        this.JInput = JInput;
        Action = action;
    }
}
