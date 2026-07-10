package org.firstinspires.ftc.teamcode.useable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "DECODE Final Auto")
public class MainAutonomous extends OpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor intakeMotor;
    private DcMotor catapultMotor;

    private ElapsedTime autoTimer = new ElapsedTime();
    private ElapsedTime stateTimer = new ElapsedTime();

    private enum StartPosition {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    private enum AutoMode {
        SIDE_BALLS,
        RELEASE_AREA
    }

    private enum AutoState {
        SIDE_INTAKE_FORWARD,
        STOP_BEFORE_SHOOT,
        SHOOT,
        RESET_CATAPULT,
        AFTER_SHOOTING_LOAD,
        RETURN_TO_START,
        TURN_TO_RELEASE_AREA,
        DRIVE_TO_RELEASE_AREA,
        RELEASE_AREA_INTAKE_FORWARD,
        STOP
    }

    private StartPosition startPosition = StartPosition.TOP_RIGHT;
    private AutoMode mode = AutoMode.SIDE_BALLS;
    private AutoState state = AutoState.SIDE_INTAKE_FORWARD;

    private final double DRIVE_SPEED = 0.45;
    private final double RELEASE_DRIVE_SPEED = 0.3;
    private final double TURN_SPEED = 0.35;
    private final double INTAKE_SPEED = 1.0;
    private final double CATAPULT_SHOOT_POWER = 1.0;
    private final double CATAPULT_RESET_POWER = -0.35;

    private final long SIDE_INTAKE_TIME = 1300;
    private final long RELEASE_AREA_INTAKE_TIME = 1100;
    private final long STOP_BEFORE_SHOOT_TIME = 250;
    private final long SHOOT_TIME = 450;
    private final long RESET_TIME = 350;

    private final int BALLS_PER_LOAD = 3;
    private final int SIDE_LOADS = 3;

    private int ballsShotThisLoad = 0;
    private int sideLoadsDone = 0;
    private int totalShots = 0;

    /*
    Methods Guide:

        This is a regular OpMode autonomous using an enum state machine.

        init():
            Runs once when INIT is pressed.
            Gets the motors from the hardwareMap.

        init_loop():
            Runs over and over before PLAY is pressed.
            Use this to choose the starting position.

            left bumper:
                TOP_LEFT

            right bumper:
                TOP_RIGHT

            dpad left:
                BOTTOM_LEFT

            dpad right:
                BOTTOM_RIGHT

        start():
            Runs once when PLAY is pressed.
            Resets timers and starts autonomous.

        loop():
            Runs over and over after PLAY is pressed.
            Runs the autonomous state machine.

        stop():
            Runs when the OpMode stops.
            Stops all motors.

        Main auto idea:
            1. Start from the selected position.
            2. Go forward on that side.
            3. Run intake to collect 3 balls.
            4. Stop the drive.
            5. Shoot 3 balls with the catapult.
            6. Repeat side-ball loop.
            7. When side balls are done, back up to starting area.
            8. Turn toward the loading / release area.
            9. Drive behind where balls are released.
            10. Keep intaking 3 balls and shooting 3 balls until auto ends.

        Important:
            This is time-based.
            You must tune the time values for your real robot.
    */

    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        catapultMotor = hardwareMap.get(DcMotor.class, "catapultMotor");

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        catapultMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        catapultMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        catapultMotor.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Selected Start", startPosition);
        telemetry.update();
    }

    @Override
    public void init_loop() {
        if (gamepad1.left_bumper) {
            startPosition = StartPosition.TOP_LEFT;
        }

        if (gamepad1.right_bumper) {
            startPosition = StartPosition.TOP_RIGHT;
        }

        if (gamepad1.dpad_left) {
            startPosition = StartPosition.BOTTOM_LEFT;
        }

        if (gamepad1.dpad_right) {
            startPosition = StartPosition.BOTTOM_RIGHT;
        }

        telemetry.addData("Choose Start Position", "");
        telemetry.addData("Left Bumper", "TOP_LEFT");
        telemetry.addData("Right Bumper", "TOP_RIGHT");
        telemetry.addData("Dpad Left", "BOTTOM_LEFT");
        telemetry.addData("Dpad Right", "BOTTOM_RIGHT");
        telemetry.addData("Selected Start", startPosition);
        telemetry.update();
    }

    @Override
    public void start() {
        autoTimer.reset();
        stateTimer.reset();

        mode = AutoMode.SIDE_BALLS;
        state = AutoState.SIDE_INTAKE_FORWARD;

        ballsShotThisLoad = 0;
        sideLoadsDone = 0;
        totalShots = 0;
    }

    private void goTo(AutoState newState) {
        stopAll();
        state = newState;
        stateTimer.reset();
    }

    private boolean autoAlmostOver() {
        return autoTimer.seconds() >= 29.5;
    }

    private boolean isTopSide() {
        return startPosition == StartPosition.TOP_LEFT ||
                startPosition == StartPosition.TOP_RIGHT;
    }

    private boolean isRightSide() {
        return startPosition == StartPosition.TOP_RIGHT ||
                startPosition == StartPosition.BOTTOM_RIGHT;
    }

    private long getReturnToStartTime() {
        if (isTopSide()) {
            return 2000;
        }

        return 1500;
    }

    private long getTurnToReleaseAreaTime() {
        if (isTopSide()) {
            return 500;
        }

        return 350;
    }

    private long getDriveToReleaseAreaTime() {
        if (isTopSide()) {
            return 1500;
        }

        return 900;
    }

    private void turnTowardReleaseArea(double speed) {
        if (startPosition == StartPosition.TOP_RIGHT) {
            turnLeft(speed);
        } else if (startPosition == StartPosition.BOTTOM_RIGHT) {
            turnRight(speed);
        } else if (startPosition == StartPosition.TOP_LEFT) {
            turnRight(speed);
        } else {
            turnLeft(speed);
        }
    }

    private void stopDrive() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    private void stopIntake() {
        intakeMotor.setPower(0);
    }

    private void stopCatapult() {
        catapultMotor.setPower(0);
    }

    private void stopAll() {
        stopDrive();
        stopIntake();
        stopCatapult();
    }

    private void drive(double leftPower, double rightPower, double speed) {
        leftMotor.setPower(leftPower * speed);
        rightMotor.setPower(rightPower * speed);
    }

    private void driveForward(double speed) {
        drive(1, 1, speed);
    }

    private void driveBackward(double speed) {
        drive(-1, -1, speed);
    }

    private void turnRight(double speed) {
        drive(1, -1, speed);
    }

    private void turnLeft(double speed) {
        drive(-1, 1, speed);
    }

    private void intakeIn() {
        intakeMotor.setPower(INTAKE_SPEED);
    }

    private void shootCatapult() {
        catapultMotor.setPower(CATAPULT_SHOOT_POWER);
    }

    private void resetCatapult() {
        catapultMotor.setPower(CATAPULT_RESET_POWER);
    }

    private void startShootingLoad() {
        ballsShotThisLoad = 0;
        goTo(AutoState.STOP_BEFORE_SHOOT);
    }

    private void finishOneShot() {
        ballsShotThisLoad++;
        totalShots++;
        goTo(AutoState.RESET_CATAPULT);
    }

    private void afterLoadFinished() {
        if (mode == AutoMode.SIDE_BALLS) {
            sideLoadsDone++;

            if (sideLoadsDone < SIDE_LOADS) {
                goTo(AutoState.SIDE_INTAKE_FORWARD);
            } else {
                goTo(AutoState.RETURN_TO_START);
            }
        } else {
            goTo(AutoState.RELEASE_AREA_INTAKE_FORWARD);
        }
    }

    @Override
    public void loop() {
        telemetry.addData("Start Position", startPosition);
        telemetry.addData("Mode", mode);
        telemetry.addData("State", state);
        telemetry.addData("Auto Time", autoTimer.seconds());
        telemetry.addData("State Time", stateTimer.milliseconds());
        telemetry.addData("Side", isRightSide() ? "RIGHT" : "LEFT");
        telemetry.addData("Side Loads Done", sideLoadsDone);
        telemetry.addData("Balls Shot This Load", ballsShotThisLoad);
        telemetry.addData("Total Shots", totalShots);
        telemetry.update();

        if (autoAlmostOver() && state != AutoState.STOP) {
            goTo(AutoState.STOP);
        }

        switch (state) {
            case SIDE_INTAKE_FORWARD:
                driveForward(DRIVE_SPEED);
                intakeIn();

                if (stateTimer.milliseconds() >= SIDE_INTAKE_TIME) {
                    startShootingLoad();
                }
                break;

            case STOP_BEFORE_SHOOT:
                stopAll();

                if (stateTimer.milliseconds() >= STOP_BEFORE_SHOOT_TIME) {
                    goTo(AutoState.SHOOT);
                }
                break;

            case SHOOT:
                stopDrive();
                stopIntake();
                shootCatapult();

                if (stateTimer.milliseconds() >= SHOOT_TIME) {
                    finishOneShot();
                }
                break;

            case RESET_CATAPULT:
                stopDrive();
                stopIntake();
                resetCatapult();

                if (stateTimer.milliseconds() >= RESET_TIME) {
                    if (ballsShotThisLoad < BALLS_PER_LOAD) {
                        goTo(AutoState.SHOOT);
                    } else {
                        goTo(AutoState.AFTER_SHOOTING_LOAD);
                    }
                }
                break;

            case AFTER_SHOOTING_LOAD:
                afterLoadFinished();
                break;

            case RETURN_TO_START:
                driveBackward(0.5);
                stopIntake();

                if (stateTimer.milliseconds() >= getReturnToStartTime()) {
                    mode = AutoMode.RELEASE_AREA;
                    goTo(AutoState.TURN_TO_RELEASE_AREA);
                }
                break;

            case TURN_TO_RELEASE_AREA:
                turnTowardReleaseArea(TURN_SPEED);

                if (stateTimer.milliseconds() >= getTurnToReleaseAreaTime()) {
                    goTo(AutoState.DRIVE_TO_RELEASE_AREA);
                }
                break;

            case DRIVE_TO_RELEASE_AREA:
                driveForward(0.45);

                if (stateTimer.milliseconds() >= getDriveToReleaseAreaTime()) {
                    goTo(AutoState.RELEASE_AREA_INTAKE_FORWARD);
                }
                break;

            case RELEASE_AREA_INTAKE_FORWARD:
                driveForward(RELEASE_DRIVE_SPEED);
                intakeIn();

                if (stateTimer.milliseconds() >= RELEASE_AREA_INTAKE_TIME) {
                    startShootingLoad();
                }
                break;

            case STOP:
                stopAll();
                break;
        }
    }

    @Override
    public void stop() {
        stopAll();
    }
}