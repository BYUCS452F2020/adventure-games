package Model;

import java.io.Serializable;

public class Game implements Serializable {
  private String id;
  private String hostId;
  private String location;
  private long startTime;
  private String[] initialPlayers;
  private String[] playersRemaining;
  private String code;
  private String winner;

  public Game(String location, String code, String userId) {
    this.id = code;
    this.hostId = userId + "_" + code;
    this.location = location;
    this.startTime = 0;
    this.initialPlayers = new String[] {userId + "_" + code};
    this.playersRemaining = new String[] {userId + "_" + code};
    this.code = code;
    this.winner = "";
  }

  public Game(String id, String hostId, String location, long startTime, String[] initialPlayers, String[] playersRemaining, String code, String winner) {
    this.id = id;
    this.hostId = hostId;
    this.location = location;
    this.startTime = startTime;
    this.initialPlayers = initialPlayers;
    this.playersRemaining = playersRemaining;
    this.code = code;
    this.winner = winner;
  }

  public String getId() {
    return id;
  }

  public String getHostId() {
    return hostId;
  }

  public String getLocation() {
    return location;
  }

  public long getStartTime() {
    return startTime;
  }

  public String[] getInitialPlayerCount() {
    return initialPlayers;
  }

  public String[] getPlayersRemainingCount() {
    return playersRemaining;
  }

  public String getCode() {
    return code;
  }

  public String getWinner() {
    return winner;
  }
}
