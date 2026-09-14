import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Main TeleOp Java")
public class TankDrive extends OpMode {

    private DcMotor leftMotor;

    private DcMotor rightMotor;


    @Override
    public void init() {
        //Motors
        leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_motor");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }



    @Override
    public void loop() {
        drive();
    }



    private void drive(){
        double  leftPower  =  -gamepad1.left_stick_y;
        double  rightPower  =  -gamepad1.right_stick_y;

        setMotorSpeed(leftMotor, leftPower);
        setMotorSpeed(rightMotor, leftPower);

    }



    private void setMotorSpeed(DcMotor motor, double speed){
        //speed -1 to 1
        motor.setPower(speed);
    }
} 