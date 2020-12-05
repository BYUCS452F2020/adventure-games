package Handler;

import Result.PlayersResult;
import Service.PlayerService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PlayersHandler extends RequestHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String message;
    PlayersResult result = null;
    try {
      if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
        String urlPath = exchange.getRequestURI().toString();
        String username = urlPath.substring(urlPath.lastIndexOf('/') + 1);
        result = PlayerService.getPlayers(username);
      } else {
        message = "{ \"message\": \"error: bad request\" }";
        result = new PlayersResult(false, message);
      }
    } catch (Exception e) {
      message = "{ \"message\": \"error: " + e.getMessage() + "\" }";
      result = new PlayersResult(false, message);
      e.printStackTrace();
    } finally {
      message = JsonHandler.serialize(result, PlayersResult.class);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
      OutputStream respBody = exchange.getResponseBody();
      writeString(message, respBody);
      respBody.close();
    }
  }
}
