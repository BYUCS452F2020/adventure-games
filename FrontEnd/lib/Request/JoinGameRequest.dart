import 'package:json_annotation/json_annotation.dart';

part 'JoinGameRequest.g.dart';

@JsonSerializable()

class JoinGameRequest {
  String userId;
  String code;

  JoinGameRequest(this.userId, this.code);

  factory JoinGameRequest.fromJson(Map<String, dynamic> json) => _$JoinGameRequestFromJson(json);

  Map<String, dynamic> toJson() => _$JoinGameRequestToJson(this);
}