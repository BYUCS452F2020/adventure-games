import 'package:json_annotation/json_annotation.dart';

part 'Player.g.dart';

@JsonSerializable()

class Player {

  int playerId;
  String userId;
  String targetId;
  int gameId;
  int kills;
  bool status;

  Player(this.playerId, this.userId, this.targetId, this.gameId, this.kills,
      this.status);

  factory Player.fromJson(Map<String, dynamic> json) => _$PlayerFromJson(json);

  Map<String, dynamic> toJson() => _$PlayerToJson(this);
}
