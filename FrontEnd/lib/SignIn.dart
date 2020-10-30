import 'package:flutter/material.dart';
import 'package:flutter_app/UserDashboard.dart';
import 'Model/User.dart';
import 'main.dart';
import 'Request/LoginRequest.dart';
import 'Request/RegisterRequest.dart';
import 'Result/UserResult.dart';
import 'ServerFacade/ServerFacade.dart';

class SignIn extends StatefulWidget {
  @override
  _SignInState createState() => _SignInState();
}

class _SignInState extends State<SignIn> {
  TextEditingController firstNameController = TextEditingController();
  TextEditingController lastNameController = TextEditingController();
  TextEditingController userNameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
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

  void _signIn() async {
    print(firstNameController.text);
    print(lastNameController.text);
    print(userNameController.text);
    print(passwordController.text);

    RegisterRequest request = new RegisterRequest(userNameController.text, passwordController.text, firstNameController.text, lastNameController.text);
    UserResult result = await new ServerFacade().register(request);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    User currentUser = result.user;
    final route = MaterialPageRoute(
      builder: (context) =>
          UserDashboard(currentUser: currentUser),
    );
    Navigator.push(context, route);
  }

  void checkButtonEnabled() {
    String text1,text2,text3, text4;

    text1 = firstNameController.text ;
    text2 = lastNameController.text ;
    text3 = userNameController.text ;
    text4 = passwordController.text ;
    if(text1 == '' || text2 == '' || text3 == '' || text4 == '')
    {
      print('Text Field is empty, Please Fill All Data');
    }else{
      setState(() => buttonEnabled = true);
      print('Not Empty, All Text Input is Filled.');
    }

  }



  void _login() {
    final route = MaterialPageRoute(
      builder: (context) =>
          login(),
    );
    Navigator.push(context, route);
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
                      'Adventure Games',
                      style: TextStyle(
                          color: Colors.blue,
                          fontWeight: FontWeight.w500,
                          fontSize: 30),
                    )),
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Sign in',
                      style: TextStyle(fontSize: 20),
                    )),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: firstNameController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'First Name',
                    ),
                  ),
                ),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: lastNameController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Last Name',
                    ),
                  ),
                ),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: userNameController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'User Name',
                    ),
                  ),
                ),
                Container(
                  padding: EdgeInsets.fromLTRB(10, 10, 10, 0),
                  child: TextField(
                    obscureText: true,
                    controller: passwordController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Password',
                    ),
                  ),
                ),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: RaisedButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Sign In'),
                      onPressed: checkButtonVar() ? () => _signIn() : null,
                    )),
                Container(
                    child: Row(
                      children: <Widget>[
                        Text('Already have an account?'),
                        FlatButton(
                          textColor: Colors.blue,
                          child: Text(
                            'Login',
                            style: TextStyle(fontSize: 20),
                          ),
                          onPressed: () {
                            _login();
                          },
                        )
                      ],
                      mainAxisAlignment: MainAxisAlignment.center,
                    ))
              ],
            )));
  }
}