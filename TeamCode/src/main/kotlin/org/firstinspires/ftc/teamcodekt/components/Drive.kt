@file:Suppress("LocalVariableName")
@file:ConfigKt

package org.firstinspires.ftc.teamcodekt.components

import com.arcrobotics.ftclib.hardware.RevIMU
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import ftc.rogue.blacksmith.BlackOp
import ftc.rogue.blacksmith.BlackOp.Companion.hwMap
import ftc.rogue.blacksmith.annotations.ConfigKt
import ftc.rogue.blacksmith.util.kt.invoke
import ftc.rogue.blacksmith.util.kt.maxMagnitudeAbs
import ftc.rogue.blacksmith.util.kt.pow
import org.firstinspires.ftc.teamcodekt.components.meta.DeviceNames
import java.util.*
import kotlin.math.*

@JvmField
var tiltCorrectionMult = 0.001

@JvmField
var imuAngleUsed = 2 // or 0/1, not sure which one it is... will have to test.

class Drivetrain {
    private val frontLeft  = hwMap<DcMotorEx>(DeviceNames.DRIVE_FL).apply { direction = Direction.REVERSE }
    private val frontRight = hwMap<DcMotorEx>(DeviceNames.DRIVE_FR)
    private val backLeft   = hwMap<DcMotorEx>(DeviceNames.DRIVE_BL).apply { direction = Direction.REVERSE }
    private val backRight  = hwMap<DcMotorEx>(DeviceNames.DRIVE_BR)

    private val imu = RevIMU(hwMap)

    init {
        imu.init()

        withEachMotor {
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }
    }

    private var shouldDriveRC = true

    private val green = """
        🟩🟩🟩🟩🟩🟩🟩◼️◼️◼️◼️◼️🟩🟩🟩🟩🟩🟩
        🟩🟩🟩🟩🟩🟩◼️🟫🟫🟫🟫🟫◼️🟩🟩🟩🟩🟩
        🟩🟩🟩🟩🟩◼️🟫🟫🟫🟫◼️◼️◼️◼️🟩🟩🟩🟩
        🟩🟩🟩🟩🟩◼️🟫🟫🟫◼️🟦🟦🟦🟦◼️🟩🟩🟩
        🟩🟩🟩◼️◼️◼️🟫🟫🟫◼️🟦🟦🟦🟦◼️🟩🟩🟩
        🟩🟩🟩◼️🟫◼️🟫🟫🟫◼️🟦🟦🟦🟦◼️🟩🟩🟩
        🟩🟩🟩◼️🟫◼️🟫🟫🟫◼️🟦🟦🟦🟦◼️🟩🟩🟩
        🟩🟩🟩◼️🟫◼️🟫🟫🟫🟫◼️◼️◼️◼️🟩🟩🟩🟩
        🟩🟩🟩◼️🟫◼️🟫🟫🟫🟫🟫🟫🟫◼️🟩🟩🟩🟩
        🟩🟩🟩◼️◼️◼️🟫🟫🟫🟫🟫🟫🟫◼️🟩🟩🟩🟩
        🟩🟩🟩🟩🟩◼️🟫🟫🟫◼️◼️◼️🟫◼️🟩🟩🟩🟩
        🟩🟩🟩🟩🟩◼️🟫🟫🟫◼️🟩◼️🟫◼️🟩🟩🟩🟩
        🟩🟩🟩🟩🟩◼️◼️◼️◼️◼️🟩◼️◼️◼️🟩🟩🟩🟩
    """.trimIndent()

    private val red = green.replace("\uD83D\uDFE9", "\uD83D\uDFE5")

    private val elapsedTime = ElapsedTime()

    private var currentColor = red

    fun drive(gamepad: Gamepad, powerMulti: Double) {
        if (elapsedTime.milliseconds() > 1000) {
            currentColor = if (currentColor === red) green else red
            elapsedTime.reset()
        }

        if (shouldDriveRC) {
            driveRC(gamepad, powerMulti)
        } else {
            driveFC(gamepad, powerMulti)
        }

        BlackOp.mTelemetry.addLine(currentColor)
    }

    fun switchMode() {
        shouldDriveRC = !shouldDriveRC
    }

    fun resetIMU() {
        imu.reset()
    }

    private fun driveRC(gamepad: Gamepad, powerMulti: Double) {
        val (x, y, _r) = gamepad.getDriveSticks()
        val r = _r * .9

        val theta = atan2(y, x)
        val power = hypot(x, y)

        val xComponent = power * cos(theta - PI / 4)
        val yComponent = power * sin(theta - PI / 4)

        val max = maxMagnitudeAbs<Double>(xComponent, yComponent, 1e-16)

        val correctionTilt = tiltCorrectionMult * imu.angles.get(imuAngleUsed)

        val powers = doubleArrayOf(
            power * (xComponent / max) + r + correctionTilt, // TODO: Check if this code works, also if imuAngleUsed is correct.
            power * (yComponent / max) - r + correctionTilt, // TODO: If I add a power value to each one, then will all motors go forward?
            power * (yComponent / max) + r + correctionTilt,
            power * (xComponent / max) - r + correctionTilt,
        )

        if (power + abs(r) > 1) {
            powers.mapInPlace { it / (power + abs(r)) }
        }

        val _powerMulti = if (!gamepad.isAnyJoystickTriggered()) 0.0 else powerMulti

        powers.mapInPlace { (it pow 3) * _powerMulti }

        withEachMotor {
            this.power = powers[it]
        }
    }

    private fun driveFC(gamepad: Gamepad, powerMulti: Double) {
        val (x, _y, rx) = gamepad.getDriveSticks()
        val y = -_y

        val heading = Math.toRadians(-imu.heading)
        val rotX = x * cos(heading) - y * sin(heading)
        val rotY = x * sin(heading) + y * cos(heading)

        val powers = doubleArrayOf(
            rotY + rotX - rx,
            rotY - rotX + rx,
            rotY - rotX - rx,
            rotY + rotX + rx,
        )

        val max = powers.max()
        if (max > 1) {
            powers.mapInPlace { it / max }
        }

        val _powerMulti = if (!gamepad.isAnyJoystickTriggered()) 0.0 else powerMulti

        powers.mapInPlace { (it pow 3) * _powerMulti }

        withEachMotor {
            this.power = powers[it]
        }
    }

    private fun DoubleArray.mapInPlace(transform: (Double) -> Double) = repeat(size) {
        this[it] = transform(this[it])
    }

    private fun withEachMotor(transformation: DcMotorEx.(Int) -> Unit) {
        frontLeft .transformation(0)
        frontRight.transformation(1)
        backLeft  .transformation(2)
        backRight .transformation(3)
    }
}