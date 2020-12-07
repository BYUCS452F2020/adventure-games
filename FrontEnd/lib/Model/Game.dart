import 'package:json_annotation/json_annotation.dart';

part 'Game.g.dart';

@JsonSerializable()

class Game {

  String id;
  String hostId;
  String location;
  int startTime;
  List initialPlayers;
  List playersRemaining;
  String code;
  String winner;

  Game(this.id, this.hostId, this.location, this.startTime,
      this.initialPlayers, this.playersRemaining, this.code,
      this.winner);

  factory Game.fromJson(Map<String, dynamic> json) => _$GameFromJson(json);

  Map<String, dynamic> toJson() => _$GameToJson(this);
}
