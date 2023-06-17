
import org.opencv.core.Point;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Main {

	public static synchronized void main(String[] args) {
		//EV3
		/*Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 30).offset(110);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 30).offset(-110);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2},WheeledChassis.TYPE_DIFFERENTIAL); 
		MovePilot pilot = new MovePilot(chassis);
		pilot.setAngularSpeed(10);
		pilot.setLinearSpeed(10);*/
		RegulatedMotor leftMotor = Motor.A;
		RegulatedMotor rightMotor = Motor.D;
		RegulatedMotor harvester = Motor.B;
		Motor.C.setSpeed(1);
		
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 60).offset(-70);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 60).offset(70);
		
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		
		MovePilot movePilot = new MovePilot(chassis);
		
		//DifferentialPilot pilot1 = new DifferentialPilot(60, 97.5, Motor.A, Motor.D);


		//Network
		int port = 4445;
		String serverIP = "192.168.1.103";
		/*serverAddress = InetAddress.getByName(address);
		Socket socket = new Socket(serverAddress, port);
		Scanner input = new Scanner(socket.getInputStream());*/
		//LineFollowingExample lineFollowingExample = null;
		Client client = Client.getClient(serverIP);
		double[] line = null;
		Point targetBall = null;
		Receiver receiver = new Receiver();
		while(true) {
			String mes = client.receiveMessage();
			if(mes.equalsIgnoreCase(MessageStrings.Turn.toString())) {
				leftMotor.setSpeed(25);
				rightMotor.setSpeed(25);
				leftMotor.forward();
				rightMotor.backward();
				client.sendMessage(MessageStrings.Turn.toString());
			}else if(mes.contains(MessageStrings.Line.toString())) {
				line = (double[]) receiver.decode1(mes);
			}else if(mes.contains(MessageStrings.TargetBall.toString())) {
				targetBall = (Point) receiver.decode1(mes);
			}else if(mes.equalsIgnoreCase(MessageStrings.Stop.toString())) {
				leftMotor.stop();
				rightMotor.stop();
				movePilot.rotate(-15);
				client.sendMessage(MessageStrings.Stop.toString());
				if( targetBall!=null && line!=null) {
					PDController pdController = initiatePDController(line,targetBall,leftMotor,rightMotor, serverIP);
					harvester.setSpeed(9000);
					harvester.backward();
					pdController.run();
					client.sendMessage("finnish");
					harvester.stop();
				}else {
					System.err.println("ERROR: No line and no tableTennis targetball");
				}
			}
		}
		
		
		
		
		
		
		/*Receiver receiver = new Receiver();
		double[] line = null;
		Point targetBall = null;
		boolean targeting = false;
		while(true) {
			String message = "";
			message = client.receiveMessage();
			if(message.contains(MessageStrings.Line.toString())) {
				line = (double[]) receiver.decode1(message);
			}else if(message.contains(MessageStrings.TargetBall.toString())){
				targetBall = (Point) receiver.decode1(message);
			}else if(message.contains(MessageStrings.Position.toString()) && lineFollowingExample!=null) {
				//lineFollowingExample.lineFollowingMoveController.setRobotX(port);
			}

			if(line!=null && targetBall!=null && !targeting) {
				lineFollowingExample = new LineFollowingExample(Motor.A,Motor.D,line[0],line[1]);
				Thread task = new Thread(lineFollowingExample.lineFollowingMoveController);
				task.run();
				Motor.B.forward();
				Motor.C.forward();
				targeting = true;
			}
		}*/




	}

	private static PDController initiatePDController(double[] line, Point targetBall, RegulatedMotor leftMotor, RegulatedMotor rightMotor, String serverIP) {
		return new PDController(leftMotor, rightMotor, line, targetBall, serverIP);
	}

}
