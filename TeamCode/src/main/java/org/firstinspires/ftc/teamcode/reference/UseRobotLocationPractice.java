package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class UseRobotLocationPractice extends OpMode {
    RobotLocationPractice robotLocation = new RobotLocationPractice(0.0, 0.0,0.0);


    private void reset(){
        robotLocation.setAngle(0);
        robotLocation.setX(0);
        robotLocation.setY(0);
    }

    @Override
    public void init() {
        reset();
        telemetry.addData("Angle", robotLocation.getAngle());
    }

    @Override
    public void loop() {
        robotLocation.turnRobot(gamepad1.right_stick_x * 0.1);
        if (gamepad1.dpad_right) robotLocation.addX(.1);
        else if (gamepad1.dpad_left) robotLocation.addX(-.1);
        else if (gamepad1.dpad_down) robotLocation.addY(1);
        else if (gamepad1.dpad_up) robotLocation.addY(-.1);
    }
}
