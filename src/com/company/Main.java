package com.company;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

import static java.lang.Thread.sleep;

public class Main extends Container  {
    // Variables
    private static int counter=0;
    private static int towerAggressive=10;
    private static int towerAccuracy=0;
    private static int minesTotal = 10;
    private static final int step =10;
    boolean tankFire = false,towerFire = false;
    private final Image background, GameOverImage,WinnerImage;
    private static Tank tank;
    private static String mainPath,highscoresPath;
    private final ArrayList<Mine> mines;
    private static Tower tower;
    private static Mountain mountain;
    private static final int[] timerCounter = {0};
    private boolean IsGameEnd = false;

    public Main(String path){
        background = new ImageIcon(path).getImage();
        GameOverImage = new ImageIcon(mainPath+"gameover.png").getImage();
        WinnerImage =new ImageIcon(mainPath+"winner.png").getImage();

        tower = new Tower(mainPath + "Tower.png", mainPath + "Cannon.png", mainPath + "Fire.png",mainPath+"heart.png");
        mountain = new Mountain(mainPath + "Mount.png");

        int x=0,y=0;
        Random rand = new Random();
        boolean flag = true;
        while (flag) {
            x = rand.nextInt ((1100 - 750)+1) + 750;
            y = rand.nextInt ((350 - 150)+1) + 150;

            flag = tower.getTowerRectangle().contains(x, y) || mountain.getMountainRectangle().contains(x, y);

        }

        tank = new Tank(x,y,mainPath + "tank2.png", mainPath + "tank1.png", mainPath + "heart.png", mainPath + "missle.png");



        this.mines = new ArrayList<>();
        rand = new Random();
        x=0;y=0;
        for (int i = 0; i<minesTotal; i++){
            flag = true;
            while (flag) {
                x = rand.nextInt(1100);
                y = rand.nextInt(350);

                flag = tower.getTowerRectangle().contains(x, y) || mountain.getMountainRectangle().contains(x, y) || tank.getFrame().contains(x, y);

            }
            mines.add(i,new Mine(mainPath +"mine.png",x,y));
        }

//        thread = new Thread(this);
//        thread.start();
    }
    @Override
    public void paint(Graphics g){
        g.drawImage(background,0,0,1175,420,null);
        tower.paint(g);
        tank.paint(g);
        for (int i=0; i<minesTotal;i++){
            mines.get(i).paint(g);
            //mines.get(i).printXY();
            //mines.get(i).
        }
        mountain.paint(g);


        if(tank.getLife()==0){ g.drawImage(GameOverImage, 130, 10, 900, 350, null); }
        if(tower.getLife()==0){ g.drawImage(WinnerImage, 130, 10, 900, 350, null); }

        if(IsGameEnd && counter == 4){
            try { saveScores(); } catch (IOException e) { e.printStackTrace(); }
            try { sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            System.exit(0);

        }

        if(tank.getLife()==0 || tower.getLife()==0){ IsGameEnd=true;counter++;repaint();}
    }

    public void saveScores() throws IOException {
        File file = new File(highscoresPath+"highestScores.txt");
        String data;
        data="Game Score - Time = "+timerCounter[0]+" seconds - ";
        if(tower.getLife()==0)data+="Tank Won! \n";
        else data+="Tower Won! \n";
        data+="Tower Life: "+tower.getLife()+"\n";
        data+="Tank Life: "+tank.getLife()+"\n";
        data+="=====================\n\n";
        if(file.createNewFile())
            System.out.println("New scores file created in: "+highscoresPath+"highestScores.txt");
        FileWriter fr = new FileWriter(file, true);
        fr.write(data);
        fr.close();

    }



    public boolean checkCollide(int Move){
        boolean flag = false;

        tank.Move(Move);
        if(tank.getFrame().intersects(tower.getTowerRectangle()))
            flag = true;

        if(tank.getFrame().intersects(mountain.getMountainRectangle()))
            flag = true;

        tank.Move(-Move);

        return !flag;
    }
    public void mineCollide(){

        for(int i=0;i<minesTotal;i++)
            if(tank.getFrame().intersects(mines.get(i).getMineRectangle())){
                mines.get(i).explode();
                tank.setLife(0);
            }


    }
    public void FireTank(Frame jFrame){

        while (tankFire) {
            try {
                sleep(100);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            tank.Fire(-step * 4);
            if (tank.getMissileFrame().intersects(tower.getTowerRectangle())) {
                tank.ResetMissile();
                tower.setLife(tower.getLife()-1);
                tankFire = false;
            }

            if(Tank.getMissile_x()>=1175 || Tank.getMissile_y()>=420 || Tank.getMissile_x()<=0 || Tank.getMissile_y()<=0){
                tank.ResetMissile();
                tankFire = false;
            }



//            jFrame.repaint();
            jFrame.update(jFrame.getGraphics());
            jFrame.repaint();

        }

    }

    public void towerFire(Frame jFrame){
        if (!IsGameEnd) {
            towerFire = true;

            int tank_now_x=tank.getVarX(), tank_now_y=tank.getVarY();
            while (towerFire) {
                try {
                    sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                int towerFireSpeed = 10;
                tower.fire(towerFireSpeed,towerAccuracy,tank_now_x,tank_now_y);
                if (tank.getFrame().intersects(tower.getMissileFrame())) {
                    tower.resetFire();
                    tank.setLife(tank.getLife()-1);
                    towerFire = false;
                }
                if(tower.getFire_x()>=1175 || tower.getFire_y()>=420 || tower.getFire_x()<=0 || tower.getFire_y()<=0){
                    tower.resetFire();
                    towerFire = false;
                }
                jFrame.repaint();
                jFrame.update(jFrame.getGraphics());
                jFrame.repaint();

            }
        }

    }

    public static void main(String[] args) {
        mainPath = "/Users/ramyelgendi/IdeaProjects/TankAttack/src/com/company/img/";
        highscoresPath = "/Users/ramyelgendi/IdeaProjects/TankAttack/src/com/company/";

        // Creating GUI
        JFrame jFrame = new JFrame("Tank Attack");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);

        // Images
        BufferedImage Logo = null;
        try { Logo = ImageIO.read(new File(mainPath+"title.png")); } catch (IOException e) { e.printStackTrace(); }
        assert Logo != null;
        JLabel LogoPic = new JLabel(new ImageIcon(Logo.getScaledInstance(450,40,Image.SCALE_FAST)));

        // Labels
        JLabel gameAcc = new JLabel("<html><body><br>* Accuracy of the tower *</body></html>");gameAcc.setForeground(Color.RED);
        JLabel gameAgg = new JLabel("<html><body><br>* How aggressive are missiles of the tower *</body></html>");gameAgg.setForeground(Color.RED);
        JLabel gameMines = new JLabel("<html><body><br>* Number of mines *</body></html>");gameMines.setForeground(Color.RED);
        JLabel spacer = new JLabel("<html><body><br><br></body></html>");

        // Button
        JButton b=new JButton("Begin Game");
        b.setFocusable(false);

        // Sliders
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        slider.setPaintLabels(true);
        Hashtable<Integer, JLabel> position = new Hashtable<>();        // Add positions label in the slider
        position.put(0, new JLabel("Very Hard"));position.put(5, new JLabel("Hard"));position.put(10, new JLabel("Medium"));
        position.put(15, new JLabel("Easy"));position.put(20, new JLabel("Very Easy"));

        slider.setSnapToTicks(true);slider.setLabelTable(position);slider.setMajorTickSpacing(5);slider.setPaintTicks(true);
        slider.setPaintLabels(true);slider.setFocusable(false);

        Hashtable<Integer, JLabel> position2 = new Hashtable<>();        // Add positions label in the slider
        position2.put(5, new JLabel("Very Hard"));position2.put(10, new JLabel("Hard"));position2.put(15, new JLabel("Medium"));
        position2.put(20, new JLabel("Easy"));position2.put(25, new JLabel("Very Easy"));

        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 5, 25, 10);
        slider2.setPaintLabels(true);slider2.setSnapToTicks(true);slider2.setLabelTable(position2);slider2.setMajorTickSpacing(5);
        slider2.setPaintTicks(true);slider2.setPaintLabels(true);slider2.setFocusable(false);
        JSlider slider3 = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider3.setPaintLabels(true);slider3.setSnapToTicks(true);slider3.setMajorTickSpacing(5);
        slider3.setPaintTicks(true);slider3.setPaintLabels(true);slider3.setFocusable(false);

        // Panel
        JPanel panel=new JPanel(new GridLayout(10, 1));
        panel.setPreferredSize(new Dimension(40, 80));
        panel.add(LogoPic);
        panel.add(gameAcc);
        panel.add(spacer,"span, grow");
        panel.add(slider);
        panel.add(gameAgg);
        panel.add(spacer,"span, grow");
        panel.add(slider2);

        panel.add(gameMines);
        panel.add(spacer,"span, grow");
        panel.add(slider3);
        panel.add(spacer,"span, grow");
        panel.add(b);
        panel.setPreferredSize(new Dimension(10,10));
        panel.setBorder(new EmptyBorder(10, 300, 10, 300));

        jFrame.add(panel);


        jFrame.setBounds(100, 100, 1175, 420);
        jFrame.setVisible(true);

        b.addActionListener(e -> {
            towerAccuracy=slider.getValue();
            towerAggressive=slider2.getValue();
            minesTotal=slider3.getValue();


            System.out.println("The path of the folders that contains image is set to be: ");
            System.out.println(mainPath);
            System.out.println("Number of mines is: " + minesTotal);
            System.out.println("How Aggressive Tower Is (0 being hardest): " + towerAggressive);
            System.out.println("Accuracy Of Tower Is (0 being hardest):  " + towerAccuracy);


            Main main = new Main(mainPath + "brown.jpg");

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    timerCounter[0]++;
                    if (timerCounter[0] % towerAggressive == 0) {
                        main.towerFire(jFrame);
                        jFrame.repaint();

                    }

                }
            };

            timer.scheduleAtFixedRate(timerTask, 0, 1000);

            jFrame.setContentPane(main);
            jFrame.repaint();
            jFrame.setVisible(true);


            jFrame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {

                    int keyCode = e.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.VK_UP: {
                            if (main.checkCollide(-step) && !main.IsGameEnd && !main.tankFire) {
                                tank.Move(-step);
                                main.mineCollide();
                                jFrame.repaint();
                            }
                            break;
                        }
                        case KeyEvent.VK_DOWN: {
                            if (main.checkCollide(step) && !main.IsGameEnd && !main.tankFire) {
                                tank.Move(step);
                                main.mineCollide();
                                jFrame.repaint();
                            }
                            break;
                        }
                        case KeyEvent.VK_RIGHT: {
                            if (!main.IsGameEnd && !main.tankFire) {
                                tank.Rotate();
                                main.mineCollide();
                                jFrame.repaint();
                            }
                            break;
                        }
                        case KeyEvent.VK_LEFT: {
                            if (!main.IsGameEnd && !main.tankFire) {
                                tank.OppositeRotate();
                                main.mineCollide();
                                jFrame.repaint();
                            }
                            break;
                        }
                        case KeyEvent.VK_SPACE: {
                            if (!main.IsGameEnd) {
                                main.tankFire = true;
                                main.FireTank(jFrame);
                            }
                            break;
                        }
                        default:

                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });



        });
    }
}