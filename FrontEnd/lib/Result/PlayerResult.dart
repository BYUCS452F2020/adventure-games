import 'package:flutter_app/Model/Player.dart';
import 'package:flutter_app/Result/Result.dart';
import 'package:json_annotation/json_annotation.dart';

part 'PlayerResult.g.dart';

@JsonSerializable(explicitToJson: true)

class PlayerResult extends Result {
  Player player;

  PlayerResult(bool success, String message) : super(success, message);

  PlayerResult.player(this.player) : super(true, "success");

  factory PlayerResult.fromJson(Map<String, dynamic> json) => _$PlayerResultFromJson(json);

  Map<String, dynamic> toJson() => _$PlayerResultToJson(this);
}