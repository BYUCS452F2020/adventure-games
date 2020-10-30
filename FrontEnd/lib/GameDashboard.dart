import 'package:flutter/material.dart';
import 'package:flutter_app/Result/GameResult.dart';
import 'Model/User.dart';
import 'Model/Game.dart';
import 'Model/Player.dart';
import 'ServerFacade/ServerFacade.dart';

class GameDashboard extends StatefulWidget {
  GameDashboard({Key key, this.currPlayer}) : super(key: key);
  final Player currPlayer;
  @override
  _GameDashboardState createState() => _GameDashboardState();
}

class _GameDashboardState extends State<GameDashboard> {
  Game currGame;

  @override
  void initState(){
    super.initState();
    init();
  }

  void init() async {
    GameResult result = await new ServerFacade().getGame(widget.currPlayer.gameId);


  }

  void _killTarget() {

  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Game Dashboard'),
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                if (currGame.startTime == null) ...[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Game Code: ${currGame.code}',
                      style: TextStyle(fontSize: 20),
                    )),
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Players Remaining: ${currGame.playersRemainingCount}',
                      style: TextStyle(fontSize: 20),
                    )),
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Target: ${widget.currPlayer.targetId}',
                      style: TextStyle(fontSize: 20),
                    )),
                ]
                else ...[
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Game Code: ${currGame.code}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Players Remaining: ${currGame.playersRemainingCount}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Target: ${widget.currPlayer.targetId}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      height: 50,
                      padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                      child: RaisedButton(
                        textColor: Colors.white,
                        color: Colors.blue,
                        child: Text('Kill Target'),
                        onPressed: () {
                          _killTarget();
                        },
                      )),

                ]
              ],
            )));
  }
}