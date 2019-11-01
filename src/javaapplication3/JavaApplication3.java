package javaapplication3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class JavaApplication3 {

    public static void main(String[] args) {
        // TODO code application logic here
        double[][] data = new double[3][101];
        //double[] data_rand = new double[101];
        for (int i=0;i<101;i++){
            data[0][i]=Math.cos(0.01*i)+Math.sin(0.1*i);
            data[1][i]=data[0][i]+Math.random()-0.5;
        }
        double[] xn = new double[11];
        double[] yn = new double[11];
        for (int i=0;i<11;i++) {xn[i]=i;yn[i]=i;}
        data[2][0]=data[1][0];
        for (int i=1;i<101;i++)
            data[2][i]=data[2][i-1]+(data[1][i]-data[2][i-1])/(i+1);
        Draw myDraw1 = new Draw(xn,data,"The updated average method",500,400,"%.1f",10,10);
        data[0][0]=data[1][0];
        data[2][0]=data[0][0];
        for (int i=1;i<101;i++){
            data[0][i]=data[1][i];
            data[1][i]=0.25*data[0][i]+0.75*data[1][i-1];
            data[2][i]=0.75*data[0][i]+0.25*data[2][i-1];
        }
        Draw myDraw2 = new Draw(xn,data,"Exponential smoothing method",500,400,"%.1f",10,460);
        
    }
    
}

class Draw extends javax.swing.JFrame {
 
    private double[] xn;
    private double y_min,y_max;
    private double[][] y;
    private int[][] yInt;
    //private int colwidth;
    private Dimension size;
    private Dimension startPointXoY;
    private double scale;
    String title;
    String form;
    int lx,ly;
    private static final Color[] colors = {Color.RED,Color.GREEN,Color.BLUE,Color.MAGENTA,Color.ORANGE,Color.PINK};
 
    public Draw(double[] xn, double[][] y, String title,int width, int height,String form, int lx, int ly) {
        this.xn = xn;
        this.y = y;
        this.scale=scale;
        this.title=title;
        this.form=form;
        this.lx=lx;
        this.ly=ly;
        y_min=y[0][0];
        y_max=y[0][0];
        for (int i=0;i<y.length;i++)
            for (int j=0;j<y[i].length;j++){
            	if (y[i][j]>y_max) y_max=y[i][j];
                if (y[i][j]<y_min) y_min=y[i][j];
            }
        size=new Dimension(width,height);
        startPointXoY=new Dimension(30,size.height-30);
        this.scale=((double)y_max-y_min)/(size.height-60);
        yInt = new int[y.length][y[0].length];
        reBuildArreys();
        initInterface();
    }
 
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,size.width,size.height);
 
        g.setColor(Color.BLACK);
        g.drawLine(startPointXoY.width, startPointXoY.height, startPointXoY.width, 30);
        g.drawLine(startPointXoY.width, startPointXoY.height, size.width-30, startPointXoY.height);
 
        for (int i = 0; i < xn.length; i++){
            g.drawLine(startPointXoY.width + (int)Math.round((double)(size.width-60)/(xn.length-1)*i),startPointXoY.height,startPointXoY.width + (int)Math.round((double)(size.width-60)/(xn.length-1)*i),startPointXoY.height - 5);
            g.drawString(String.format(form, xn[i]),startPointXoY.width - 10 + (int)Math.round((double)(size.width-60)/(xn.length-1)*i),startPointXoY.height + 15);
        }
        for (int i = 0; i < 11; i++)
            g.drawString(String.format(form, (y_min+(y_max-y_min)/10*i)),startPointXoY.width -28,startPointXoY.height - (int)Math.round((double)size.height/11*i));
        for (int i=0;i<y.length;i++){
            g.setColor(colors[i]);
            double x_scale = ((double)size.width-60)/y[i].length;
            for (int j=0;j<y[i].length-1;j++)
                g.drawLine(startPointXoY.width+(int)Math.round(j*x_scale), yInt[i][j], startPointXoY.width+(int)Math.round((j+1)*x_scale), yInt[i][j+1]);
        }
    }
 
    private void initInterface() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(size);
        setResizable(false);
        setTitle(title);
        setLocation(lx,ly);
        setVisible(true);
    }
 
    private void reBuildArreys() {
        for (int i = 0; i < y.length; i++)
        for (int j = 0; j < y[0].length;j++){
            yInt[i][j] = (int)Math.round((y[i][j]-y_min)/scale);
            yInt[i][j] = startPointXoY.height - (yInt[i][j]);
        }
    }
}
