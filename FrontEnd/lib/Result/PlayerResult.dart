import 'package:flutter_app/Model/Player.dart';

import 'Result.dart';

class PlayerResult extends Result {
  Player player;

  PlayerResult(bool success, String message) : super(success, message);

  PlayerResult.player(this.player) : super(true, "success");

}