package Service;

import DataAccess.*;
import Model.Game;
import Model.Player;
import Request.JoinGameRequest;
import Result.PlayerResult;
import Result.PlayersResult;

import java.sql.Connection;

public class PlayerService {

  /**
   * Returns player by playerId
   */
  public static PlayerResult getPlayer(int playerId) throws DataAccessException {
    try {
      PlayerDao playerDao = new PlayerDao();
      Player player = playerDao.getOne(playerId);

      if (player == null) {
        return new PlayerResult(false, "no player data associated with player id");
      }

      return new PlayerResult(player);
    } catch (Exception e) {
      e.printStackTrace();
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Returns all players associated with username
   */
  public static PlayersResult getPlayers(String username) throws Exception {
    try {
      PlayerDao playerDao = new PlayerDao();
      Player[] players = playerDao.getAll(username);

      return new PlayersResult(players);
    } catch (Exception e) {
      e.printStackTrace();
      return new PlayersResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Gets gameId from game code
   * Generates player for that game and inserts it into the database
   */
  public static PlayerResult joinGame(JoinGameRequest r) throws Exception {

    try {
      PlayerDao playerDao = new PlayerDao();
      GameDao gameDao = new GameDao();

      String userId = r.getUserId();
      String code = r.getCode();

      int gameId = gameDao.getGameIdByCode(code);
      Game game = gameDao.getOne(gameId);

      if (game.getStartTime() > 0) {
        return new PlayerResult(false, "Game has already started");
      }

      gameDao.playerJoined(gameId);

      Player player = new Player(userId, gameId);
      playerDao.insert(player);
      player = playerDao.getOne(userId, gameId);

      return new PlayerResult(player);
    } catch (Exception e) {
      e.printStackTrace();
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  /**
   * If player is last one in the the game, deletes game and player
   * If game hasn't started yet, deletes player before starting game
   * If players are still in the game and player is alive, reassigns target and deletes player, adjusts game players remaining
   * If players are still in the game but player is dead, just deletes player
   */
  public static PlayerResult leaveGame(int playerId) throws Exception {
    try {
      PlayerDao playerDao = new PlayerDao();
      GameDao gameDao = new GameDao();
      Player player = playerDao.getOne(playerId);
      int gameId = player.getGameId();

      if (gameDao.getPlayersRemaining(gameId) == 1) {
        gameDao.delete(gameId);
      } else if (player.getStatus()) {
        Game game = gameDao.getOne(gameId);
        if (game.getStartTime() == 0) {
          gameDao.playerLeftBeforeStart(gameId);
        } else {
          Player assassin = playerDao.getAssassin(player.getUserId(), gameId);
          playerDao.updateTarget(assassin.getPlayerId(), player.getTargetId());
          gameDao.playerLeftAfterStart(gameId);
        }
      }

      playerDao.delete(playerId);

      return new PlayerResult(true, "Player left game");
    } catch (Exception e) {
      e.printStackTrace();
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  public static PlayerResult killTarget(int playerId) throws Exception {
    try {

      PlayerDao playerDao = new PlayerDao();
      UserDao userDao = new UserDao();
      GameDao gameDao = new GameDao();

      Player player = playerDao.getOne(playerId);
      String userId = player.getUserId();
      int gameId = player.getGameId();
      Player target = playerDao.getTarget(player.getTargetId(), gameId);

      //sets target status to dead (false) and nullifies their targetId
      playerDao.killPlayer(target.getPlayerId());
      //updates player and user kill count
      playerDao.updateKills(playerId);
      userDao.updateKills(userId);
      //subtracts 1 from the game's players remaining
      gameDao.playerKilled(gameId);

      if (gameDao.getPlayersRemaining(gameId) == 1) {
        userDao.updateWin(userId);
        gameDao.setWinner(gameId, userId);
        return new PlayerResult(true, "Killing player successful, game over");
      }

      //sets player's new target as target's old target
      String targetUsername = target.getTargetId();
      target = playerDao.getTarget(targetUsername, gameId);
      boolean stillInGame = target.getStatus();
      //checks if new target is still alive, if not, assigns next target
      while (!stillInGame) {
        targetUsername = target.getTargetId();
        target = playerDao.getTarget(targetUsername, gameId);
        stillInGame = target.getStatus();
      }
      playerDao.updateTarget(playerId, target.getUserId());

      return new PlayerResult(true, "Killing player successful");
    } catch (DataAccessException e) {
      e.printStackTrace();
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }
}