package com.android.freelance.javaretrofit.data.network.responses;

import com.android.freelance.javaretrofit.data.network.responses.model.User;

import java.util.List;

public class UsersResponse {

    private boolean error;
    private List<User> users;

    public UsersResponse(boolean error, List<User> users) {
        this.error = error;
        this.users = users;
    }

    public boolean isError() {
        return error;
    }

    public List<User> getUsers() {
        return users;
    }
}
