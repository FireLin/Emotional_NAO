This project is to integrate Microsoft Emotion API with the NAO Robot. The software extract images seen by NAO throught it's API, pass it to the Emotion API, and return back results for the NAO to react.

The project has two types of robots, a Dummy Robot which acts as a test if you do not own a NAO, and the NAO robot.

To run the project you need ant build 

just run in terminal 
> ant run

To switch between dummy and NAO, you need to go to java Main file
src/com/hlab/app/console/Main.java

and change the initButler(0) in the Main constructor.
0 for dummy
1 for NAO
