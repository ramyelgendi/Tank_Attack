package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Tower extends Component implements Runnable{
    private int life = 3;
    private final Image TowerPic,CannonPic,FirePic,heart;
    private final Rectangle towerRectangle,MissileFrame;
    private final int height=256,width=138,x=50,y=100;
    private int fire_x=162;
    private int fire_y=128;
    private final int fire_width=25;
    private final int fire_height=25;


    public Rectangle getTowerRectangle() { return towerRectangle; }
    public Rectangle getMissileFrame() { return MissileFrame; }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    public int getFire_width() { return fire_width; }
    public int getFire_height() { return fire_height; }
    public int getLife() { return life; }
    public void setLife(int life) { this.life = life; }
    public int getFire_x() { return fire_x; }
    public void setFire_x(int fire_x) { this.fire_x = fire_x; }
    public int getFire_y() { return fire_y; }
    public void setFire_y(int fire_y) { this.fire_y = fire_y; }

    Thread thread;
    public Tower(String pathTower,String pathCannon,String pathFire,String heartPath){
        TowerPic = new ImageIcon(pathTower).getImage();
        CannonPic = new ImageIcon(pathCannon).getImage();
        FirePic = new ImageIcon(pathFire).getImage();
        towerRectangle = new Rectangle(x,y,width,height);
        heart = new ImageIcon(heartPath).getImage();

        thread = new Thread(this);
        thread.start();

        MissileFrame = new Rectangle(fire_x,fire_y,fire_width,fire_height);
        resetFire();
    }
    void fire(int Move,int Accuracy,int tank_now_x,int tank_now_y){
        // CREATE MISSILE FOR TOWER, AND MAKE IT MOVE LIKE THE TANK.

        int deltaX= tank_now_x-x;
        int deltaY= tank_now_y-y;

        int move_x=deltaX/Move;
        int move_y=deltaY/Move;

        fire_x+=move_x+Accuracy;
        fire_y+=move_y+Accuracy;
        MissileFrame.x = fire_x+Accuracy;
        MissileFrame.y = fire_y+Accuracy;


    }
    public void resetFire(){
        fire_x=162;
        fire_y=128;
        MissileFrame.x = fire_x;
        MissileFrame.y = fire_y;
    }
    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(TowerPic,x,y,width,height,null);
//        g2d.drawRect(towerRectangle.x,towerRectangle.y,towerRectangle.width,towerRectangle.height);
        AffineTransform old = g2d.getTransform();
        g2d.rotate(Math.toRadians(30),135,135);
        g2d.drawImage(CannonPic,100,100,70,70,null);
        g2d.setTransform(old);
        g2d.drawImage(FirePic,fire_x,fire_y,fire_width,fire_height,null);
//        g2d.drawRect(fire_x,fire_y,fire_width,fire_height);

        if(life>=1)
            g2d.drawImage(heart, x+35, y+height+2, 20, 20, null);
        if(life>=2)
            g2d.drawImage(heart, x+55, y+height+2, 20, 20, null);
        if(life>=3)
            g2d.drawImage(heart, x+75, y+height+2, 20, 20, null);
        //things you draw after here will not be rotated
    }

    @Override
    public void run() {
        repaint();
    }
}
