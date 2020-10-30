
class user {

  String userId;
  String password;
  String firstName;
  String lastName;
  int kills;
  int wins;

  void setUser(Map input){
    userId = input["userId"];
    password = input["password"];
    firstName = input["firstName"];
    lastName = input["lastName"];
    kills = input["kills"];
    wins = input["wins"];
  }
}
