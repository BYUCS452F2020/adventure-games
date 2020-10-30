import 'package:flutter/material.dart';

class LoadingPage {
  static Scaffold loading() {
    return Scaffold(
        appBar: AppBar(
          title: Text('Loading Screen'),
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Loading...',
                      style: TextStyle(fontSize: 20),
                    )),
              ],
            )));
  }
}