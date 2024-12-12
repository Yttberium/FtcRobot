package org.firstinspires.ftc.teamcode.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.smartcluster.oracleftc.commands.Command;
import com.smartcluster.oracleftc.commands.helpers.InstantCommand;
import com.smartcluster.oracleftc.commands.helpers.ParallelCommand;
import com.smartcluster.oracleftc.commands.helpers.SequentialCommand;
import com.smartcluster.oracleftc.utils.input.OracleGamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    //All motors/servos/sensors
    private DcMotorEx arm;
    private ServoImplEx pitch;
    private CRServoImplEx intakest, intakedr;
    public Telemetry telemetry;
    public int[] armpos ={12,23,321};

    PID pid = new PID();
    ElapsedTime timer = new ElapsedTime();
    double lastError = 0;
    double integralSum = 0;


    public Intake(HardwareMap hardwareMap) {
        arm = hardwareMap.get(DcMotorEx.class, "arm");
        pitch = hardwareMap.get(ServoImplEx.class, "pitch");
        intakest = hardwareMap.get(CRServoImplEx.class, "intakest");
        intakedr = hardwareMap.get(CRServoImplEx.class, "intakedr");
        //arm.setDirection(DcMotorEx.Direction.REVERSE);
        //pitch.setPwmRange(new PwmControl.PwmRange(550, 2450));
        //intake.setPwmRange(new PwmControl.PwmRange(550, 2450));
    }

    public double NewPID(double reference, double state) {
        double error = reference - state;
        integralSum = integralSum + error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;
        timer.reset();
        double output = (error * PID.kP) + (derivative * PID.kD) + (integralSum * PID.kI);
        return output;
    }

    public Command takein(OracleGamepad gamepad) {
        return new InstantCommand(() -> {
            intakest.setPower(0.2);
            intakedr.setPower(-0.2);

        });
    }
        public Command takeout(OracleGamepad gamepad) {
            return new InstantCommand(() -> {
                intakest.setPower(-0.2);
                intakedr.setPower(0.2);

            });
        }
        public Command stop(OracleGamepad gamepad) {
            return new InstantCommand(() -> {
                intakest.setPower(0);
                intakedr.setPower(0);

        });
    }
        public Command arm(OracleGamepad gamepad) {
            return new Command.CommandBuilder()
                    .update(()->{

            })
                    .build();

        }
    public void telemetryData(){
        telemetry.addData("slider pos", arm.getCurrentPosition());
//        telemetry.addData("sliderLimit", arm.getState());
    }
    public Command length(int index){
        return new InstantCommand(()->{
            arm.setTargetPosition(armpos[index]);
            arm.setPower(1);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        });
    }
    public Command angle(int index){
        return new InstantCommand(()->{
            arm.setTargetPosition(armpos[index]);
            arm.setPower(1);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        });
    }








}

