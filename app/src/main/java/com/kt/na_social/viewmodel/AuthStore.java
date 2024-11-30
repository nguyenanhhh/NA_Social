package com.kt.na_social.viewmodel;

import com.kt.na_social.model.User;

public class AuthStore {
    private static AuthStore I;

    private User loggedUser;

    public static AuthStore getInstance() {
        if (I == null) {
            I = new AuthStore();
        }
        return I;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
