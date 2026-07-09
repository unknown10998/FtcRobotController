package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.reference.mechanisms.TestBench;

@Disabled
@TeleOp
public class IMUPractice extends OpMode {

    TestBench bench = new TestBench();

    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        double facingDeg = bench.getHeading(AngleUnit.DEGREES), facingRad = bench.getHeading(AngleUnit.RADIANS);
        telemetry.addData("Facing Deg", facingDeg);
        telemetry.addData("Facing Rad", facingRad);

        if (facingDeg >= 0.5 || facingDeg <= -0.5) {
            bench.setMotorSpeed(facingDeg / Math.abs(facingDeg));
        }

        else {
            bench.setMotorSpeed(0.0);
        }
    }
}
