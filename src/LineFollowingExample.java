import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class LineFollowingExample {

    private static final double KP = 0.5; // Proportional gain
    private static final double TARGET_DISTANCE = 10; // Distance from the line to the robot's center

    public static void main(String[] args) {
        LineFollowingMoveController moveController = new LineFollowingMoveController(Motor.A, Motor.D);

        moveController.start();

        Button.ENTER.waitForPressAndRelease();

        moveController.stop();
    }

    static class LineFollowingMoveController {
        private static final int SPEED = 300;
        private static final double LINE_SLOPE = 1.0; // Slope of the line
        private static final double LINE_INTERCEPT = 20; // Y-intercept of the line

        private final RegulatedMotor leftMotor;
        private final RegulatedMotor rightMotor;

        public LineFollowingMoveController(RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
            this.leftMotor = leftMotor;
            this.rightMotor = rightMotor;
        }
public void start() {
            while (true) {
                // Get the robot's position (assuming X-axis)
                double robotX = 0.0;// Code to get the robot's X-coordinate

                // Calculate the desired position on the line based on the robot's X-coordinate
                double desiredY = LINE_SLOPE * robotX + LINE_INTERCEPT;

                // Calculate the error (deviation from the line)
                double error = desiredY - TARGET_DISTANCE;

                // Calculate the motor speeds based on the error
                int leftSpeed = (int) (SPEED + KP * error);
                int rightSpeed = (int) (SPEED - KP * error);

                // Set the motor speeds and direction
                leftMotor.setSpeed(leftSpeed);
                rightMotor.setSpeed(rightSpeed);

                // Move the robot forward
                leftMotor.forward();
                rightMotor.forward();

                if (Button.ESCAPE.isDown()) {
                    break;
                }
            }
        }

        public void stop() {
            leftMotor.stop(true);
            rightMotor.stop();
        }
    }
}