package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.reference.mechanisms.TestBench;

@Disabled
@TeleOp
public class LEDPractice extends OpMode {

    TestBench bench = new TestBench();

    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            bench.setRedLED(true);
            bench.setGreenLED(false);
        }

        else if (gamepad1.b){
            bench.setGreenLED(true);
            bench.setRedLED(false);
        }

        else if (gamepad1.y){
            bench.setRedLED(true);
            bench.setGreenLED(true);
        }
    }
}
