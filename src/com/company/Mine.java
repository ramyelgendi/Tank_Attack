package com.company;

import javax.swing.*;
import java.awt.*;

public class Mine extends Component{
    private int height=25;
    private int width=25;
    private final int x,y;

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public Rectangle getMineRectangle() {
        return mineRectangle;
    }

    private final Rectangle mineRectangle;
    private final Image background;
    public Mine(String path, int newX,int newY){
        x = newX;
        y = newY;

        background = new ImageIcon(path).getImage();
        mineRectangle = new Rectangle(x+5,y+10,width-15,height-10);

    }
    @Override
    public void paint(Graphics g){
        g.drawImage(background,x,y,width,height,null);
//        g.drawRect(mineRectangle.x,mineRectangle.y,mineRectangle.width,mineRectangle.height);

    }
    public void explode(){
        width=0;
        height=0;
        mineRectangle.width=0;
        mineRectangle.height=0;

    }
}


