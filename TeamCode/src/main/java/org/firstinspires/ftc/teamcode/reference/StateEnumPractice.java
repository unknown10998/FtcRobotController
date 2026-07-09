package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.reference.mechanisms.TestBench;

@Disabled
@Autonomous
public class StateEnumPractice extends OpMode {

    TestBench bench  = new TestBench();
    private States state;

    private enum States {
        WAITING, FORWARD, CLAW, TURN, BACK, END
    }

    @Override
    public void init() {
        bench.init(hardwareMap);
        state = States.WAITING;
    }

    @Override
    public void loop() {
        telemetry.addData("Current state", state);
        switch (state){
            case WAITING:
                telemetry.addLine("Press A to exit");
                if (gamepad1.a) state = States.FORWARD;
                break;

            case FORWARD:
                telemetry.addLine("Press B to exit");
                if (gamepad1.b) state = States.CLAW;
                break;

            case CLAW:
                telemetry.addLine("Press X to exit");
                if (gamepad1.x) state = States.TURN;
                break;

            case TURN:
                telemetry.addLine("Press Y to exit");
                if (gamepad1.y) state = States.BACK;
                break;

            case BACK:
                telemetry.addLine("Press D PAd Down to end");
                if (gamepad1.dpad_down) state = States.END;
                break;

            default:
                telemetry.addLine("Auto State Machine finished.");
        }

        telemetry.update();
    }
}
