package org.firstinspires.ftc.team417_PowerPlay;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team417_PowerPlay.drive.SampleMecanumDrive;

abstract public class BaseOpMode extends LinearOpMode {

    DcMotor motorArm;

    Servo leftGrabberServo;
<<<<<<< HEAD
    Servo rightGrabberServo;
=======
>>>>>>> 0eb1321eccaff22ac406c1c7cb3937de6ed4e33e
    Toggler grabberToggle;

    SampleMecanumDrive drive;
    public static final double LEFT_GRABBER_OPEN = 0;
    public static final double RIGHT_GRABBER_OPEN = 1;
    public static final double LEFT_GRABBER_CLOSED = 1;
    public static final double RIGHT_GRABBER_CLOSED = 0;
    public static final double GRABBER_HALF_CLOSED = 0.8;

    public static final int MAX_ARM_POSITION = 1600;
    public static final int GROUND_JUNCT_ARM_POSITION = 300;
    public static final int LOW_JUNCT_ARM_POSITION = 800;
    public static final int MID_JUNCT_ARM_POSITION = 1200;
    public static final int HIGH_JUNCT_ARM_POSITION = 1600;
    public static final int MIN_ARM_POSITION = 0;
    public static final int ARM_ENCODER_TOLERANCE = 10;

    public static final int FIRST_CONE_STACK_ARM_POSITION = 500;
    public static final int SECOND_CONE_STACK_ARM_POSITION = 400;
    public void initializeHardware() {
        drive = new SampleMecanumDrive(hardwareMap);

        motorArm = hardwareMap.dcMotor.get("motorArm");

        motorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftGrabberServo = hardwareMap.servo.get("leftGrabberServo");
<<<<<<< HEAD
        rightGrabberServo = hardwareMap.servo.get("rightGrabberServo");
=======
>>>>>>> 0eb1321eccaff22ac406c1c7cb3937de6ed4e33e
        grabberToggle = new Toggler();

        drive.setMotorPowers(0, 0, 0, 0);
        motorArm.setPower(0);
    }

    public void mecanumDrive(double x, double y, double turning) {
        double powerFL;
        double powerFR;
        double powerBL;
        double powerBR;

        powerFL = y + x + turning;
        powerFR = y - x - turning;
        powerBL = y - x + turning;
        powerBR = y + x - turning;

        drive.setMotorPowers(powerFL, powerBL, powerBR, powerFR);
    }
}
