package org.firstinspires.ftc.teamcode.useable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Decode_2Motor_Auto", group = "Autonomous")
public class test extends LinearOpMode {

    // 1. Hardware Variables (Only two drive motors)
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor shooterFlywheel = null;

    // 2. Autonomous States
    private enum AutoState {
        SCAN_OBELISK,
        DRIVE_FORWARD,       // Step 1: Drive forward toward goal line
        TURN_TO_GOAL,        // Step 2: Turn on the spot toward the goal
        APPROACH_GOAL,       // Step 3: Drive directly up to the scoring area
        REV_SHOOTER,         // Step 4: Spin up flywheel
        LAUNCH_ARTIFACTS,    // Step 5: Score game pieces
        PARK_IN_ZONE,        // Step 6: Back up or pivot into scoring zone
        DONE
    }

    private AutoState currentState = AutoState.SCAN_OBELISK;
    private ElapsedTime stateTimer = new ElapsedTime();

    // 3. Encoder Math (Adjust these numbers for your physical wheels)
    static final double COUNTS_PER_MOTOR_REV  = 537.7;  // GoBilda Yellow Jacket
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // Standard 4-inch wheels
    static final double COUNTS_PER_INCH       = COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * Math.PI);

    @Override
    public void runOpMode() {
        // Hardware Mapping
        leftDrive       = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive      = hardwareMap.get(DcMotor.class, "right_drive");
        shooterFlywheel = hardwareMap.get(DcMotor.class, "shooter_flywheel");

        // Reverse left side so positive power moves both wheels forward
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        shooterFlywheel.setDirection(DcMotor.Direction.FORWARD);

        // Reset Encoders
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "2-Motor System Initialized");
        telemetry.update();

        waitForStart();
        stateTimer.reset();

        while (opModeIsActive()) {

            switch (currentState) {

                case SCAN_OBELISK:
                    // Perform vision scanning, then transition
                    encoderDriveSetup(36.0, 36.0, 0.5); // Prep to drive 36 inches straight
                    currentState = AutoState.DRIVE_FORWARD;
                    break;

                case DRIVE_FORWARD:
                    if (!driveMotorsBusy()) {
                        stopDriving();
                        // Execute a point turn (Left forward, Right backward turns right)
                        // Adjust these inch approximations based on your chassis width
                        encoderDriveSetup(12.0, -12.0, 0.4);
                        currentState = AutoState.TURN_TO_GOAL;
                    }
                    break;

                case TURN_TO_GOAL:
                    if (!driveMotorsBusy()) {
                        stopDriving();
                        encoderDriveSetup(15.0, 15.0, 0.5); // Approach the goal
                        currentState = AutoState.APPROACH_GOAL;
                    }
                    break;

                case APPROACH_GOAL:
                    if (!driveMotorsBusy()) {
                        stopDriving();
                        shooterFlywheel.setPower(0.85); // Spin up shooter
                        currentState = AutoState.REV_SHOOTER;
                        stateTimer.reset();
                    }
                    break;

                case REV_SHOOTER:
                    if (stateTimer.seconds() > 1.5) {
                        // Activate scoring mechanism here
                        currentState = AutoState.LAUNCH_ARTIFACTS;
                        stateTimer.reset();
                    }
                    break;

                case LAUNCH_ARTIFACTS:
                    if (stateTimer.seconds() > 2.0) {
                        shooterFlywheel.setPower(0.0);
                        // Back up 24 inches to park in the safe zone
                        encoderDriveSetup(-24.0, -24.0, 0.5);
                        currentState = AutoState.PARK_IN_ZONE;
                    }
                    break;

                case PARK_IN_ZONE:
                    if (!driveMotorsBusy()) {
                        stopDriving();
                        currentState = AutoState.DONE;
                    }
                    break;

                case DONE:
                    stopDriving();
                    telemetry.addData("Auto Status", "Complete!");
                    break;
            }

            telemetry.addData("Current State", currentState);
            telemetry.update();
        }
    }

    // Helper: Simplified 2-motor encoder setup
    public void encoderDriveSetup(double leftInches, double rightInches, double power) {
        leftDrive.setTargetPosition(leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH));
        rightDrive.setTargetPosition(rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH));

        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(power);
        rightDrive.setPower(power);
    }

    public void stopDriving() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    public boolean driveMotorsBusy() {
        return leftDrive.isBusy() && rightDrive.isBusy();
    }

    public void setDriveMode(DcMotor.RunMode mode) {
        leftDrive.setMode(mode);
        rightDrive.setMode(mode);
    }
}
