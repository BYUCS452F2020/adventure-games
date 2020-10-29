package DataAccess;

import Model.User;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
  private final Connection conn;

  public UserDao(Connection conn) {
    this.conn = conn;
  }

  public User getOne(String username) throws DataAccessException {
    User user;
    ResultSet rs = null;
    String sql = "SELECT * FROM users WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      if (rs.next()) {
        user = new User(rs.getString("userName"), rs.getString("password"),
                rs.getString("firstName"), rs.getString("lastName"),
                rs.getInt("wins"), rs.getInt("kills"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding user: " + e.getMessage());
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

  public void insert(User user) throws DataAccessException {
    String sql = "INSERT INTO users (userName, password, firstName, lastName, wins, kills) VALUES (?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getFirstName());
      stmt.setString(4, user.getLastName());
      stmt.setInt(5, user.getWins());
      stmt.setInt(6, user.getKills());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while inserting user into the database: " + e.getMessage());
    }
  }

  public void updateWin(String username) throws DataAccessException {
    String sql = "UPDATE users SET wins = wins + 1 WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating user in the database: " + e.getMessage());
    }
  }

  public void updateKills(String username) throws DataAccessException {
    String sql = "UPDATE users SET kills = kills + 1 WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating user in the database: " + e.getMessage());
    }
  }

  public void removeAll() throws DataAccessException {
    String sql = "DELETE FROM users";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while deleting all users from database: " + e.getMessage());
    }
  }
}
