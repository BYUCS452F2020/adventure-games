import 'package:json_annotation/json_annotation.dart';

part 'User.g.dart';

@JsonSerializable()

class User {

  String username;
  String password;
  String firstName;
  String lastName;
  int wins;
  int kills;


  User(this.username, this.password, this.firstName, this.lastName, this.wins,
      this.kills);

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  Map<String, dynamic> toJson() => _$UserToJson(this);
}
