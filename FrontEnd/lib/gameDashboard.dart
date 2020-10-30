import 'package:flutter/material.dart';
import 'Classes/user.dart';
import 'Classes/game.dart';
import 'Classes/player.dart';

class gameDashboard extends StatefulWidget {
  gameDashboard({Key key, this.currPlayer}) : super(key: key);
  final player currPlayer;
  @override
  _gameDashboardState createState() => _gameDashboardState();
}

class _gameDashboardState extends State<gameDashboard> {
  game currGame;

  @override
  void initState(){
    super.initState();
    init();
  }

  void init() async {
    currGame = await ServerFacade().getGame(widget.currPlayer.gameId);
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