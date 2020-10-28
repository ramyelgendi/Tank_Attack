package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;


public class Tank extends Component implements Runnable{
    private static int missile_x,missile_y,missile_height=20,missile_width=20;
    private final int height=60,width=100;
    private final Image background, SecondBg,heart,missile;
    private Polygon Frame, MissileFrame;
    private int RotationAngle,Life=3;
    private static int x=1050,y=310;

    public int getVarX() { return x; }

    public static void setVarX(int x) { Tank.x = x; }

    public int getVarY() { return y; }

    public static void setVarY(int y) { Tank.y = y; }

    public Polygon getFrame() { return Frame; }

    public void setFrame(Polygon frame) { Frame = frame; }

    public Polygon getMissileFrame() {
        return MissileFrame;
    }

    public void setMissileFrame(Polygon missileFrame) {
        MissileFrame = missileFrame;
    }

    public int getRotationAngle() {
        return RotationAngle;
    }

    public void setRotationAngle(int rotationAngle) {
        RotationAngle = rotationAngle;
    }

    public int getLife() {
        return Life;
    }

    public void setLife(int life) {
        Life = life;
    }

    public static void setX(int x) {
        Tank.x = x;
    }


    public static void setY(int y) {
        Tank.y = y;
    }

    public static int getMissile_x() {
        return missile_x;
    }

    public static void setMissile_x(int missile_x) {
        Tank.missile_x = missile_x;
    }

    public static int getMissile_y() {
        return missile_y;
    }

    public static void setMissile_y(int missile_y) {
        Tank.missile_y = missile_y;
    }

    public static int getMissile_height() {
        return missile_height;
    }

    public static void setMissile_height(int missile_height) {
        Tank.missile_height = missile_height;
    }

    public static int getMissile_width() {
        return missile_width;
    }

