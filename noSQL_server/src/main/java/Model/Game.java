package Model;

import java.io.Serializable;

public class Game implements Serializable {
  private int id;
  private int hostId;
  private String location;
  private long startTime;
  private int initialPlayerCount;
  private int playersRemainingCount;
  private String code;
  private String winner;

  public Game(String location, String code) {
    this.hostId = 0;
    this.location = location;
    this.startTime = 0;
    this.initialPlayerCount = 1;
    this.playersRemainingCount = 1;
    this.code = code;
    this.winner = null;
  }

  public Game(int id, int hostId, String location, long startTime, int initialPlayerCount, int playersRemainingCount, String code, String winner) {
    this.id = id;
    this.hostId = hostId;
    this.location = location;
    this.startTime = startTime;
    this.initialPlayerCount = initialPlayerCount;
    this.playersRemainingCount = playersRemainingCount;
    this.code = code;
    this.winner = winner;
  }

  public int getId() {
    return id;
  }

  public int getHostId() {
    return hostId;
  }

  public String getLocation() {
    return location;
  }

  public long getStartTime() {
    return startTime;
  }

  public int getInitialPlayerCount() {
    return initialPlayerCount;
  }

  public int getPlayersRemainingCount() {
    return playersRemainingCount;
  }

  public String getCode() {
    return code;
  }

  public String getWinner() {
    return winner;
  }
}
