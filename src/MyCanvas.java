/**
 * @(#)MyCanvas.java
 *
 *
 * @author LianYun
 * @version 1.00 2011/10/12
 */

import java.awt.*;
import java.net.URL;

class MyCanvas extends Canvas
{
	private class Move extends Thread{

		private boolean isContinue = false;
		private int xoy = 0;
		public void setXOrY(int n)
        {
			xoy = n;
		}
		public void startRun()
		{
			isContinue = true;
		}
		public void terminate()
		{
			isContinue = false;
		}
		public void run()
		{
			while(true)
			{
				try{
					if(isContinue){
						if(xoy == 0)
						{
							if(sX<width/2-10)
							{
								sX = sX + 1;
							}
							else
							{
								sX = 20;
							}
						}
						else if(xoy == 1)
						{
							if(sY<height-30)
							{
								sY = sY + 1;
							}
							else
							{
								sY = 60;
							}
						}
						else
						{
							if(nums<16)
							{
								nums++;
							}
							else
							{
								nums = 0;
							}
						}
						repaint();
						Thread.sleep(60);
					}
				}catch(Exception e)
				{
					break;
				}
			}
		}
	}
	private int 	height,width;
	private int 	state;
	private Image    offI;
	private Graphics offG;
	private Image 	 backGround1;
	private Image 	 backGround2;
	private Image 	 backGround3;
	private Image 	 []starts;
	//���������û��ʵĴ�С
    private final static BasicStroke stokeLine1 = new BasicStroke(6.0f);
    private final static BasicStroke stokeLine2 = new BasicStroke(3.0f);
    private final static int R = 20;
    private int [][][]positionX;
    private int [][][]positionY;
    private int [][][]posiState;
	private int sX,sY;
	int x,y;
	private int nums;
	private Move moves;
   	MyCanvas()
   	{
   	  	setBackground(Color.WHITE);
   	  	positionX = new int[4][3][3];
   	  	positionY = new int[4][3][3];
   	  	posiState = new int[4][3][3];
   	  	sX = 20;
   	  	sY = 60;
   	  	nums =0;
   	  	moves = new Move();
   	  	moves.start();
   	  	starts = new Image[17];
   	  	//���ر���ͼ��
   	  	Toolkit tookkit = Toolkit.getDefaultToolkit();
   	  	URL url = this.getClass().getResource("backgroud.jpg");
        backGround1 = tookkit.getImage(url);
        url = this.getClass().getResource("screen.jpg");
        backGround2 = tookkit.getImage(url);
        url = this.getClass().getResource("endbackgroud.jpg");
        backGround3 = tookkit.getImage(url);
        for(int i = 0;i<17;i++)
        {
        	int k =i+1;
        	url = this.getClass().getResource("T"+k+".gif");
        	starts[i] = tookkit.getImage(url);
        }
   	}
   	private  void init()
   	{
   		if(offI == null){
   			offI = this.createImage(width,height);
   			offG = offI.getGraphics();
   		}
   	}
   	void setState(int n)
   	{
   		state = n;
   		if(state == 0)
   		{
   			moves.setXOrY(2);
   			moves.startRun();
   		}
   		else if(state == 2)
   		{
   			moves.setXOrY(0);
   			moves.startRun();
   		}
   		else if(state == 3)
   		{
   			moves.setXOrY(1);
   			moves.startRun();
   		}
   		else
   		{
   			moves.terminate();
   		}
   	}
   	void setMySize(int w,int h)
   	{
   		height = h;
   		width  = w;
   		int tempx = 20;
   	  	int tempy = 15;
   	  	int wix = (width/2-20)/3;
   	  	int hiy = (height/2 - 20)/3;
   	  	int x = tempx,y = tempy;
   	  	for(int i =0;i<4;)
   	  	{
   	  		for(int j=0;j<3;j++)
   	  		{
   	  			int temp = y;
   	  			for(int k=0;k<3;k++)
   	  			{
   	  				positionX[i][j][k]=x;
   	  				positionY[i][j][k]=y;
   	  				y = y + hiy;
   	  			}
   	  			x = x+wix;
   	  			y = temp;
   	  		}
   	  		i++;
   	  		if(i ==1)
   	  		{
   	  			x = tempx + width/2;
   	  			y = tempy;
   	  		}
   	  		else if(i == 2)
   	  		{
   	  			x = tempx;
   	  			y = tempy + height/2;
   	  		}
   	  		else if(i == 3)
   	  		{
   	  			x = tempx + width/2;
   	  			y = tempy + height/2;
   	  		}
   	  	}
   	}
   	void setPositionState(int [][][]n)
   	{
   		//����λ�õĻ�ͼ״̬
   		posiState = n;
   		repaint();
   	}
   	public void paint(Graphics g)
   	{
   		init();
   		switch (state){
   			case 0:
   				welcomeScrren();
   				break;
   			case 1:
   				UpdataMyGameCanvas();
   				break;
   			case 2:
   				winScrren();
   				break;
   			case 3:
   				failScreen();
   				break;
   		}
   		g.drawImage(offI,0,0,this);
   	}
   	public void update(Graphics g){
   		paint(g);
   	}
   	private void initStartPaint()
    {
   		offG.drawImage(backGround2,0,0,width,height,this);
		offG.setColor(Color.GREEN);
		Graphics2D g2d = (Graphics2D)offG;
		g2d.setStroke(stokeLine1);
		offG.drawLine(0,height/2,width,height/2);
		offG.drawLine(width/2,0,width/2,height);
		offG.drawRect(0,0,width,height);
		g2d.setStroke(stokeLine2);
		offG.setColor(Color.BLACK);
		offG.drawLine(0,height/2,width,height/2);
		offG.drawLine(width/2,0,width/2,height);
		offG.drawRect(0,0,width,height);
   	}
   	private void UpdataMyGameCanvas()
   	{
   		initStartPaint();
   		for(int i = 0;i<4;i++)
   		{
   			for(int j =0;j<3;j++)
   			{
   				for(int k =0;k<3;k++)
   				{
   					drawSinglePiece(positionX[i][j][k],positionY[i][j][k],posiState[i][j][k]);
   				}
   			}
   		}
   	}
   	private void drawSinglePiece(int x,int y,int cho)
   	{
   		if(cho == 0)
   		{
   		}
   		else if(cho == 1)
   		{
   			offG.setColor(Color.BLACK);
   			offG.drawOval(x,y,2*R,2*R);
   		}
   		else if(cho == 2)
   		{
   			offG.setColor(Color.BLACK);
   			offG.fillOval(x,y,2*R,2*R);
   		}
   		else if(cho == 3)
   		{
   			offG.setColor(Color.RED);
   			offG.fillOval(x,y,2*R,2*R);
   		}
   	}
   	private void welcomeScrren()
   	{
   		offG.drawImage(backGround1,0,0,width,height,this);
   		offG.drawImage(starts[nums],width/2-65,0,130,80,this);
   		offG.setFont(new Font("Courier",Font.BOLD,40));
   		FontMetrics FM = offG.getFontMetrics();

        String txt = "welcome alagos";
   		int As = FM.getAscent();
   		int Ds = FM.getDescent();
   		int w = FM.stringWidth(txt);
   		int X = (width - w)/2;
   		int Y = (height - (As+Ds))/2+As;
   		offG.setColor(Color.lightGray);
   		offG.drawString(txt, X, Y);
   		offG.setColor(Color.green);
   		offG.drawString(txt, X-2, Y-2);
   		offG.setColor(Color.RED);
   		Graphics2D g2d = (Graphics2D)offG;
		g2d.setStroke(stokeLine1);
   		offG.drawRect(X-10, Y-As, w+20, As+Ds);

   		offG.setFont(new Font("Courier1",Font.PLAIN,25));
   		offG.setColor(Color.PINK);
   		offG.drawString("Designed By Cloud|ADS",X+50,Y+As+Ds);
   	}
   	private void winScrren()
   	{
   		/*
   		 *���ʤ������ף����
   		 */
   		offG.drawImage(backGround3,0,0,width,height,this);
		offG.setFont(new Font("Courier",Font.BOLD,50));
   		FontMetrics FM = offG.getFontMetrics();

   		int As = FM.getAscent();
   		int Ds = FM.getDescent();
   		int w = FM.stringWidth("you win");
   		int X = sX;
   		int Y = (height - (As+Ds))/2+As;
   		offG.setColor(Color.RED);
   		offG.fillRect(X-20,Y-As-10,w+40,As+Ds+20);
   		offG.setColor(Color.lightGray);
   		offG.drawString("you win",X,Y);
   		offG.setColor(Color.black);
   		offG.drawString("you win",X-2,Y-2);
   		offG.setColor(Color.BLACK);
   		offG.drawRect(X-10,Y-As,w+20,As+Ds);
   	}
   	private void failScreen()
   	{
   		/*
   		 *���ʧ�ܺ�Ľ���
   		 */
   		offG.drawImage(backGround3,0,0,width,height,this);
		offG.setFont(new Font("Courier",Font.BOLD,50));
   		FontMetrics FM = offG.getFontMetrics();

   		int As = FM.getAscent();
   		int Ds = FM.getDescent();
   		int w = FM.stringWidth("you lose~");
   		int X = (width - w)/2;
   		int Y = sY;
   		offG.setColor(Color.BLACK);
   		offG.fillRect(X-20,Y-As-10,w+40,As+Ds+20);
   		offG.setColor(Color.lightGray);
   		offG.drawString("you lose~",X,Y);
   		offG.setColor(Color.RED);
   		offG.drawString("you lose~",X-2,Y-2);
   		offG.setColor(Color.RED);
   		offG.drawRect(X-10,Y-As,w+20,As+Ds);
   	}
}