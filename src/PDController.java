import org.opencv.core.Point;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PDController{
	
	//The travel speed of the motors
	private int SPEED = 100;
	//Proportional gain
	private float KP = 0.5f;
	//derivative
	private float KD = 1f;
	//the last error or deviation from the line
	private float lastError;
	
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	private Client client;
	
	private double[] line;
	private Point targetBall;
	
	private double actual_x;
	private double actual_y;
	
	

	public PDController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, double[] line, Point targetBall, String serverIP) {
		super();
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.client = Client.getClient(serverIP);
		this.line = line;
		this.targetBall = targetBall;
	}



	public void calculateAndSetMotorSpeed() {
		float error = (float) getError();
        float controlValue = KP * error + KD * (error - lastError);
        
        int leftMotorSpeed = 170 + (int) controlValue;
        int rightMotorSpeed = 170 - (int) controlValue;
        
        leftMotor.setSpeed(leftMotorSpeed);
        rightMotor.setSpeed(rightMotorSpeed);
        
        // Continue with your line following logic
        
        lastError = error;
	}



	private double getError() {
		client.sendMessage(MessageStrings.GETRobotPos.toString());
		//Wait for response NB blocking call!
		String[] res = client.receiveMessage().split(",");
		
		actual_x = Double.parseDouble(res[0]);
		actual_y = Double.parseDouble(res[1]);
		
		double desired_y = line[0]*actual_x+line[1];
		return actual_y-desired_y;
	}

	public void run() {
		leftMotor.setSpeed(SPEED);
		rightMotor.setSpeed(SPEED);
		leftMotor.forward();
		rightMotor.forward();
		double distanceToTarget = distance(targetBall,actual_x,actual_y);
		while(distanceToTarget>1.0) {
			distanceToTarget = distance(targetBall,actual_x,actual_y);
			calculateAndSetMotorSpeed();
		}
		leftMotor.stop();
		rightMotor.stop();
	}



	private double distance(Point targetBall2, double actual_x2, double actual_y2) {
		return Math.sqrt(Math.pow(targetBall2.x-actual_x2, 2)+Math.pow(targetBall2.y-actual_y2,2));
	}
	

}
