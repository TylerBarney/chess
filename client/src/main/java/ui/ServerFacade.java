package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ServerFacade {
    HttpURLConnection http;
    String authToken;
    int port;
    public ServerFacade(int port) throws Exception {
        this.port = port;
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
            System.out.println("Error");
        }

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthData response = new Gson().fromJson(inputStreamReader, AuthData.class);
            authToken = response.getAuthToken();;
            return response;
        } catch (Exception ex){
            System.out.println("Error");
        }
        return null;
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
            System.out.println("Error");
        }
        return null;
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
            System.out.println(temp);

        } catch (Exception ex){
            System.out.println("Error");
        }

    }
}
