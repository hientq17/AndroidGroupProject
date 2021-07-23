package edu.fpt.groupproject.model.common;

public class ReturnToken {
    protected ReturnModel returnModel;
    protected String token;

    public ReturnToken() {
    }

    public ReturnModel getReturnModel() {
        return returnModel;
    }

    public void setReturnModel(ReturnModel returnModel) {
        this.returnModel = returnModel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
