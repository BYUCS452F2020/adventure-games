import 'package:flutter_app/Model/Player.dart';
import 'package:flutter_app/Result/Result.dart';

class PlayersResult extends Result {
  List<Player> players;

  PlayersResult(bool success, String message) : super(success, message);

  PlayersResult.players(this.players) : super(true, "success");
}