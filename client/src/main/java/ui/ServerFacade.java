package ui;

import com.google.gson.Gson;
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
    int port;
    public ServerFacade(int port) throws Exception {
        this.port = port;
    }

    public void register(String username, String password, String email) throws Exception {
        URI uri = new URI(String.format("http://localhost:%d/user", port));
        http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        //http.connect();
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
            String response = new Gson().fromJson(inputStreamReader, String.class);
            if (response.equals("200")){
                System.out.println("Register Successful! Logged in!");
            } else {
                System.out.println(response);
            }
        } catch (Exception ex){
            System.out.println("Error");
        }
    }
}
