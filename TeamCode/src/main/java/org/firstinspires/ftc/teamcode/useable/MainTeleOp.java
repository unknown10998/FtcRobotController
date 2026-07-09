package org.firstinspires.ftc.teamcode.useable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.useable.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.reference.mechanisms.MecanumDrive;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends OpMode {
    private MecanumDrive driveMotors = new MecanumDrive();
    private Intake intake = new Intake();
    private ElapsedTime timer = new ElapsedTime();
    private double forward, strafe, rotate, slowMode, intakePower;

    private final double SLOW_MODE_SPEED = 0.5, STOP_LIMIT = 3;

    private void driveMotorsWithJoystick(){
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;
        slowMode = gamepad1.left_bumper ? SLOW_MODE_SPEED : 1;

        driveMotors.fieldOrientedDrive(forward * slowMode, strafe * slowMode, rotate * slowMode);
    }

    private void runIntakeMotors(){
        double intakePower = gamepad1.right_trigger - gamepad1.left_trigger;
        intake.intakeMotor(intakePower);

        if (gamepad1.x) {
            intake.deployServo();
        }

        if (gamepad1.y) {
            intake.stowServo();
        }
    }

    private void checkStop(){
        if (gamepad1.b && timer.seconds() >= STOP_LIMIT) {
            intake.stop();
            timer.reset();
        }

        else{
            timer.reset();
        }
    }

    @Override
    public void init() {
        timer.reset();
        driveMotors.init(hardwareMap);
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {
        driveMotorsWithJoystick();
        runIntakeMotors();
        checkStop();
    }
}
