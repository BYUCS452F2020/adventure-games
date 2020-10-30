package Model;

import java.io.Serializable;

public class Player implements Serializable {
  private int id;
  private String userId;
  private int gameId;
  private int kills;
  private String targetId;
  private boolean status;

  public Player(String userId) {
    this.userId = userId;
    this.gameId = -1;
    this.kills = 0;
    this.targetId = null;
    this.status = true;
  }

  public Player(String userId, int gameId) {
    this.userId = userId;
    this.gameId = gameId;
    this.kills = 0;
    this.targetId = null;
    this.status = true;
  }

  public Player(int id, String userId, int gameId) {
    this.id = id;
    this.userId = userId;
    this.gameId = gameId;
    this.kills = 0;
    this.targetId = null;
    this.status = true;
  }

  public Player(int id, String userId, int gameId, int kills, String targetId, boolean status) {
    this.id = id;
    this.userId = userId;
    this.gameId = gameId;
    this.kills = kills;
    this.targetId = targetId;
    this.status = status;
  }

  public int getPlayerId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public int getGameId() {
    return gameId;
  }

  public int getKills() {
    return kills;
  }

  public String getTargetId() {
    return targetId;
  }

  public boolean getStatus() {
    return status;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }
}
