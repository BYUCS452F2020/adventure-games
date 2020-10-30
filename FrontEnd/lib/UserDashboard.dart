import 'package:flutter/material.dart';
import 'package:flutter_app/LoadingPage.dart';
import 'Model/User.dart';
import 'Model/Game.dart';
import 'Model/Player.dart';
import 'GameDashboard.dart';
import 'CreateGame.dart';
import 'JoinGame.dart';
import 'Result/PlayersResult.dart';
import 'ServerFacade/ServerFacade.dart';

class UserDashboard extends StatefulWidget {
  UserDashboard({Key key, this.currentUser}) : super(key: key);
  final User currentUser;
  @override
  _UserDashboardState createState() => _UserDashboardState();
}

class _UserDashboardState extends State<UserDashboard> {
  Widget _body = LoadingPage.loading();
  List<Player> playerList;

  @override
  void initState() {
    super.initState();
    init();
  }

  void init() async {
    playerList = await getPlayers(widget.currentUser.username);
    setState(() => _body = userDashboardPage());
  }

  Future<List<Player>> getPlayers(String username) async {
    PlayersResult result = await new ServerFacade().getPlayers(
        widget.currentUser.username);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return null;
    }
    if (result.players == null) {
      return new List();
    }
    return result.players;
  }

  void _gameDashboard(Player currPlayer) {
    final route = MaterialPageRoute(
      builder: (context) =>
          GameDashboard(currPlayer: currPlayer, currUser: widget.currentUser),
    );
    Navigator.push(context, route);
  }

  void _createGame() async {
    final route = MaterialPageRoute(
      builder: (context) =>
          CreateGame(currUser: widget.currentUser),
    );
    Navigator.push(context, route);
  }

  void _joinGame() {
    final route = MaterialPageRoute(
      builder: (context) =>
          JoinGame(currUser: widget.currentUser),
    );
    Navigator.push(context, route);
  }


  @override
  Widget build(BuildContext context) {
    return _body;
  }


  Scaffold userDashboardPage() {
    return Scaffold(
        appBar: AppBar(
          title: Text('User Dashboard'),
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Username: ${widget.currentUser.username}',
                      style: TextStyle(fontSize: 20),
                    )),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: FlatButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Create Game'),
                      onPressed: () {
                        _createGame();
                      },
                    )),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: FlatButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Join Game'),
                      onPressed: () {
                        _joinGame();
                      },
                    )),
                Expanded (
                  child: ListView.builder(
                    shrinkWrap: true,
                    itemCount: playerList.length,
                    itemBuilder: (context, index) {
                      return ListTile(
                        title: Text(playerList[index].gameId.toString()),
                        onTap: () {
                          _gameDashboard(playerList[index]);
                        },
                      );
                    },
                  ),
                ),
              ],
            )));
  }
}