import org.opencv.core.Point;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;

public class PDController{
	
	//The travel speed of the motors
	private int SPEED = 100;
	//Proportional gain
	private float KP = 0.5f;
	//derivative
	private float KD = 1f;
	//the last error or deviation from the line
	private float lastError;
	
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	
	private Client client;
	
	private double[] line;
	private Point targetBall;
	
	private double actual_x0;
	private double actual_y0;
	private double actual_x1;
	private double actual_y1;
	
	//centimeters/pixel
	private final float SCALE = 0.15971606033F;
	
	
	

	public PDController(RegulatedMotor leftMotor2, RegulatedMotor rightMotor2, double[] line, Point targetBall, String serverIP) {
		super();
		this.leftMotor = leftMotor2;
		this.rightMotor = rightMotor2;
		this.client = Client.getClient(serverIP);
		this.line = line;
		this.targetBall = targetBall;
	}



	public void calculateAndSetMotorSpeed() {
		float error = (float) getError();
        float controlValue = KP * error + KD * (error - lastError);
        
        if (controlValue > 55)
        {
        	controlValue = 55;
        }

        if (controlValue < -55)
        {
        	controlValue = -55;
        }
        
        int leftMotorSpeed = 100 + (int) controlValue;
        int rightMotorSpeed = 100 - (int) controlValue;
        
        leftMotor.setSpeed(leftMotorSpeed);
        rightMotor.setSpeed(rightMotorSpeed);
        /*
        if(leftMotorSpeed<0) {
        	leftMotor.stop();
        	leftMotor.backward();
        }else {
        	leftMotor.stop();
        	leftMotor.forward();
        }
        
        if(rightMotorSpeed<0) {
        	rightMotor.stop();
        	rightMotor.backward();
        }else {
        	rightMotor.stop();
        	rightMotor.forward();
        }*/
        
        lastError = error;
	}



	private double getError() {
		client.sendMessage(MessageStrings.GETRobotPos.toString());
		//Wait for response NB blocking call!
		String[] arr = client.receiveMessage().split(";");
		
		String[] res0 = arr[0].split(",");
		actual_x0 = Double.parseDouble(res0[0]);
		actual_y0 = Double.parseDouble(res0[1]);
		
		String[] res1 = arr[1].split(",");
		actual_x1 = Double.parseDouble(res1[0]);
		actual_y1 = Double.parseDouble(res1[1]);
		
		double desired_y = line[0]*actual_x0+line[1];
		return Math.abs(desired_y-desired_y);
	}

	public void run() {
		leftMotor.setSpeed(SPEED);
		rightMotor.setSpeed(SPEED);
		
		double distanceToTarget = 11.0;
		leftMotor.forward();
		rightMotor.forward();
		while(distanceToTarget>10.0) {
			System.out.println("distance: _______________"+distanceToTarget);
			calculateAndSetMotorSpeed();
			distanceToTarget = distance(targetBall);
		}
		System.out.println("distance: _______________"+distanceToTarget+"   STOP");
		leftMotor.stop();
		rightMotor.stop();
	}



	private float distance(Point targetBall2) {
		float deltaX0 = (float) (targetBall2.x-actual_x0);
		float deltaY0 = (float) (targetBall2.y-actual_y0);
		
		float mul0 = (deltaX0*deltaX0)+(deltaY0*deltaY0);
		float sqr0 = (float) Math.sqrt(mul0);
		float dis0 = sqr0*(SCALE);
		
		float deltaX1 = (float) (targetBall2.x-actual_x1);
		float deltaY1 = (float) (targetBall2.y-actual_y1);
		
		float mul1 = (deltaX1*deltaX1)+(deltaY1*deltaY1);
		float sqr1 = (float) Math.sqrt(mul1);
		float dis1 = sqr1*(SCALE);
		
		//Return the lowest distance
		return dis0<=dis1 ? dis0 : dis1;
	}
	

}
