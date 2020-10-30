import 'package:flutter_app/Model/User.dart';
import 'package:json_annotation/json_annotation.dart';

import 'Result.dart';

part 'userresult.g.dart';

@JsonSerializable()
class UserResult extends Result {
  User user;

  UserResult(bool success, String message) : super(success, message);

  UserResult.user(this.user) : super(true, "success");

  factory UserResult.fromJson(Map<String, dynamic> json) =>
      _$UserFromJson(json);
}
