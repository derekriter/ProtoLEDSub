package frc.robot;

import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.LEDSubsystem.LEDProtocol;

public class RobotContainer {

    public final LEDSubsystem ledSub = new LEDSubsystem(LEDProtocol.WS2815);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {}
}
