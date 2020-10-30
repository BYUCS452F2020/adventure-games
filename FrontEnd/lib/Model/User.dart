import 'package:json_annotation/json_annotation.dart';

part 'User.g.dart';

@JsonSerializable()

class User {

  String userId;
  String password;
  String firstName;
  String lastName;
  int kills;
  int wins;

  User(this.userId, this.password, this.firstName, this.lastName, this.kills,
      this.wins);

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  Map<String, dynamic> toJson() => _$UserToJson(this);
}
