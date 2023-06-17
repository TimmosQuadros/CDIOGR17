import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.opencv.core.Point;

public class Receiver implements Runnable{
	
	private Scanner input;
	private Socket socket;
	private WaitForCalibration waitForCalibration;
	private List<Point> ballPoints = new ArrayList<>();
	
	public Receiver(String ip) {
		waitForCalibration = new WaitForCalibration();
		
		int port = 4445;
        InetAddress serverAddress;
		try {
			serverAddress = InetAddress.getByName(ip);
			socket = new Socket(serverAddress, port);
			input = new Scanner(socket.getInputStream());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public Receiver() {
		//In case we don't want all the functionality
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			String message = input.nextLine();
			decode(message);
			ballPoints.clear();
		}
	}

	private void decode(String message) {
		if(message.contains(MessageStrings.ScaleFactor.toString())) {
			String[] array = message.split(":");
			waitForCalibration.setScaleFactor(Double.parseDouble(array[1]));
		}else if(message.contains(MessageStrings.Position.toString())) {
			String[] array = message.split(":");
			String[] coordinates = array[1].split(",");
			waitForCalibration.setStartPos(new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1])));
		}else if(message.contains(MessageStrings.BallsPos.toString())) {
			String array[] = message.split(":");
			String[] balls = array[1].split(";");
			for (String ball : balls) {
				String[] arr  = ball.split(",");
				ballPoints .add(new Point(Double.parseDouble(arr[0]),Double.parseDouble(arr[1])));
			}
			System.out.println(waitForCalibration.getBallPoints());
			waitForCalibration.setBallPoints(ballPoints);
		}else if(message.contains(MessageStrings.StartAngle.toString())) {
			String[] array = message.split(":");
			waitForCalibration.setStartAngle(Double.parseDouble(array[1]));
			System.out.println(Double.parseDouble(array[1]));
		}else if(message.contains(MessageStrings.TargetBall.toString())) {
			
		}
	}
	
	public Object decode1(String message) throws NumberFormatException{
		String[] array = message.split(":");
		if(message.contains(MessageStrings.Line.toString())) {
			String[] line = array[1].split(",");
			return new double[]{Double.parseDouble(line[0]),Double.parseDouble(line[1])};
		}else if(message.contains(MessageStrings.TargetBall.toString())) {
			String[] targetBall = array[1].split(",");
			return new Point(Double.parseDouble(targetBall[0]),Double.parseDouble(targetBall[1]));
		}else if(message.contains(MessageStrings.Position.toString())) {
			String[] coordinates = array[1].split(",");
			return new Point(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
		}
		return null;
	}

	public WaitForCalibration getWaitForCalibration() {
		return waitForCalibration;
	}

	public void setWaitForCalibration(WaitForCalibration waitForCalibration) {
		this.waitForCalibration = waitForCalibration;
	}
	
	

}
