import 'package:json_annotation/json_annotation.dart';

part 'RegisterRequest.g.dart';

@JsonSerializable()

class RegisterRequest {
  String username;
  String password;
  String firstName;
  String lastName;

  RegisterRequest(this.username, this.password, this.firstName, this.lastName);

  factory RegisterRequest.fromJson(Map<String, dynamic> json) => _$RegisterRequestFromJson(json);

  Map<String, dynamic> toJson() => _$RegisterRequestToJson(this);
}