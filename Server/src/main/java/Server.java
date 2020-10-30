import Handler.*;
import Request.RegisterRequest;
import Result.GameResult;
import Result.UserResult;
import Service.GameService;
import Service.PlayerService;
import Service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

public class Server {

  private void startServer(int port) throws IOException {
    System.out.println("Initializing HTTP Server");

    try {
      InetSocketAddress serverAddress = new InetSocketAddress(port);
      HttpServer server = HttpServer.create(serverAddress, 10);
      registerHandlers(server);
      server.start();
      System.out.println("Adventure Games Server listening on port " + port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void registerHandlers(HttpServer server) {
    server.createContext("/user/login", new LoginHandler());
    server.createContext("/user/register", new UserHandler());
    server.createContext("/user/", new UserHandler());
    server.createContext("/player/", new PlayerHandler());
    server.createContext("/players/", new PlayersHandler());
    server.createContext("/leave_game/", new LeaveGameHandler());
    server.createContext("/join_game", new JoinGameHandler());
    server.createContext("/start_game/", new StartGameHandler());
    server.createContext("/kill_target/", new KillTargetHandler());
    server.createContext("/game", new GameHandler());
    server.createContext("/game/", new GameHandler());
  }

  public static void main(String[] args) throws IOException {
    String portNumber = args[0];
    new Server().startServer(Integer.parseInt(portNumber));
  }
}
