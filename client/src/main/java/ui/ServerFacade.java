package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {
    HttpURLConnection http;
    String authToken;
    public ListResponse gameList;
    int port;
    public ServerFacade(int port) throws Exception {
        this.port = port;
        try {gameList = listGames();} catch (Exception ignored) {}
    }

    public AuthData register(String username, String password, String email) throws Exception {
        URI uri = new URI(String.format("http://localhost:%d/user", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        var body = new RegisterRequest(username, password, email);
        try (var outputStream = http.getOutputStream()){
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        } catch (Exception ex){
            throw ex;
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData response = new Gson().fromJson(inputStreamReader, AuthData.class);
            authToken = response.getAuthToken();;
            return response;
        } catch (Exception ex){
            throw ex;
        }
    }

    public AuthData login(String username, String password) throws Exception{
        URI uri = new URI(String.format("http://localhost:%d/session", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        var body = new LoginRequest(username, password);
        try (var outputStream = http.getOutputStream()){
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        } catch (Exception ex){
            System.out.println("Error");
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData response =  new Gson().fromJson(inputStreamReader, AuthData.class);
            authToken = response.getAuthToken();
            return response;
        } catch (Exception ex){
            throw ex;
        }
    }

    public void logout() throws Exception{
        URI uri = new URI(String.format("http://localhost:%d/session", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");


        http.setDoOutput(true);
        http.addRequestProperty("Authorization", authToken);
        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            Map temp = new Gson().fromJson(inputStreamReader, Map.class);

        } catch (Exception ex){
            throw ex;
        }

    }
    public String createGame(String gameName) throws Exception {
        URI uri = new URI(String.format("http://localhost:%d/game", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setDoOutput(true);
        http.addRequestProperty("Authorization", authToken);
        http.addRequestProperty("Content-Type", "application/json");

        CreateGameRequest body = new CreateGameRequest(gameName);
        try (var outputStream = http.getOutputStream()){
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        } catch (Exception ex){
            throw ex;
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            CreateGameResponse response = new Gson().fromJson(inputStreamReader, CreateGameResponse.class);
            return response.gameID();
        } catch (Exception ex){
            throw ex;
        }
    }

    public ListResponse listGames() throws Exception {
        URI uri = new URI(String.format("http://localhost:%d/game", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        http.setDoOutput(true);
        http.addRequestProperty("Authorization", authToken);
        http.addRequestProperty("Content-Type", "application/json");
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            ListResponse response = new Gson().fromJson(inputStreamReader, ListResponse.class);
            gameList = response;
            return response;
        } catch (Exception ex){
            throw ex;
        }
    }

    public ChessGame joinGame(String playerColor, int gameID) throws Exception {
        URI uri = new URI(String.format("http://localhost:%d/game", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        http.setDoOutput(true);
        http.addRequestProperty("Authorization", authToken);
        http.addRequestProperty("Content-Type", "application/json");
        if (playerColor != null) playerColor.toUpperCase();
        var body = new JoinRequest(playerColor, gameList.games[gameID].gameID);
        try (OutputStream reqBody = http.getOutputStream()){
            var jsonBody = new Gson().toJson(body);
            reqBody.write(jsonBody.getBytes());
        } catch (Exception ex){
            throw ex;
        }

        try (InputStream respBody = http.getInputStream()){
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            ChessGame response = new Gson().fromJson(inputStreamReader, ChessGame.class);
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public ChessGame observeGame(int gameID) throws Exception {
        return joinGame(null, gameID);
    }
}
