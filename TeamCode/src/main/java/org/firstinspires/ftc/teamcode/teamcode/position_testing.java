package org.firstinspires.ftc.teamcode.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;


@Config
@TeleOp
public class position_testing extends LinearOpMode {
    public static double p=0.00,i=0.0,d=0;
    public static double f=0.0;
    public static int target = 0;
    public final double ticks_in_degree=700/180.0;
    Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {
        PIDFController pidController = new PIDFController(p,i,d,f);
        limelight=hardwareMap.get(Limelight3A.class, "limelight");
        telemetry=new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "frontright");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "frontleft");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "backright");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "backleft");
        DcMotorEx arm = hardwareMap.get(DcMotorEx.class, "arm");
        ServoImplEx pitch = hardwareMap.get(ServoImplEx.class, "pitch");
        CRServoImplEx intake = hardwareMap.get(CRServoImplEx.class, "intake");

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setDirection(DcMotorSimple.Direction.REVERSE);

            int armpos=arm.getCurrentPosition();
            double pid= pidController.calculate(armpos,target);
            double ff=Math.cos(Math.toRadians(target/ticks_in_degree))*f;

            double power=pid+ff;
            arm.setPower(power);

            telemetry.addData("pos ", armpos);
            telemetry.addData("target ", target);
            telemetry.update();
           // This is a pidf code

        }
}

