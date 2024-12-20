package org.firstinspires.ftc.teamcode.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.smartcluster.oracleftc.math.control.EdgeDetector;
import com.smartcluster.oracleftc.math.control.MotionState;
import com.smartcluster.oracleftc.math.control.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.smartcluster.oracleftc.commands.Command;
import com.smartcluster.oracleftc.commands.CommandScheduler;
import com.smartcluster.oracleftc.commands.helpers.InstantCommand;
import com.smartcluster.oracleftc.commands.helpers.ParallelCommand;
import com.smartcluster.oracleftc.commands.helpers.SequentialCommand;
import com.smartcluster.oracleftc.fsm.FSM;
import com.smartcluster.oracleftc.hardware.OracleLynxVoltageSensor;
import com.smartcluster.oracleftc.hardware.Subsystem;
import com.smartcluster.oracleftc.hardware.SubsystemFlavor;
import com.smartcluster.oracleftc.math.control.TrapezoidalMotionProfile;
import com.smartcluster.oracleftc.utils.input.OracleGamepad;
import com.smartcluster.oracleftc.hardware.Subsystem;
import com.smartcluster.oracleftc.hardware.SubsystemFlavor;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.teamcode.TeleOp.SoloOpMode;
@Override
public class SliderUp extends Subsystem {
    private final CommandScheduler scheduler = new CommandScheduler();
    private DcMotorEx intdr, intst, pitchEncoder1, pitchEncoder2;
    private OracleLynxVoltageSensor voltageSensor;
    public static PIDController sliderPIDController = new PIDController(0.001, 0.0, 0.0001);
    public static TrapezoidalMotionProfile sliderMotionProfile = new TrapezoidalMotionProfile(1000, 1000, 750);
    private DigitalChannel sliderLimit;
    public static int currentLimit = 4000;

    private EdgeDetector detector = new EdgeDetector(EdgeDetector.EdgeType.FALLING);

    public static double tolerance = 30;

    public SliderUp(OpMode opmode) {
        super(opMode);
        intdr = hardwareMap.get(DcMotorEx.class, "intdr");
        intst = hardwareMap.get(DcMotorEx.class, "intst");
        voltageSensor = hardwareMap.getAll(OracleLynxVoltageSensor.class).iterator().next();
        sliderLimit.setMode(DigitalChannel.Mode.INPUT);
        intst.setCurrentAlert(currentLimit, CurrentUnit.MILLIAMPS);
        intdr.setCurrentAlert(currentLimit, CurrentUnit.MILLIAMPS);

        intdr.setDirection(DcMotorSimple.Direction.REVERSE);
    }



}