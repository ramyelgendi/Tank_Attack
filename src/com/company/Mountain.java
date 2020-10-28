package com.company;

import javax.swing.*;
import java.awt.*;

public class Mountain extends Component{
    private final Image background;

    public Rectangle getMountainRectangle() {
        return mountainRectangle;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    private final Rectangle mountainRectangle;
    private final int height=160,width=256,x=950,y=40;

    public Mountain(String path){
        background = new ImageIcon(path).getImage();
        mountainRectangle = new Rectangle(x,y+25,width,height-50);

    }
    @Override
    public void paint(Graphics g){
        g.drawImage(background,x,y,width,height,null);
//        g.drawRect(mountainRectangle.x,mountainRectangle.y,mountainRectangle.width,mountainRectangle.height);

    }
}
