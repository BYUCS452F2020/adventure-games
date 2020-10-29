package Model;

import java.io.Serializable;

public class User implements Serializable {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private int wins;
  private int kills;

  public User(String username, String password, String firstName, String lastName, int wins, int kills) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.wins = wins;
    this.kills = kills;
  }

  public User(String username, String password, String firstName, String lastName) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.wins = 0;
    this.kills = 0;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getWins() {
    return wins;
  }

  public int getKills() {
    return kills;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public void setKills(int kills) {
    this.kills = kills;
  }
}
