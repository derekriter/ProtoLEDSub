package frc.robot;

import frc.robot.subsystems.LEDSubsystem;

public abstract class Constants {
    public abstract class LEDConsts {
        public static final int portID = 0;
        public static final int ledCount = 44;

        public static final LEDSubsystem.Range left = new LEDSubsystem.Range(0, ledCount / 2 - 1);
        public static final LEDSubsystem.Range right = new LEDSubsystem.Range(ledCount / 2, ledCount - 1);
    }
}
