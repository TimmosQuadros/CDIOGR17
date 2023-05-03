import org.opencv.core.Point;
import java.util.ArrayList;
import java.util.List;

    public class PointCollection {
        public static List<Double> calculateDeltaY(List<Point> points, double a, double b) {
            List<Double> deltaY = new ArrayList<>();
            for (Point point : points) {
                double y = a * point.x + b;
                deltaY.add(point.y - y);			
			}
            return deltaY;
        }
    }