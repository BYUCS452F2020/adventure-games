package Model;

import java.io.Serializable;

public class Player implements Serializable {
  private String id;
  private String userId;
  private String gameId;
  private int kills;
  private String targetId;
  private boolean gameStatus;

  public Player(String userId) {
    this.userId = userId;
    this.gameId = null;
    this.kills = 0;
    this.targetId = null;
    this.gameStatus = true;
  }

  public Player(String userId, String gameId) {
    this.id = userId + "_" + gameId;
    this.userId = userId;
    this.gameId = gameId;
    this.kills = 0;
    this.targetId = "";
    this.gameStatus = true;
  }

  public Player(String id, String userId, String gameId, int kills, String targetId, boolean gameStatus) {
    this.id = id;
    this.userId = userId;
    this.gameId = gameId;
    this.kills = kills;
    this.targetId = targetId;
    this.gameStatus = gameStatus;
  }

  public String getPlayerId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getGameId() {
    return gameId;
  }

  public int getKills() {
    return kills;
  }

  public String getTargetId() {
    return targetId;
  }

  public boolean getStatus() {
    return gameStatus;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}
