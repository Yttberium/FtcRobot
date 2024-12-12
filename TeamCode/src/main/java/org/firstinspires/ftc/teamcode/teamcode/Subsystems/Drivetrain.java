package org.firstinspires.ftc.teamcode.teamcode.Subsystems;

import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.smartcluster.oracleftc.commands.Command;
import com.smartcluster.oracleftc.commands.helpers.InstantCommand;
import com.smartcluster.oracleftc.utils.input.OracleGamepad;

public class Drivetrain {
    private DcMotorEx frontRight, frontLeft, backRight, backLeft;
    private LynxEmbeddedIMU imu;
    public Drivetrain(HardwareMap hardwareMap){
        frontRight=hardwareMap.get(DcMotorEx.class, "frontright");
        frontLeft=hardwareMap.get(DcMotorEx.class, "frontleft");
        backRight=hardwareMap.get(DcMotorEx.class, "backright");
        backLeft=hardwareMap.get(DcMotorEx.class, "backleft");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public Command update(OracleGamepad gamepad){
        return new Command.CommandBuilder()
                .update(()->{
                    double y = -gamepad.left_stick.get().y;
                    double x = gamepad.left_stick.get().x * 1.1;
                    double rx = gamepad.right_stick.get().x;

                    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

                    double frontLeftPower=(y + x + rx)/denominator;
                    double backLeftPower=(y - x + rx)/denominator;
                    double frontRightPower=(y - x - rx)/denominator;
                    double backRightPower=(y + x - rx)/denominator;

                    frontLeft.setPower(frontLeftPower);
                    backLeft.setPower(backLeftPower);
                    frontRight.setPower(frontRightPower);
                    backRight.setPower(backRightPower);})
                .build();
    }
}
