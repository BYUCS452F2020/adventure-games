import 'package:flutter/material.dart';
import 'Classes/user.dart';
import 'Classes/game.dart';
import 'Classes/player.dart';
import 'gameDashboard.dart';
import 'createGame.dart';
import 'joinGame.dart';

class userDashboard extends StatefulWidget {
  userDashboard({Key key, this.currentUser}) : super(key: key);
  final user currentUser;
  @override
  _userDashboardState createState() => _userDashboardState();
}

class _userDashboardState extends State<userDashboard> {
  List<player> playerList;
  @override
  void initState(){
    super.initState();
    init();
  }

  void init() async {
    playerList = getPlayers(widget.currentUser.userId);
  }

  List<player> getPlayers(String username) async {
    PlayersResult result = await ServerFacade().getPlayers(widget.currentUser);

    if (!result.isSuccess()) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.getMessage),
      ));
      return null;
    }
    return result.getData();
  }

  void _gameDashboard(player currPlayer){
    final route = MaterialPageRoute(
      builder: (context) =>
          gameDashboard(currPlayer: currPlayer),
    );
    Navigator.push(context, route);
  }

  void _createGame() async {
    final route = MaterialPageRoute(
      builder: (context) =>
          createGame(currUser: widget.currentUser),
    );
    Navigator.push(context, route);
  }

  void _joinGame(){
    final route = MaterialPageRoute(
      builder: (context) =>
          joinGame(currUser: widget.currentUser),
    );
    Navigator.push(context, route);
  }


  @override
  Widget build(BuildContext context) {
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
                      'Username: ${widget.currentUser.userId}',
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