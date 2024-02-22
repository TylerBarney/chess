package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {
    private String userName;
    private String authToken;

    public AuthData(String userName){
        this.userName = userName;
        authToken = UUID.randomUUID().toString();
    }

    public String getUserName() {
        return userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData auth = (AuthData) o;
        return Objects.equals(userName, auth.userName) && Objects.equals(authToken, auth.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, authToken);
    }

    @Override
    public String toString() {
        return "Auth{" +
                "userName='" + userName + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
