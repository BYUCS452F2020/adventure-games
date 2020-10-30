import 'package:json_annotation/json_annotation.dart';

part 'GameRequest.g.dart';

@JsonSerializable()

class GameRequest {
  String username;
  String location;

  GameRequest(this.username, this.location);

  factory GameRequest.fromJson(Map<String, dynamic> json) => _$GameRequestFromJson(json);

  Map<String, dynamic> toJson() => _$GameRequestToJson(this);
}
