package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.TestBench;

@TeleOp
public class DistanceSensorPractice extends OpMode {

    TestBench bench = new TestBench();

    @Override
    public void init(){
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        double distance = bench.getDistance();
        if (distance <= 10) telemetry.addLine("TOO CLOSE!!!");
        telemetry.addData("Distance", distance);
    }
}
