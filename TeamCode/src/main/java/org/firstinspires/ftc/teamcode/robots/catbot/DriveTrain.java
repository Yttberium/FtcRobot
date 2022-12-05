package org.firstinspires.ftc.teamcode.robots.catbot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

class DriveTrain {
    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private DcMotorEx motorFrontRight = null;
    private DcMotorEx motorBackLeft = null;
    private DcMotorEx motorFrontLeft = null;
    private DcMotorEx motorBackRight = null;
    // regular drive
    private double powerLeft = 0;
    private double powerRight = 0;
    // mecanum types
    private double powerFrontLeft = 0;
    private double powerFrontRight = 0;
    private double powerBackLeft = 0;
    private double powerBackRight = 0;
    // Number variables
    private static final float DEADZONE = .1f;
    public DriveTrain(Telemetry telemetry, HardwareMap hardwareMap)
    {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
    }
    public void resetMotors() {
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mechanumDrive(0, 0, 0);
    }
    public void tankDrive(double left, double right) {
        powerRight = 0;
        powerLeft = 0;
        if (Math.abs(left) > DEADZONE) {
            powerLeft = -left;
        }
        if (Math.abs(right) > DEADZONE) {
            powerRight = -right;
        }
        motorFrontRight.setPower(powerRight);
        motorFrontLeft.setPower(powerLeft);
        motorBackRight.setPower(powerRight);
        motorBackLeft.setPower(powerLeft);
    }

    public void mechanumDrive(double forward, double strafe, double turn) {
        double r = Math.hypot(strafe, forward);
        double robotAngle = Math.atan2(forward, strafe) - Math.PI / 4;
        double rightX = turn;
        powerFrontLeft = r * Math.cos(robotAngle) + rightX;
        powerBackRight = r * Math.sin(robotAngle) - rightX;
        powerBackLeft = r * Math.sin(robotAngle) + rightX;
        powerFrontRight = r * Math.cos(robotAngle) - rightX;
        motorFrontLeft.setPower(powerFrontLeft);
        motorFrontRight.setPower(powerFrontRight);
        motorBackLeft.setPower(powerBackLeft);
        motorBackRight.setPower(powerBackRight);
    }

    public void motorInit()
    {
        motorFrontLeft = this.hardwareMap.get(DcMotorEx.class, "motorFrontLeft");
        motorBackLeft = this.hardwareMap.get(DcMotorEx.class, "motorBackLeft");
        motorFrontRight = this.hardwareMap.get(DcMotorEx.class, "motorFrontRight");
        motorBackRight = this.hardwareMap.get(DcMotorEx.class, "motorBackRight");
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        this.motorBackLeft.setDirection(DcMotorEx.Direction.REVERSE);
        this.motorFrontLeft.setDirection(DcMotorEx.Direction.REVERSE);
    }
    public int getMotorFrontLeftPosition(){return motorFrontLeft.getCurrentPosition();}
}
