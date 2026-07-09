package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "My First Autonomous")
public class Preset extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private BNO055IMU imu;

    private final double TURN_CONSTANT = 457.0 / 90.0;

    private void initHardware() {
        backLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        backRight = hardwareMap.get(DcMotor.class, "motorRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu.initialize(parameters);
    }

    private void end() {
        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }

    private double getHeading() {
        Orientation angles = imu.getAngularOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.DEGREES
        );

        return angles.firstAngle;
    }

    private double angleDifference(double target, double current) {
        double difference = target - current;

        while (difference > 180) {
            difference -= 360;
        }

        while (difference < -180) {
            difference += 360;
        }

        return difference;
    }

    private void drive(long time, double left, double right, double speed) {
        if (speed <= 0) {
            end();
            return;
        }

        backLeft.setPower(left * speed);
        frontLeft.setPower(left * speed);
        backRight.setPower(right * speed);
        frontRight.setPower(right * speed);

        sleep((long)(time / speed));

        end();
    }

    private void drive(long time, double left, double right) {
        drive(time, left, right, 1.0);
    }

    private void turnToAngle(double targetAngle, double speed) {
        double startTime = getRuntime();
        double heading = getHeading();
        double error = angleDifference(targetAngle, heading);

        if (Double.isNaN(heading) || Double.isNaN(error)) {
            if (targetAngle > 0) {
                drive((long)(TURN_CONSTANT * Math.abs(targetAngle)), 1, -1, speed);
            } else {
                drive((long)(TURN_CONSTANT * Math.abs(targetAngle)), -1, 1, speed);
            }

            return;
        }

        while (opModeIsActive() && Math.abs(error) > 2 && getRuntime() - startTime < 5) {
            heading = getHeading();
            error = angleDifference(targetAngle, heading);

            if (Double.isNaN(heading) || Double.isNaN(error)) {
                end();
                return;
            }

            double turnPower = Math.abs(error) * 0.01;

            if (turnPower < 0.15) {
                turnPower = 0.15;
            }

            if (turnPower > speed) {
                turnPower = speed;
            }

            if (error > 0) {
                backLeft.setPower(turnPower);
                frontLeft.setPower(turnPower);
                backRight.setPower(-turnPower);
                frontRight.setPower(-turnPower);
            } else {
                backLeft.setPower(-turnPower);
                frontLeft.setPower(-turnPower);
                backRight.setPower(turnPower);
                frontRight.setPower(turnPower);
            }

            telemetry.addData("Target", targetAngle);
            telemetry.addData("Heading", heading);
            telemetry.addData("Error", error);
            telemetry.addData("Turn Power", turnPower);
            telemetry.update();
        }

        end();
    }

    private void driveForward(long time, double speed) {
        drive(time, 1, 1, speed);
    }

    private void driveBackword(long time, double speed) {
        drive(time, -1, -1, speed);
    }

    private void turnRight(double angle, double speed) {
        turnToAngle(angle, speed);
    }

    private void turnLeft(double angle, double speed) {
        turnToAngle(-angle, speed);
    }

    private void driveForward(long time) {
        drive(time, 1, 1, 1);
    }

    private void driveBackword(long time) {
        drive(time, -1, -1, 1);
    }

    private void turnRight(double angle) {
        turnToAngle(angle, 0.3);
    }

    private void turnLeft(double angle) {
        turnToAngle(-angle, 0.3);
    }

    @Override
    public void runOpMode() {
        initHardware();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Heading", getHeading());
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            turnRight(90);

            driveForward(1000, 0.5);

            end();
        }
    }
}

/*/*
Methods Guide:

    time:
        Time is in milliseconds.
        Example: 1000 = 1 second.

    motor power:
        Left and right motor powers use -1.0 to 1.0.
        1.0 means forward.
        -1.0 means backward.
        0.0 means stop.

    speed:
        Speed uses 0.0 to 1.0.
        Example: 0.5 = half speed.

    initHardware():
        Gets the drivetrain motors from the hardwareMap.
        Sets encoders, brake mode, and motor directions.

    end():
        Stops all drivetrain motors.

    drive(time, leftPower, rightPower, speed):
        Drives using separate left and right side powers.
        Example: drive(1000, 1, 1, 0.5) drives forward for 1 second at half speed.
        Example: drive(500, 1, -1, 0.5) turns right for 0.5 seconds at half speed.

    drive(time, leftPower, rightPower):
        Same as drive(time, leftPower, rightPower, speed), but uses full speed.

    driveForward(time, speed):
        Drives forward for a certain time.

    driveForward(time):
        Drives forward at full speed.

    driveBackword(time, speed):
        Drives backward for a certain time.

    driveBackword(time):
        Drives backward at full speed.

    turnRight(angle, speed):
        Turns right using the special TURN_CONSTANT number.
        Angle is in degrees.

    turnRight(angle):
        Turns right using default speed.

    turnLeft(angle, speed):
        Turns left using the special TURN_CONSTANT number.
        Angle is in degrees.

    turnLeft(angle):
        Turns left using default speed.
/*

public class MyFIRSTJavaOpMode extends LinearOpMode {
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    private final double TURN_CONSTANT = 457.0 / 90.0;
    private final long TURN_OFFSET = 60;
    private final double DEFAULT_TURN_SPEED = 0.5;

    private void initHardware() {
        backLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        backRight = hardwareMap.get(DcMotor.class, "motorRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
    }

    private void end() {
        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }

    private void drive(long time, double left, double right, double speed) {
        if (speed <= 0) {
            end();
            return;
        }

        backLeft.setPower(left * speed);
        frontLeft.setPower(left * speed);
        backRight.setPower(right * speed);
        frontRight.setPower(right * speed);

        sleep((long)(time / speed));

        end();
    }

    private void drive(long time, double left, double right) {
        drive(time, left, right, 1.0);
    }

    private void driveForward(long time, double speed) {
        drive(time, 1, 1, speed);
    }

    private void driveForward(long time) {
        drive(time, 1, 1, 1.0);
    }

    private void driveBackword(long time, double speed) {
        drive(time, -1, -1, speed);
    }

    private void driveBackword(long time) {
        drive(time, -1, -1, 1.0);
    }

    private void turnRight(double angle, double speed) {
        long turnTime = (long)(TURN_CONSTANT * angle) + TURN_OFFSET;
        drive(turnTime, 1, -1, speed);
    }

    private void turnRight(double angle) {
        turnRight(angle, DEFAULT_TURN_SPEED);
    }

    private void turnLeft(double angle, double speed) {
        long turnTime = (long)(TURN_CONSTANT * angle) + TURN_OFFSET;
        drive(turnTime, -1, 1, speed);
    }

    private void turnLeft(double angle) {
        turnLeft(angle, DEFAULT_TURN_SPEED);
    }

    @Override
    public void runOpMode() {
        initHardware();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            driveForward(1000, 0.5);

            turnRight(90);

            driveForward(1000, 0.5);

            turnLeft(45);

            driveBackword(1000, 0.5);

            end();
        }
    }
}
*/