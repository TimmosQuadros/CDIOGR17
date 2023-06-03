
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.internal.ev3.EV3MotorPort;
import lejos.internal.ev3.EV3Port;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.Move.MoveType;
import lejos.robotics.navigation.MovePilot;

public class Main {
	
	public static void main(String[] args) {
		try {
			
			//EV3
	    	Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 56).offset(-72);
	    	Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 56).offset(72);
	    	Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2},WheeledChassis.TYPE_DIFFERENTIAL); 
	    	MovePilot pilot = new MovePilot(chassis);
	    	pilot.setLinearSpeed(100);
	    	//pilot.setAngularSpeed(100);
	    	//pilot.setAngularAcceleration(100);
	    	Motor.B.setSpeed(9999);
	    	Motor.C.setSpeed(9999);
	    	
	    	
	    	
	    	
	    	
	    	//Network
	    	int port = 4445;
	    	String address = "192.168.248.115";
	        InetAddress serverAddress = InetAddress.getByName(address);
	        Socket socket = new Socket(serverAddress, port);
			Scanner input = new Scanner(socket.getInputStream());
			
			
			
			
			while (true) {
				String msg = input.nextLine(); //Blocking call, waits for input from server...
				//String moveType = pilot.getMovement().getMoveType().name();
				boolean moveForward = false, moveBackward = false, moveLeft = false, moveRight = false;
				
				
				if(msg.equalsIgnoreCase("MoveForward")) {
					if(!pilot.isMoving()) {
						pilot.forward();
					}
				}else if(msg.equalsIgnoreCase("MoveBackward")) {
					if(!pilot.isMoving()) {
						pilot.backward();
						moveBackward = true;
					}
				}else if(msg.equalsIgnoreCase("MoveLeft")) {
					if(!pilot.isMoving()) {
						pilot.arcForward(-1);
						moveLeft = true;
					}
				}else if(msg.equalsIgnoreCase("MoveRight")) {
					if(!pilot.isMoving()) {
						pilot.arcForward(1);
						moveRight = true;
					}
				}else if(msg.equalsIgnoreCase("StopMoveForward")) {
					if(pilot.isMoving()) {
						pilot.stop();
					}
				}else if(msg.equalsIgnoreCase("StopMoveBackward")) {
					if(pilot.isMoving()) {
						pilot.stop();
					}
				}else if(msg.equalsIgnoreCase("StopMoveLeft")) {
					if(pilot.isMoving()) {
						pilot.stop();
					}
				}else if(msg.equalsIgnoreCase("StopMoveRight")) {
					if(pilot.isMoving()) {
						pilot.stop();
					}
				}else if(msg.equalsIgnoreCase("StartMotor")) {
					Motor.B.backward();
				}else if(msg.equalsIgnoreCase("StopMotor")) {
					Motor.B.stop();
					Motor.C.stop();
				}else if(msg.equalsIgnoreCase("ReverseMotor")) {
					Motor.B.forward();
				}else if(msg.equalsIgnoreCase("motorCOut")){
					Motor.C.stop();
					Motor.C.forward();
				}else if(msg.equalsIgnoreCase("motorCIn")){
					Motor.C.stop();
					Motor.C.backward();
				}
				/*else if(msg.contains("circles")) {
					String[] arr = msg.substring(9).split(";");
					for (String circ : arr) {
						String[] tmpArr = circ.split(",");
						double x=Double.parseDouble(tmpArr[0].substring(1));
						double y=Double.parseDouble(tmpArr[1].substring(0,tmpArr[1].length()-1));
						
						
					}
				}*/
				
				
				
				
				
				msg = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
            //{25,40}
	}

}
