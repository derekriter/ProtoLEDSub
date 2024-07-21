package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.LEDConsts;
import frc.robot.commands.led.SetLEDAnim;
import frc.robot.commands.led.SetLEDOff;
import frc.robot.subsystems.LEDSubsystem;

public class Robot extends TimedRobot {

    private RobotContainer robotCont;

    @Override
    public void robotInit() {
        robotCont = new RobotContainer();
    }
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        new SetLEDOff(robotCont.ledSub, LEDConsts.left, LEDConsts.right).schedule();
    }
    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousInit() {
        new SetLEDAnim(robotCont.ledSub, new LEDSubsystem.BreatheAnim(Color.kYellow).withRanges(LEDConsts.left, LEDConsts.right)).schedule();
    }
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        new SetLEDAnim(robotCont.ledSub, new LEDSubsystem.ScanAnim(Color.kOrange).withRanges(LEDConsts.left, LEDConsts.right)).schedule();
    }
    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }
    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}
    @Override
    public void simulationPeriodic() {}
}
