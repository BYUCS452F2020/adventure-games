package Result;

import Model.Player;

public class PlayersResult extends Result {
  private Player[] players;

  public PlayersResult(boolean success, String message) {
    super(success, message);
  }

  public PlayersResult(Player[] players) {
    super(true, "successful");
    this.players = players;
  }

  public Player[] getData() {
    return players;
  }
}
