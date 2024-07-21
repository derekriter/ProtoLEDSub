package frc.robot.commands.led;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDSubsystem;

public class SetLEDAnim extends Command {

    private final LEDSubsystem ledSub;
    private final LEDSubsystem.Animation[] anims;

    public SetLEDAnim(LEDSubsystem _ledSub, LEDSubsystem.Animation... _anims) {
        ledSub = _ledSub;
        anims = _anims;

        addRequirements(ledSub);
    }

    @Override
    public void initialize() {
        for(LEDSubsystem.Animation a : anims) {
            ledSub.addAnim(a);
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
