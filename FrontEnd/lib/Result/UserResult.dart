import 'package:flutter_app/Model/User.dart';

import 'Result.dart';

class UserResult extends Result {
  User user;

  UserResult(bool success, String message) : super(success, message);

  UserResult.user(this.user) : super(true, "success");
}