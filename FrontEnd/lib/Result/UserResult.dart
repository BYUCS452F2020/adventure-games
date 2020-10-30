import 'package:flutter_app/Model/User.dart';
import 'package:json_annotation/json_annotation.dart';

import 'Result.dart';

part 'UserResult.g.dart';

@JsonSerializable(explicitToJson: true)

class UserResult extends Result {
  User user;

  UserResult(bool success, String message) : super(success, message);

  UserResult.user(this.user) : super(true, "success");

  factory UserResult.fromJson(Map<String, dynamic> json) => _$UserResultFromJson(json);

  Map<String, dynamic> toJson() => _$UserResultToJson(this);
}