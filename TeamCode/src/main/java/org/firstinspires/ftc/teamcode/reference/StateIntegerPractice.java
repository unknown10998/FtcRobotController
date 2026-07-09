package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.reference.mechanisms.TestBench;

@Disabled
@Autonomous
public class StateIntegerPractice extends OpMode {

    TestBench bench  = new TestBench();
    private int state;

    @Override
    public void init() {
        bench.init(hardwareMap);
        state = 0;
    }

    @Override
    public void loop() {
        telemetry.addData("Current state", state);
        switch (state){
            case 0:
                telemetry.addLine("Press A to exit");
                if (gamepad1.a) state = 1;
                break;

            case 1:
                telemetry.addLine("Press B to exit");
                if (gamepad1.b) state = 2;
                break;

            case 2:
                telemetry.addLine("Press X to exit");
                if (gamepad1.x) state = 3;
                break;

            case 3:
                telemetry.addLine("Press Y to exit");
                if (gamepad1.y) state = 4;
                break;

            case 4:
                telemetry.addLine("Press D PAd Down to end");
                if (gamepad1.dpad_down) state = 5;
                break;

            default:
                telemetry.addLine("Auto State Machine finished.");
        }

        telemetry.update();
    }
}
