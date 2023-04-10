import lejos.hardware.sensor.EV3UltrasonicSensor;

public class ColissionSubject extends Subject {
	
	private EV3UltrasonicSensor ultrasonicSensor;
	
	public ColissionSubject(EV3UltrasonicSensor ultrasonicSensor) {
		this.ultrasonicSensor = ultrasonicSensor;
	}
	
	@Override
	public void notifyObservers() {
		//initialize the sample array
		float[] sample = new float[ultrasonicSensor.sampleSize()];
		//Get a sample from the ul_sensor in distance-mode, the distance is measured in meters 
		ultrasonicSensor.getDistanceMode().fetchSample(sample, 0);
		//get the distance in meters from the sample
		float meters = sample[0];
		//Simple check for distance less than 0.05 meters = 5 centimeters.
		if(meters < 0.05f) {
			//Notify the observers that a collision needs to be avoided
			super.notifyObservers();
		}
	}
	

}
