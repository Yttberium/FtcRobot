package org.firstinspires.ftc.teamcode.teamcode.TeleOp;

//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import org.firstinspires.ftc.teamcode.oracleftc.commands.Command;
//import org.firstinspires.ftc.teamcode.oracleftc.commands.CommandScheduler;
//import org.firstinspires.ftc.teamcode.oracleftc.commands.helpers.ParallelCommand;
//import org.firstinspires.ftc.teamcode.oracleftc.fsm.FSM;
//import org.firstinspires.ftc.teamcode.oracleftc.utils.input.OracleGamepad;
//
//import org.firstinspires.ftc.teamcode.teamcode.Subsystems.Intake;
//import org.firstinspires.ftc.teamcode.teamcode.Subsystems.Drivetrain;
//
//@TeleOp
//public class SoloOpMode extends LinearOpMode {
//    public enum States {
//        IDLE,
//        INTAKING,
//
//        LOW_BASKET,
//        LOW_CHAMBER
//
//        //todo: hang si high basket si high chamber daca ajunge bratul
//    }
//    private final CommandScheduler scheduler = new CommandScheduler();
//    @Override
//    public void runOpMode() throws InterruptedException {
//        Drivetrain drivetrain = new Drivetrain(hardwareMap);
//        Intake intake = new Intake(hardwareMap);
//
//        OracleGamepad gamepad = new OracleGamepad(gamepad1);
//
//        FSM<States> fsm = new FSM.FSMBuilder<States>()
//                .initial(States.IDLE)
//                .transition(States.IDLE, States.INTAKING, gamepad.triangle.pressed(), new ParallelCommand(
//                        intake.armGround(),
//                        intake.setPitchPos(1)
//                ))
//                .state(States.INTAKING, Command.builder()
//                        .update(()->{
//                            Command.run(intake.intake(gamepad));
//                            if(gamepad.b.state)
//                                Command.run(intake.armGround());
//                        })
//                        .build())
//                .state(States.IDLE, Command.builder()
//                        .update(()->{
//                            Command.run(intake.intake(gamepad));
//                            Command.run(intake.startArm());
//                            //Command.run(intake.startPitch());
//                        })
//                        .build())
//                .transition(States.IDLE, States.LOW_BASKET, gamepad.circle.pressed(), intake.armBasket())
//                .state(States.LOW_BASKET, Command.builder()
//                        .update(()->{
//                            Command.run(intake.intake(gamepad));
//                        })
//                        .build())
//                .transition(States.IDLE, States.LOW_CHAMBER, gamepad.cross.pressed(), intake.armChamber())
//                .state(States.LOW_CHAMBER, Command.builder()
//                        .update(()->{
//                            Command.run(intake.intake(gamepad));
//                        })
//                        .build())
//                .transition(States.LOW_BASKET, States.IDLE, gamepad.a.pressed(), intake.armGround())
//                .transition(States.LOW_CHAMBER, States.IDLE, gamepad.a.pressed(), intake.armGround())
//                .transition(States.LOW_CHAMBER, States.LOW_BASKET, gamepad.circle.pressed(), intake.armBasket())
//                .transition(States.LOW_BASKET, States.LOW_CHAMBER, gamepad.cross.pressed(), intake.armChamber())
//                .transition(States.LOW_BASKET, States.INTAKING, gamepad.triangle.pressed(), intake.intake(gamepad))
//                .transition(States.LOW_CHAMBER, States.INTAKING, gamepad.triangle.pressed(), intake.intake(gamepad))
//                .build();
//
//        scheduler.schedule(new ParallelCommand(
//                intake.init()
//        ));
//        scheduler.schedule(drivetrain.update(gamepad));
//
//        waitForStart();
//        //telemetry.addData("arm position", intake.telemetryData());
//        //telemetry.update();
//        while (opModeIsActive()) {
//            telemetry.addLine("hi");
//            telemetry.addData("arm", intake.position());
//            scheduler.update();
//            fsm.update();
//            gamepad.process();
//            telemetry.update();
//        }
//    }
//}
