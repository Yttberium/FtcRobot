package org.firstinspires.ftc.teamcode.teamcode.Subsystems;


import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.smartcluster.oracleftc.commands.Command;
import com.smartcluster.oracleftc.commands.helpers.InstantCommand;
import com.smartcluster.oracleftc.utils.input.OracleGamepad;


public class Hang {
    public CRServoImplEx hang1,hang2;
    private LynxEmbeddedIMU imu;
    public Hang(HardwareMap hardwareMap){
       hang1=hardwareMap.get(CRServoImplEx.class, "hang1");
        hang1=hardwareMap.get(CRServoImplEx.class, "hang2");
    }
    public Command hang(OracleGamepad gamepad){
        return new Command.CommandBuilder()
                .update(()->{
                    hang1.setPower(0.8);
                    hang2.setPower(0.8);

                })
                .build();

    }
}