    public static void setMissile_width(int missile_width) {
        Tank.missile_width = missile_width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    Thread thread;
    public Tank(int random_x,int random_y,String path1,String path2,String heartPath,String missilePath){
        x=random_x;
        y=random_y;
        ArrayList<Integer> list = new ArrayList<>();

        list.add(45);list.add(90);list.add(135);list.add(180);
        list.add(225);list.add(270);list.add(315);
        Random rand = new Random();
        String temp_value=list.get(rand.nextInt(list.size())).toString();
        RotationAngle=Integer.parseInt(temp_value);
        background = new ImageIcon(path1).getImage();
        SecondBg = new ImageIcon(path2).getImage();
        heart = new ImageIcon(heartPath).getImage();
        missile = new ImageIcon(missilePath).getImage();

        MissileFrame = new Polygon();
        ResetMissile();


        Frame = new Polygon();
        Frame.addPoint(x,y);
        Frame.addPoint(x+width,y);
        Frame.addPoint(x+width,y+height);
        Frame.addPoint(x,y+height);

        thread = new Thread(this);
        thread.start();
    }
    @Override
    public void paint(Graphics g){
        int CenterX = width/2; int CenterY = height/2;
        FrameRotate(RotationAngle);

        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();

//        g2d.drawPolygon(Frame);
//        g2d.drawPolygon(MissileFrame);
        g2d.drawImage(missile, missile_x, missile_y, missile_width, missile_height, null);

        g2d.rotate(Math.toRadians(RotationAngle),x+CenterX,y+CenterY);
        switch (RotationAngle) {
            case 0, 45, 90, 270, 315 -> g2d.drawImage(background, x, y, width, height, null);
            case 135, 180, 225 -> {
                g2d.rotate(Math.toRadians(180), x + CenterX, y + CenterY);
                g2d.drawImage(SecondBg, x, y, width, height, null);
            }
        }
        g2d.setTransform(old);
        if(Life>=1)
            g2d.drawImage(heart, x+35, y+height+2, 20, 20, null);
        if(Life>=2)
            g2d.drawImage(heart, x+55, y+height+2, 20, 20, null);
        if(Life>=3)
            g2d.drawImage(heart, x+75, y+height+2, 20, 20, null);
    }

    public void FrameRotate(int Angle){
        Frame.reset();

        switch (Angle) {
            case 0, 180 -> {
                Frame.addPoint(x + 10, y + 10);
                Frame.addPoint(x + width - 10, y + 10);
                Frame.addPoint(x + width - 10, y + height - 10);
                Frame.addPoint(x + 10, y + height - 10);
            }
            case 45 -> {
                Frame.addPoint(x - 10, y + 20);
                Frame.addPoint(x + height - 50, y);
                Frame.addPoint(x + height + 30, y + 70);
                Frame.addPoint(x + 40, y + width);
                Frame.addPoint(x + 20, y + width - 20);
                Frame.translate(height / 3, -width / 5);
            }
            case 135 -> {
                Frame.addPoint(x + 5, y + 50);
                Frame.addPoint(x + height - 6, y);
                Frame.addPoint(x + height - 6, y + 50);
                Frame.addPoint(x + 5, y + width);
                Frame.translate(height / 3, -width / 5);
            }
            case 225 -> {
                Frame.addPoint(x - 20, y + 25);
                Frame.addPoint(x + height - 20, y + 20);
                Frame.addPoint(x + height + 20, y + width - 20);
                Frame.addPoint(x + 5, y + width - 30);
                Frame.translate(height / 3, -width / 5);
            }
            case 315 -> {
                Frame.addPoint(x + 50, y);
                Frame.addPoint(x + width, y);
                Frame.addPoint(x + 60, y + 55);
                Frame.addPoint(x, y + height);
            }
            case 90, 270 -> {
                Frame.addPoint(x + 5, y);
                Frame.addPoint(x + height - 6, y);
                Frame.addPoint(x + height - 6, y + width);
                Frame.addPoint(x + 5, y + width);
                Frame.translate(height / 3, -width / 5);
            }
        }

    }
    public void Fire(int Move){
        switch (RotationAngle){
            case 0:
                missile_x+=Move;
                MissileFrame.translate(Move,0);
                break;
            case 45:
                missile_x+=Move;
                missile_y+=Move;
                MissileFrame.translate(Move,Move);

                break;
            case 90:
                missile_y += (Move);
                MissileFrame.translate(0,Move);
                break;
            case 135:
                missile_x+=-Move;
                missile_y+=Move;
                MissileFrame.translate(-Move,Move);
                break;
            case 180:
                missile_x+=-Move;
                MissileFrame.translate(-Move,0);
                break;
            case 225:
                missile_x+=-Move;
                missile_y+=-Move;
                MissileFrame.translate(-Move,-Move);
                break;
            case 270:
                missile_y += -Move;
                MissileFrame.translate(0,-Move);

                break;
            case 315:
                missile_x+=Move;
                missile_y+=-Move;
                MissileFrame.translate(Move,-Move);
                break;
            default:
        }
    }
    public void ResetMissile(){
        missile_x=x+width/2;
        missile_y=y+height/2;
        MissileFrame.reset();
        MissileFrame.addPoint(missile_x+3,missile_y+6);
        MissileFrame.addPoint(missile_x+3+missile_width/3,missile_y+6);
        MissileFrame.addPoint(missile_x+3+missile_width/3,missile_y+missile_height/3 +6);
        MissileFrame.addPoint(missile_x+3,missile_y+missile_height/3 +6);

    }
    public void Rotate(){
        RotationAngle += 45;
        if(RotationAngle == 360){ RotationAngle = 0; }
    }
    public void OppositeRotate(){
        RotationAngle -= 45;
        if(RotationAngle == -45){ RotationAngle = 315; }
    }


    public void Move(int Move){
        switch (RotationAngle){
            case 0:
                x+=Move;
                Frame.translate(Move,0);
                Fire(Move);
                break;
            case 45:
                x+=Move;
                y+=Move;
                Frame.translate(Move,Move);
                Fire(Move);

                break;
            case 90:
                y += (Move);
                Frame.translate(0,Move);
                Fire(Move);
                break;
            case 135:
                x+=-Move;
                y+=Move;
                Frame.translate(-Move,Move);
                Fire(Move);
                break;
            case 180:
                x+=-Move;
                Frame.translate(-Move,0);
                Fire(Move);
                break;
            case 225:
                x+=-Move;
                y+=-Move;
                Frame.translate(-Move,-Move);
                Fire(Move);
                break;
            case 270:
                y += -Move;
                Frame.translate(0,-Move);
                Fire(Move);

                break;
            case 315:
                x+=Move;
                y+=-Move;
                Frame.translate(Move,-Move);
                Fire(Move);

                break;
            default:
        }
        if(x<0)x=0;
        if(y<0)y=0;
        if(x>1100) x=1100;
        if(y>350) y=350;

    }

    @Override
    public void run() {
        repaint();
    }
}
