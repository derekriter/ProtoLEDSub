package frc.robot.commands.led;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDSubsystem;

public class SetLEDOff extends Command {

    private final LEDSubsystem ledSub;
    private final LEDSubsystem.Range[] ranges;

    public SetLEDOff(LEDSubsystem _ledSub, LEDSubsystem.Range... _ranges) {
        ledSub = _ledSub;
        ranges = _ranges;

        addRequirements(ledSub);
    }

    @Override
    public void initialize() {
        for(LEDSubsystem.Range r : ranges) {
            ledSub.removeAnim(r);
            for(int i = r.start(); i <= r.end(); i++) {
                ledSub.setPixel(r, i, Color.kBlack);
            }
        }
    }
    @Override
    public void execute() {}
    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return true;
    }
    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
