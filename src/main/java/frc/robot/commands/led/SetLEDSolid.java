package frc.robot.commands.led;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDSubsystem;

public class SetLEDSolid extends Command {

    private final LEDSubsystem ledSub;
    private final LEDSubsystem.Range[] ranges;
    private final Color col;

    public SetLEDSolid(LEDSubsystem _ledSub, Color _col, LEDSubsystem.Range... _ranges) {
        ledSub = _ledSub;
        col = _col;
        ranges = _ranges;

        addRequirements(ledSub);
    }

    @Override
    public void initialize() {
        for(LEDSubsystem.Range r : ranges) {
            ledSub.removeAnim(r);
            for(int i = 0; i < r.length(); i++) {
                ledSub.setPixel(r, i, col);
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
