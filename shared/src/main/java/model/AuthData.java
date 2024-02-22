package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {
    private String username;

    private String authToken;


    public AuthData(String userName){
        this.username = userName;
        authToken = UUID.randomUUID().toString();
    }
    public AuthData(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }

    public String getUserName() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData auth = (AuthData) o;
        return Objects.equals(username, auth.username) && Objects.equals(authToken, auth.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authToken);
    }

    @Override
    public String toString() {
        return "Auth{" +
                "userName='" + username + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
