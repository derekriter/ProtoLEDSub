package frc.robot;

import frc.robot.subsystems.LEDSubsystem;

public class RobotContainer {

    public final LEDSubsystem ledSub = new LEDSubsystem();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {}
}
