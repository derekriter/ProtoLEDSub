package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.Constants.LEDConsts;

public class RobotContainer {

    public RobotContainer() {
        configureBindings();

        AddressableLED leds = new AddressableLED(LEDConsts.portID);
        AddressableLEDBuffer buff = new AddressableLEDBuffer(LEDConsts.ledCount);

        leds.setLength(LEDConsts.ledCount);
        leds.start();

        for(int i = 0; i < buff.getLength(); i++) {
            buff.setRGB(i, 255, 0, 0);
        }
        leds.setData(buff);
    }

    private void configureBindings() {}
}
