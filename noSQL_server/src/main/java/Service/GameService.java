package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.GameDao;
import DataAccess.PlayerDao;
import Model.Game;
import Model.Player;
import Request.GameRequest;
import Result.GameResult;
import Result.PlayerResult;

import java.sql.Connection;

public class GameService {
  private static int gameCodeLength = 8;

  /**
   * Returns game by gameId
   */
  public static GameResult getGame(int gameId) throws DataAccessException {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      GameDao gameDao = new GameDao(conn);
      Game game = gameDao.getOne(gameId);

      if (game == null) {
        db.closeConnection(false);
        return new GameResult(false, "no game data associated with id");
      }

      db.closeConnection(true);

      return new GameResult(game);
    } catch (Exception e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new GameResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Creates player for user creating game
   * Creates game based on location and start time provided
   * Game object generates random code for others to use to join
   * Creates player for user that created the game
   * Adds player that created it to game
   * Returns game object (including gameId and generated code)
   */
  public static PlayerResult setupGame(GameRequest r) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      GameDao gameDao = new GameDao(conn);
      PlayerDao playerDao = new PlayerDao(conn);

      String userId = r.getUsername();
      String location = r.getLocation();

      String code;
      do {
        code = getRandomCode();
      } while (!gameDao.checkCodeAvailability(code));

      Game game = new Game(location, code);
      int gameId = gameDao.setupGame(game);
      Player player = new Player(userId, gameId);
      playerDao.insert(player);

      player = playerDao.getOne(userId, gameId);
      gameDao.setHostId(gameId, player.getPlayerId());

      db.closeConnection(true);

      return new PlayerResult(player);
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Gets number of players that have joined the game since it's setup
   * Updates game with number of initial players and players remaining
   * Assigns each player a target
   */
  public static GameResult startGame(int gameId) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      GameDao gameDao = new GameDao(conn);
      PlayerDao playerDao = new PlayerDao(conn);

      Player[] players = playerDao.getAllPlayersInGame(gameId);
      Player player;
      Player target;
      for (int i = 0; i < players.length - 1; i++) {
        player = players[i];
        target = players[i + 1];
        playerDao.updateTarget(player.getPlayerId(), target.getUserId());
      }
      player = players[players.length - 1];
      target = players[0];
      playerDao.updateTarget(player.getPlayerId(), target.getUserId());

      gameDao.startGame(gameId, players.length);

      db.closeConnection(true);

      return new GameResult(true, "Start game successful");
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new GameResult(false, "error " + e.getMessage());
    }
  }

  private static String getRandomCode() {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";

    StringBuilder sb = new StringBuilder(gameCodeLength);

    for (int i = 0; i < gameCodeLength; i++) {
      int index
              = (int)(AlphaNumericString.length()
              * Math.random());
      sb.append(AlphaNumericString
              .charAt(index));
    }

    return sb.toString();
  }
}
