package org.firstinspires.ftc.teamcode.reference;

public class RobotLocationPractice {

    private double angle, x, y;

    public RobotLocationPractice(double angle, double x, double y){
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    public double getAngle(){
        return this.angle - 360.0 * Math.floor((this.angle + 180.0) / 360.0);
    }

    public void setAngle(double newAngle){
        this.angle = newAngle;
    }

    public double getX(){
        return this.x;
    }

    public void setX(double newX){
        this.x = newX;
    }

    public void addX(double xIncrement){
        this.x += xIncrement;
    }

    public double getY(){
        return this.y;
    }

    public void setY(double newY){
        this.y = newY;
    }

    public void addY(double yIncrement){
        this.y += yIncrement;
    }

    public void turnRobot(double angleIncrement){
        this.angle += angleIncrement;
    }
}
