/**
 * @(#)Apagos.java
 *
 * Apagos application
 *
 * @author LianYun
 * @version 1.00 2011/10/10
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import java.io.*;

public class Apagos extends JFrame implements ActionListener,MouseListener,MouseMotionListener{

	private class AboutDialog extends JDialog {
 		JLabel about1 = new JLabel("排队棋(Apagos)，是法国人 Francis Pacherie 在 2010 年推出的两人棋类游戏。\n ");
		JLabel about2 = new JLabel("cloud ads");
		JLabel about3 = new JLabel("��ַ:http://zh.wikipedia.org/zh-cn/%E6%8E%92%E9%9A%8A%E6%A3%8B");
   		public AboutDialog(JFrame frame) {
    		this.setTitle("Alagos");
    		this.setSize(500, 100);
    		this.setLocation(250,250);
    		this.setLayout(new GridLayout(3,1));
    		about1.setHorizontalAlignment(SwingConstants.CENTER);
    		about2.setHorizontalAlignment(SwingConstants.CENTER);
    		about3.setHorizontalAlignment(SwingConstants.CENTER);
    		this.getContentPane().add(about1);
    		this.getContentPane().add(about2);
    		this.getContentPane().add(about3);
    	}
	}

	Container contentPane;
	JPanel controlPanel;
	MyCanvas imageCanvas;
	JPanel infoPanel;
	JPanel statePanel;
	JLabel stateLabel;
	JLabel infoLabel1;
	JLabel infoLabel2;
	JLabel mouseLabel;
	JProgressBar stateBar;
	JButton startGame;
	JButton restartGame;
	JButton helpGame;
	JButton exitGame;

	mGameEvent me;
	int width,height;
	int stateNum;
	int stepNum;

	public Apagos()
	{
		super();
		controlPanel = new JPanel(new FlowLayout());
		imageCanvas = new MyCanvas();
		imageCanvas.addMouseListener(this);
		imageCanvas.addMouseMotionListener(this);

		infoPanel = new JPanel();
		infoLabel1 = new JLabel("about");
		infoLabel2 = new JLabel("0");
		statePanel = new JPanel();
		stateLabel = new JLabel();
		mouseLabel = new JLabel();
		stateLabel.setText("welcome");
		stateBar = new JProgressBar();
		stateBar.setMaximum(16);
		startGame = new JButton("start game");
		restartGame = new JButton("restart game");
		helpGame = new JButton("help");
		exitGame = new JButton("exit game");
		restartGame.setEnabled(false);

		controlPanel.setLayout(new GridLayout(1,4));
		controlPanel.add(startGame);
		controlPanel.add(restartGame);
		controlPanel.add(helpGame);
		controlPanel.add(exitGame);

		infoPanel.setLayout(new GridLayout(4,1));
		infoPanel.add(infoLabel1);
		infoPanel.add(infoLabel2);
		infoPanel.setBackground(Color.gray);

		statePanel.setLayout(new GridLayout(1,3));
		statePanel.add(stateLabel);
		statePanel.add(stateBar);
		statePanel.add(mouseLabel);

		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(controlPanel,"North");
		contentPane.add(infoPanel,"East");
		contentPane.add(imageCanvas,"Center");
		contentPane.add(statePanel,"South");

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("alagos");
		setSize(500,400);
		setResizable(false);
		setLocation(200,200);
//===========================================
		int width = 455;
		int height = 325;
//===========================================
		me = new mGameEvent(imageCanvas,width,height);
		stateNum = 0;
		stepNum = 0;
//=========================================================
		startGame.addActionListener(this);
		restartGame.addActionListener(this);
		exitGame.addActionListener(this);
		helpGame.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
    {
    	if(e.getSource() == startGame)
    	{
    		stateNum = 1;
    		stepNum = 0;
    		me.setStateNum(stateNum);
    		stateLabel.setText("player step");
    		infoLabel2.setText("0");
    		stateBar.setValue(stepNum);
    		startGame.setEnabled(false);
    		restartGame.setEnabled(true);
    	}
    	else if(e.getSource() == restartGame)
    	{
    		stateNum = 0;
    		stepNum = 0;
    		me.restart();
    		me.setStateNum(stateNum);
    		stateLabel.setText("player step");
    		infoLabel2.setText("0");
    		stateBar.setValue(stepNum);
    		startGame.setEnabled(true);
    		restartGame.setEnabled(false);
    	}
    	else if(e.getSource() == helpGame)
    	{
    		System.out.println("---");
			AboutDialog newDialog = new AboutDialog(this);
			newDialog.show();
    	}
    	else if(e.getSource() == exitGame)
    	{
    		System.exit(0);
    	}
    }
    public void mouseClicked(MouseEvent e)
    {
    	if(stateNum!=0){
    		int x,y;
			x = e.getX();
			y = e.getY();
			stateLabel.setText("player step: ");
			if(me.UpdataCanvas(x,y))
			{
				stepNum++;
				infoLabel2.setText(""+stepNum);
				stateBar.setValue(stepNum*2);
			}
			stateLabel.setText("aa");
    	}
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e)
    {
    	mouseLabel.setText(" ");
    }
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e)
    {
    	int x,y;
		x = e.getX();
		y = e.getY();
		String s = " ";
		if(stateNum == 1){
			int position = me.detectionPosition(x,y)+1;
			if(position > 0 && position < 5)
			{
				s = "position: "+position;
			}
			else
			{
				s = "out of bound";
			}
		}
    	mouseLabel.setText("("+e.getX()+","+e.getY()+")"+s);
    }
    public static void main(String[] args) {
    	new Apagos();
    }
}