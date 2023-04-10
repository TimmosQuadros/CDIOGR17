
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Main {
	
	public static void main(String[] args) {
		try {
			
			//EV3
	    	Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 56).offset(-72);
	    	Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 56).offset(72);
	    	Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2},WheeledChassis.TYPE_DIFFERENTIAL); 
	    	MovePilot pilot = new MovePilot(chassis);
	    	pilot.setLinearSpeed(999999);
	    	Motor.B.setSpeed(9999);
	    	
	    	//Network
	    	int port = 4445;
	    	String address = "192.168.137.183";
	        InetAddress serverAddress = InetAddress.getByName(address);
	        Socket socket = new Socket(serverAddress, port);
			Scanner input = new Scanner(socket.getInputStream());
			
			
			while (true) {
				String msg = input.nextLine(); //Blocking call, waits for input from server...
				if(msg.equalsIgnoreCase("MoveForward")) {
					pilot.forward();
				}else if(msg.equalsIgnoreCase("MoveBackward")) {
					pilot.backward();
				}else if(msg.equalsIgnoreCase("MoveLeft")) {
					pilot.arcForward(-1);
				}else if(msg.equalsIgnoreCase("MoveRight")) {
					pilot.arcForward(1);
				}else if(msg.equalsIgnoreCase("StopMoveForward")) {
					pilot.stop();
				}else if(msg.equalsIgnoreCase("StopMoveBackward")) {
					pilot.stop();
				}else if(msg.equalsIgnoreCase("StopMoveLeft")) {
					pilot.stop();
				}else if(msg.equalsIgnoreCase("StopMoveRight")) {
					pilot.stop();
				}else if(msg.equalsIgnoreCase("StartMotor")) {
					Motor.B.backward();
				}else if(msg.equalsIgnoreCase("StopMotor")) {
					Motor.B.stop();
				}else if(msg.equalsIgnoreCase("ReverseMotor")) {
					Motor.B.forward();
				}
				msg = "";
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					e.getMessage();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
            
	}

}
