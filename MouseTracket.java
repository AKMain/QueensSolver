
import java.awt.*;
import java.awt.MouseInfo;
import java.awt.Point;


public class MouseTracket {

    public static void main(String[] args) {

        try {
            Thread.sleep(3000);
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();

            Point mouseLocation = pointerInfo.getLocation();

            double x = mouseLocation.getX();
            double y = mouseLocation.getY();


            System.out.printf("Mouse position: x=%.0f, y=%.0f\n", x, y);

//            Robot robot = new Robot();
//            robot.mouseMove(665, 275);

        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}

