import 'package:json_annotation/json_annotation.dart';

part 'Player.g.dart';

@JsonSerializable()

class Player {

  String id;
  String userId;
  String targetId;
  String gameId;
  int kills;
  bool gameStatus;

  Player(this.id, this.userId, this.targetId, this.gameId, this.kills,
      this.gameStatus);

  factory Player.fromJson(Map<String, dynamic> json) => _$PlayerFromJson(json);

  Map<String, dynamic> toJson() => _$PlayerToJson(this);
}
