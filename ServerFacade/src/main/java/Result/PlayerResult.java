package Result;

import Model.Player;

public class PlayerResult extends Result {
  private Player player;

  public PlayerResult(boolean success, String message) {
    super(success, message);
  }

  public PlayerResult(Player player) {
    super(true, "success");
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
