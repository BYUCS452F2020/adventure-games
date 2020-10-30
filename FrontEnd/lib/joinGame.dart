import 'package:flutter/material.dart';
import 'main.dart';
import 'Classes/user.dart';
import 'Classes/game.dart';

class joinGame extends StatefulWidget {
  joinGame({Key key, this.currUser}) : super(key: key);
  final user currUser;
  @override
  _joinGameState createState() => _joinGameState();
}

class _joinGameState extends State<joinGame> {
  TextEditingController gameIdController = TextEditingController();
  TextEditingController codeController = TextEditingController();
  bool buttonEnabled;

  @override
  void initState(){
    super.initState();
    init();
  }

  void init() async {
    buttonEnabled = false;
  }

  bool checkButtonVar () {
    return buttonEnabled;
  }


  void checkButtonEnabled() {
    String text1,text2;

    text1 = gameIdController.text ;
    text2 = codeController.text ;
    if(text1 == '' || text2 == '')
    {
      print('Text Field is empty, Please Fill All Data');
    }else{
      setState(() => buttonEnabled = true);
      print('Not Empty, All Text Input is Filled.');
    }

  }



  void _join() {
    print(gameIdController.text);
    print(codeController.text);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Adventure Games'),
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Join Game',
                      style: TextStyle(
                          color: Colors.blue,
                          fontWeight: FontWeight.w500,
                          fontSize: 30),
                    )),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: gameIdController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Game Id',
                    ),
                  ),
                ),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: codeController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Code',
                    ),
                  ),
                ),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: RaisedButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Join'),
                      onPressed: checkButtonVar() ? () => _join() : null,
                    )),
              ],
            )));
  }
}