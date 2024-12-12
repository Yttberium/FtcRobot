package org.firstinspires.ftc.teamcode.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.smartcluster.oracleftc.commands.Command;
import com.smartcluster.oracleftc.commands.CommandScheduler;
import com.smartcluster.oracleftc.commands.helpers.ParallelCommand;
import com.smartcluster.oracleftc.fsm.FSM;
import com.smartcluster.oracleftc.utils.input.OracleGamepad;

import org.firstinspires.ftc.teamcode.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.teamcode.Subsystems.Drivetrain;

@TeleOp
public class SoloOpMode extends LinearOpMode {
    public enum States {
        IDLE,
        INTAKE,

        BASKET,
        CHAMBER

        //todo: hang si high basket si high chamber daca ajunge bratul
    }
    private final CommandScheduler scheduler = new CommandScheduler();
    @Override
    public void runOpMode() throws InterruptedException {
        Drivetrain drivetrain = new Drivetrain(hardwareMap);
        Intake intake = new Intake(hardwareMap);

        OracleGamepad gamepad = new OracleGamepad(gamepad1);

        FSM<States> fsm = new FSM.FSMBuilder<States>()
                .initial(States.IDLE)
                .state(States.INTAKE, Command.builder()
                        .update(()->{
                            if(gamepad.x.state)
                                Command.run(intake.takein(gamepad));
                            else if(gamepad.circle.state)
                                Command.run(intake.takeout(gamepad));

                        })
                        .build())
                .state(States.IDLE, Command.builder()
                        .update(()->{
                           if(gamepad.triangle.state) intake.stop(gamepad);
                        })
                        .build())
                        .build();


        scheduler.schedule(drivetrain.update(gamepad));

        waitForStart();
        telemetry.update();
        while (opModeIsActive()) {
            telemetry.addLine("hi");
            scheduler.update();
            fsm.update();
            gamepad.process();
            telemetry.update();
        }
    }
}
