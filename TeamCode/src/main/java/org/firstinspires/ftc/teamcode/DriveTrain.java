package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class DriveTrain extends OpMode
{
    DcMotor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    boolean isTurning;
    double globalAngle, horizontal, vertical, joystickDistanceFromOrigin, angle, pwr, pwr2, target, turning, correction, x, y, rotation;
    double distancePerTick = (2 * Math.PI * 48) / 537.6;
    public double leftFrontMotorPos, leftFrontDistanceTraveled = 0, deltaLeftFrontMotorPos = 0, previousLeftFrontMotorPos;
    public double rightFrontMotorPos, rightFrontDistanceTraveled = 0, deltaRightFrontMotorPos = 0, previousRightFrontMotorPos;
    public double leftBackMotorPos, leftBackDistanceTraveled = 0, deltaLeftBackMotorPos = 0, previousLeftBackMotorPos;
    public double rightBackMotorPos, rightBackDistanceTraveled = 0, deltaRightBackMotorPos = 0, previousRightBackMotorPos;

    public void init(){}

    public void initialize()
    {
        leftFrontMotor = hardwareMap.dcMotor.get("leftFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("leftBackMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFrontMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rightBackMotor");

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();
    }

    public void loop()
    {
        correction = checkDirection();
        if (x == 0) {
            horizontal = 0.00000000000001;
        } else {
            horizontal = x;
        }
        vertical = y;
        turning = rotation;

        // Calculate distance between the current joystick position and the idle position
        joystickDistanceFromOrigin = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        //position = Math.abs((Math.sqrt(2)) * (leftFrontMotor.getCurrentPosition()) / (537.6) * 96 * Math.PI);

        // Call necessary methods
        calcPower();
        checkIfTurning();

        getAngle();
    }

    void setControlVariables(double xController, double yController, double rotationController)
    {
        x = xController;
        y = yController;
        rotation = rotationController;
    }

    void turn()
    {
        pwr = 0.5 * turning;
        pwr2 = 0.5 * turning;
        leftFrontMotor.setPower(-0.7 * pwr);
        rightFrontMotor.setPower(-0.7 * pwr2);
        leftBackMotor.setPower(-0.7 * pwr2);
        rightBackMotor.setPower(-0.7 * pwr);
        //orientation += (newTurn - oldTurn);
        target = globalAngle;
    }

    void move()
    {
        leftFrontMotor.setPower(-0.75 * pwr + correction);
        rightFrontMotor.setPower(0.75 * pwr2 + correction);
        leftBackMotor.setPower(-0.75 * pwr2 + correction);
        rightBackMotor.setPower(0.75 * pwr + correction);
    }

    private double getOrient()
    {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    private double checkDirection() {
        double angle, gain = .03;

        angle = getOrient();

        if (angle == target)
            correction = 0;
        else
            correction = -(angle - target);

        correction = correction * gain;

        return correction;
    }

    public void getAngle() {
        if (horizontal > 0) {
            angle = (Math.atan(-vertical / horizontal) - Math.PI / 4);

            // FOR FUTURE REFERENCE this ridiculous number is literally just degrees per radian.
            angle = angle - ((lastAngles.firstAngle) / 57.29577951);
        }
        if (horizontal < 0) {
            angle = (Math.atan(-vertical / horizontal) + Math.PI - Math.PI / 4);
            angle = angle - ((lastAngles.firstAngle) / 57.29577951);
        }
    }

    public void checkIfTurning() {
        if (gamepad1.right_stick_x != 0) {
            isTurning = true;
        }
        if ((gamepad1.left_stick_y != 0) || (gamepad1.left_stick_x != 0)) {
            isTurning = false;
        }
    }

    public void calcPower() {
        // Calculate motor power
        if (angle == 0) {
            pwr2 = 0.8 * (joystickDistanceFromOrigin * Math.sin(angle));
            pwr = pwr2;
        } else {
            pwr = 0.8 * (joystickDistanceFromOrigin * Math.cos(angle));
            pwr2 = 0.8 * (joystickDistanceFromOrigin * Math.sin(angle));
        }
    }

    public void getDistanceTraveled()
    {
        leftFrontMotorPos = leftFrontMotor.getCurrentPosition();
        deltaLeftFrontMotorPos = distancePerTick * (leftFrontMotorPos - previousLeftFrontMotorPos);
        leftFrontDistanceTraveled += deltaLeftFrontMotorPos;
        previousLeftFrontMotorPos = leftFrontMotorPos;

        rightFrontMotorPos = rightFrontMotor.getCurrentPosition();
        deltaRightFrontMotorPos = distancePerTick * (rightFrontMotorPos - previousRightFrontMotorPos);
        rightFrontDistanceTraveled += deltaRightFrontMotorPos;
        previousRightFrontMotorPos = rightFrontMotorPos;

        leftBackMotorPos = leftBackMotor.getCurrentPosition();
        deltaLeftBackMotorPos = distancePerTick * (leftBackMotorPos - previousLeftBackMotorPos);
        leftBackDistanceTraveled += deltaLeftBackMotorPos;
        previousLeftBackMotorPos = leftBackMotorPos;

        rightBackMotorPos = rightBackMotor.getCurrentPosition();
        deltaRightBackMotorPos = distancePerTick * (rightBackMotorPos - previousRightBackMotorPos);
        rightBackDistanceTraveled += deltaRightBackMotorPos;
        previousRightBackMotorPos = rightBackMotorPos;
    }

    public void stop()
    {
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
    }
}
