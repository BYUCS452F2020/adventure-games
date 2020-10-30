// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'User.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

User _$UserFromJson(Map<String, dynamic> json) {
  return User(
    json['userId'] as String,
    json['password'] as String,
    json['firstName'] as String,
    json['lastName'] as String,
    json['kills'] as int,
    json['wins'] as int,
  );
}

Map<String, dynamic> _$UserToJson(User instance) => <String, dynamic>{
      'userId': instance.userId,
      'password': instance.password,
      'firstName': instance.firstName,
      'lastName': instance.lastName,
      'kills': instance.kills,
      'wins': instance.wins,
    };
