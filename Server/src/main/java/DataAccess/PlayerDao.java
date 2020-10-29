package DataAccess;

import Model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerDao {
  private final Connection conn;

  public PlayerDao(Connection conn) {
    this.conn = conn;
  }

  public Player getOne(int id) throws DataAccessException {
    Player player;
    ResultSet rs = null;
    String sql = "SELECT * FROM players WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      if (rs.next()) {
        player = new Player(rs.getInt("id"), rs.getString("userId"), rs.getInt("gameId"),
                rs.getInt("kills"), rs.getString("targetId"), rs.getBoolean("status"));
        return player;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding player: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public Player getOne(String userId, int gameId) throws DataAccessException {
    Player player;
    ResultSet rs = null;
    String sql = "SELECT * FROM players WHERE userId = ? AND gameId = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, userId);
      stmt.setInt(2, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        player = new Player(rs.getInt("id"), rs.getString("userId"), rs.getInt("gameId"),
                rs.getInt("kills"), rs.getString("targetId"), rs.getBoolean("status"));
        return player;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding player: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public Player[] getAll(String username) throws DataAccessException {
    Player[] players;
    ArrayList<Player> playerArrayList = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM players WHERE userId = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      if (rs.next()) {
        Player player = new Player(rs.getInt("id"), rs.getString("userId"), rs.getInt("gameId"),
                rs.getInt("kills"), rs.getString("targetId"), rs.getBoolean("status"));
        playerArrayList.add(player);
      }
      players = playerArrayList.toArray(new Player[playerArrayList.size()]);
      return players;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding players: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void insert(Player player) throws DataAccessException {
    String sql = "INSERT INTO players (userId, gameId, kills, targetId, status) VALUES (?,?,?,?,?);";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, player.getUserId());
      stmt.setInt(2, player.getGameId());
      stmt.setInt(3, player.getKills());
      stmt.setString(4, player.getTargetId());
      stmt.setBoolean(5, player.getStatus());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while inserting player into the database: " + e.getMessage());
    }
  }

  public Player[] getAllPlayersInGame(int gameId) throws DataAccessException {
    Player[] players;
    ArrayList<Player> playerArrayList = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM players WHERE gameId = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        Player player = new Player(rs.getInt("id"), rs.getString("userId"), rs.getInt("gameId"),
                rs.getInt("kills"), rs.getString("targetId"), rs.getBoolean("status"));
        playerArrayList.add(player);
      }
      players = playerArrayList.toArray(new Player[playerArrayList.size()]);
      return players;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding players in game: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public int getPlayersRemaining(int gameId) throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT playersRemainingCount FROM players WHERE gameId = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("playersRemainingCount");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting number of players still alive: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return -1;
  }

  public int getNextId() throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'adventure_games' AND TABLE_NAME = 'players';";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("AUTO_INCREMENT");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding next game id: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return -1;
  }

  public void killPlayer(int playerId) throws DataAccessException {
    String sql = "UPDATE players SET targetId = NULL, status = false WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, playerId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating player in the database: " + e.getMessage());
    }
  }

  public void updateKills(int playerId) throws DataAccessException {
    String sql = "UPDATE players SET kills = kills + 1 WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, playerId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating player in the database: " + e.getMessage());
    }
  }


  public Player getTarget(String username, int gameId) throws DataAccessException {
    Player player;
    ResultSet rs = null;
    String sql = "SELECT * FROM players WHERE userId = ? AND gameId = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      stmt.setInt(2, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        player = new Player(rs.getInt("id"), rs.getString("userId"), rs.getInt("gameId"),
                rs.getInt("kills"), rs.getString("targetId"), rs.getBoolean("status"));
        return player;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding target: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public void updateTarget(int playerId, String newTarget) throws DataAccessException {
    String sql = "UPDATE players SET targetId = ? WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, newTarget);
      stmt.setInt(2, playerId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating target in the database: " + e.getMessage());
    }
  }

  public Player getAssassin(String targetId, int gameId) throws DataAccessException {
    Player player;
    ResultSet rs = null;
    String sql = "SELECT * FROM players WHERE targetId = ? AND gameId = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, targetId);
      stmt.setInt(2, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        player = new Player(rs.getInt("id"), rs.getString("userId"), rs.getInt("gameId"),
                rs.getInt("kills"), rs.getString("targetId"), rs.getBoolean("status"));
        return player;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding assassin: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public void delete(int playerId) throws DataAccessException {
    String sql = "DELETE FROM players WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, playerId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while deleting player from database: " + e.getMessage());
    }
  }

  public void removeAll() throws DataAccessException {
    String sql = "DELETE FROM players";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while deleting all players from database: " + e.getMessage());
    }
  }
}
