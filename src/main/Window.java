package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.math.BigInteger;

import javax.swing.JFrame;

import listening.KeyBoard;
import listening.Mouse;
import objects.Square;

@SuppressWarnings("serial")
public class Window extends JFrame {
	//-------------Fields--------------//
    /** Initial Size of the frame: X */
    final static int FRAME_X = 800;
    /** Initial Size of the frame: Y */
    final static int FRAME_Y = 800;
    /** Padding for window size: X
     * ElCapitan: 0, Windows7: 8  */   
    public final static int PADDING_X;
    /** Padding for window size: Y
     * ElCapitan: 23, Windows7: 30  */ 
    public final static int PADDING_Y;
    
    public static ArrayList<Square> squares = new ArrayList<Square>();
    //Gives Values for padding based on OS
    static {
    	if (System.getProperty("os.name").equals("Mac OS X")) {
    		PADDING_X = 0;
    		PADDING_Y = 23;
    	}
    	else if (System.getProperty("os.name").equals("Windows 7")) {
    		PADDING_X = 8;
    		PADDING_Y = 30;
    	}
    	else {
    		System.err.println("OS Not Supported Yet");
    		PADDING_X = 0;
    		PADDING_Y = 0;
    	}
    }
    
    //Double Buffering
    private Image photo;
    private Graphics dbg;
    
    int frameX;
    int frameY;
   
    Mouse m;
    boolean mousePressed;
    KeyBoard k;
    ArrayList<Boolean> keysPressed = new ArrayList<Boolean>();

    //----------Constructor--------//
    /** Makes Frame Makes objects */
    public Window() {
        super();
        
        //Set up how the frame works
        setSize(FRAME_X , FRAME_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        
        m = new Mouse();
        addMouseListener(m);
        
        k = new KeyBoard();
        addKeyListener(k);
        
        final int size = 700;
        for(double i = 0; i < 1 ; i += (1.0 / (double)size)){
        	for(double j = 0 ; j <= i ; j += (1.0 / (double)size)){
        		long bi = (long)(size * i + 0.01);
        		long bj = (long)(size * j + 0.01);
        		//System.out.println(Util.binomial(bi, bj));
        		if(Util.binomial(new BigInteger(((Long)bi).toString()),
        				new BigInteger(((Long)bj).toString()))
        				.mod(new BigInteger("2")).equals(BigInteger.ONE)) {
        			new Square(Vector.createFromRect(j+(1-i)/2, i), (1.0 /(double)size));
        		}
        	}
        }
        
    } 
   
    //-----Methods--------//
    /** Logic loop */
    public void run() {
    	while(true) {
            keysPressed = k.getKeysPressed();
    		mousePressed = m.getMousePressed();
    	}
    }
    
    public int getFrameX() {
    	return this.getContentPane().getWidth();
    }
    
    public int getFrameY() {
    	return this.getContentPane().getHeight();
    }
    
    /** Double buffering */
    @Override
    public void paint(Graphics g) {
    	int frameX = (int)(this.getContentPane().getWidth() + PADDING_X + 1);
        int frameY = (int)(this.getContentPane().getHeight() + PADDING_Y + 1);
        photo = createImage(frameX, frameY);
        dbg = photo.getGraphics();
        paintComponent(dbg);
        g.drawImage(photo, 0, 0, this);
    }
    
    /** paint loop
     * draws rectangles/people */
    public void paintComponent(Graphics g) {   
    	int i;
    	final int size = squares.size();
    	double hueConv = 360 / ((double) size);
    	for(i = 0; i < size ; i++) {
    		g.setColor(hueToColor(hueConv * i));
    		squares.get(i).draw(g, this);
    	}
        repaint();
    }
    
    public Color hueToColor(double hue) {
    	hue = hue % 360;
    	double x = 1 - Math.abs((hue / 60.0) % 2.0 - 1.0);
    	int tester = (int)(hue / 60);
    	float red = 0;
    	float green = 0;
    	float blue = 0;
    	switch (tester){
    	case 0:
    		red = 1;
        	green = (float) x;
        	blue = 0;
    		break;
    	case 1:
    		red = (float) x;
        	green = 1;
        	blue = 0;
    		break;
    	case 2:
    		red = 0;
        	green = 1;
        	blue = (float) x;
    		break;
    	case 3:
    		red = 0;
        	green = (float) x;
        	blue = 1;
    		break;
    	case 4:
    		red = (float) x;
        	green = 0;
        	blue = 1;
    		break;
    	case 5:
    		red = 1;
        	green = 0;
        	blue = (float) x;
    		break;
    	}
    	return new Color(red, green, blue);
    }
}