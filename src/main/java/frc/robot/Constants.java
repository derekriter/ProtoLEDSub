package frc.robot;

import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.LEDSubsystem.Range.InheritMode;

public abstract class Constants {
    public abstract class LEDConsts {
        public static final int portID = 0;
        public static final int ledCount = 30 * 4;

        public static final LEDSubsystem.Range left = new LEDSubsystem.Range(0, 29);
        public static final LEDSubsystem.Range _leftMirror = new LEDSubsystem.Range(30, left, InheritMode.MIRROR);
        public static final LEDSubsystem.Range right = new LEDSubsystem.Range(59, 89);
        public static final LEDSubsystem.Range _rightMirror = new LEDSubsystem.Range(90, right, InheritMode.MIRROR);
    }
}
