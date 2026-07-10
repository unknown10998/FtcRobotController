package org.firstinspires.ftc.teamcode.useable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.useable.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.useable.mechanisms.Catapult;

@Autonomous(name = "DECODE Main Auto")
public class MainAutonomous extends OpMode {
    private ArcadeDrive drive = new ArcadeDrive();
    private Catapult catapult = new Catapult();

    private DcMotor intakeMotor;

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
        RELEASE_CATAPULT,
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

    private final long SIDE_INTAKE_TIME = 1300;
    private final long RELEASE_AREA_INTAKE_TIME = 1100;
    private final long STOP_BEFORE_SHOOT_TIME = 250;

    private final long SHOOT_TIME = 450;
    private final long RELEASE_TIME = 350;

    private final int BALLS_PER_LOAD = 3;
    private final int SIDE_LOADS = 3;

    private int ballsShotThisLoad = 0;
    private int sideLoadsDone = 0;
    private int totalShots = 0;

    private boolean stateStarted = false;

    @Override
    public void init() {
        drive.init(hardwareMap);
        catapult.init(hardwareMap);

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

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

        drive.resetHeading();

        mode = AutoMode.SIDE_BALLS;
        state = AutoState.SIDE_INTAKE_FORWARD;

        ballsShotThisLoad = 0;
        sideLoadsDone = 0;
        totalShots = 0;
        stateStarted = false;
    }

    private void goTo(AutoState newState) {
        stopAll();
        state = newState;
        stateTimer.reset();
        stateStarted = false;
    }

    private boolean autoAlmostOver() {
        return autoTimer.seconds() >= 29.5;
    }

    private boolean isTopSide() {
        return startPosition == StartPosition.TOP_LEFT ||
                startPosition == StartPosition.TOP_RIGHT;
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

    private void driveFieldForward(double speed) {
        drive.fieldOrientedDrive(0, 1, speed);
    }

    private void driveFieldBackward(double speed) {
        drive.fieldOrientedDrive(0, -1, speed);
    }

    private void turnTowardReleaseArea(double speed) {
        if (startPosition == StartPosition.TOP_RIGHT) {
            drive.turnLeft(speed);
        } else if (startPosition == StartPosition.BOTTOM_RIGHT) {
            drive.turnRight(speed);
        } else if (startPosition == StartPosition.TOP_LEFT) {
            drive.turnRight(speed);
        } else {
            drive.turnLeft(speed);
        }
    }

    private void intakeIn() {
        intakeMotor.setPower(INTAKE_SPEED);
    }

    private void stopIntake() {
        intakeMotor.setPower(0);
    }

    private void stopAll() {
        drive.stop();
        stopIntake();
    }

    private void startShootingLoad() {
        ballsShotThisLoad = 0;
        goTo(AutoState.STOP_BEFORE_SHOOT);
    }

    private void finishOneShot() {
        ballsShotThisLoad++;
        totalShots++;
        goTo(AutoState.RELEASE_CATAPULT);
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
        telemetry.addData("Side Loads Done", sideLoadsDone);
        telemetry.addData("Balls Shot This Load", ballsShotThisLoad);
        telemetry.addData("Total Shots", totalShots);
        telemetry.update();

        if (autoAlmostOver() && state != AutoState.STOP) {
            goTo(AutoState.STOP);
        }

        switch (state) {
            case SIDE_INTAKE_FORWARD:
                driveFieldForward(DRIVE_SPEED);
                intakeIn();

                if (stateTimer.milliseconds() >= SIDE_INTAKE_TIME) {
                    startShootingLoad();
                }
                break;

            case STOP_BEFORE_SHOOT:
                drive.stop();
                stopIntake();

                if (stateTimer.milliseconds() >= STOP_BEFORE_SHOOT_TIME) {
                    goTo(AutoState.SHOOT);
                }
                break;

            case SHOOT:
                drive.stop();
                stopIntake();

                if (!stateStarted) {
                    catapult.shoot();
                    stateStarted = true;
                }

                if (stateTimer.milliseconds() >= SHOOT_TIME) {
                    finishOneShot();
                }
                break;

            case RELEASE_CATAPULT:
                drive.stop();
                stopIntake();

                if (!stateStarted) {
                    catapult.release();
                    stateStarted = true;
                }

                if (stateTimer.milliseconds() >= RELEASE_TIME) {
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
                driveFieldBackward(0.5);
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
                driveFieldForward(0.45);

                if (stateTimer.milliseconds() >= getDriveToReleaseAreaTime()) {
                    goTo(AutoState.RELEASE_AREA_INTAKE_FORWARD);
                }
                break;

            case RELEASE_AREA_INTAKE_FORWARD:
                driveFieldForward(RELEASE_DRIVE_SPEED);
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