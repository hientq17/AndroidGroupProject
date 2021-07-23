package edu.fpt.groupproject.model.book;

public class AddOrUpdateBook {
    protected Book JInput;
    protected String Action;

    public AddOrUpdateBook() {
    }

    public AddOrUpdateBook(Book JInput, String action) {
        this.JInput = JInput;
        Action = action;
    }

    public Book getJInput() {
        return JInput;
    }

    public void setJInput(Book JInput) {
        this.JInput = JInput;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

}
