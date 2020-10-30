// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'UserResult.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserResult _$UserResultFromJson(Map<String, dynamic> json) {
  return UserResult(
    json['success'] as bool,
    json['message'] as String,
  )..user = json['user'] == null
      ? null
      : User.fromJson(json['user'] as Map<String, dynamic>);
}

Map<String, dynamic> _$UserResultToJson(UserResult instance) =>
    <String, dynamic>{
      'success': instance.success,
      'message': instance.message,
      'user': instance.user?.toJson(),
    };
