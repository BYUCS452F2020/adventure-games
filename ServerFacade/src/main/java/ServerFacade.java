import Handlers.JsonHandler;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.PlayerResult;
import Result.PlayersResult;
import Result.UserResult;
import sun.rmi.runtime.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
  private static final String LOG_TAG = "Proxy";
  private String serverHost;
  private String serverPort;

  public ServerFacade(String serverHost, String serverPort) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
  }

  public ServerFacade() {
    this.serverHost = "192.168.1.15";
    this.serverPort = "3000";
  }

  UserResult login(LoginRequest loginRequest) throws Exception {
    String url = "http://" + serverHost + ":" + serverPort + "/user/login";
    HttpURLConnection connection =
            (HttpURLConnection) new URL(url).openConnection();

    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.connect();

    Writer out = new OutputStreamWriter(connection.getOutputStream());
    String request = JsonHandler.serialize(loginRequest, LoginRequest.class);
    out.write(request);
    out.close();

    int status = connection.getResponseCode();

    if (status != HttpURLConnection.HTTP_OK) {
      return null;
    }

    String response;
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    response = br.readLine();

    return JsonHandler.deserialize(response, UserResult.class);
  }

  UserResult register(RegisterRequest registerRequest) throws Exception {
    String url = "http://" + serverHost + ":" + serverPort + "/user/register";
    HttpURLConnection connection =
            (HttpURLConnection) new URL(url).openConnection();

    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.connect();

    Writer out = new OutputStreamWriter(connection.getOutputStream());
    String request = JsonHandler.serialize(registerRequest, RegisterRequest.class);
    out.write(request);
    out.close();

    int status = connection.getResponseCode();

    if (status != HttpURLConnection.HTTP_OK) {
      return null;
    }

    String response;
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    response = br.readLine();

    return JsonHandler.deserialize(response, UserResult.class);
  }

  UserResult getUser(String username) throws Exception {
    String url = "http://" + serverHost + ":" + serverPort + "/user/" + username;
    HttpURLConnection connection =
            (HttpURLConnection) new URL(url).openConnection();

    connection.setRequestMethod("GET");
    connection.setDoOutput(false);
    connection.connect();

    int status = connection.getResponseCode();

    if (status != HttpURLConnection.HTTP_OK) {
      return null;
    }

    String response;
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    response = br.readLine();

    return JsonHandler.deserialize(response, UserResult.class);
  }

  PlayerResult getPlayer(int playerId) throws Exception {
    String url = "http://" + serverHost + ":" + serverPort + "/player/" + playerId;
    HttpURLConnection connection =
            (HttpURLConnection) new URL(url).openConnection();

    connection.setRequestMethod("GET");
    connection.setDoOutput(false);
    connection.connect();

    int status = connection.getResponseCode();

    if (status != HttpURLConnection.HTTP_OK) {
      return null;
    }

    String response;
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    response = br.readLine();

    return JsonHandler.deserialize(response, PlayerResult.class);
  }

  PlayersResult getPlayers(String userId) throws Exception {
    String url = "http://" + serverHost + ":" + serverPort + "/players/" + userId;
    HttpURLConnection connection =
            (HttpURLConnection) new URL(url).openConnection();

    connection.setRequestMethod("GET");
    connection.setDoOutput(false);
    connection.connect();

    int status = connection.getResponseCode();

    if (status != HttpURLConnection.HTTP_OK) {
      return null;
    }

    String response;
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    response = br.readLine();

    return JsonHandler.deserialize(response, PlayersResult.class);
  }



}
