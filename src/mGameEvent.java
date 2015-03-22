/**
 * @(#)mGameEvent.java
 *
 *
 * @author LianYun
 * @version 1.00 2011/10/12
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class mGameEvent{
	MyCanvas c;						//��ͼ����
	int Width,Height;				//���ڵĴ�С
	int stateNum;					//��¼�����״̬�����������棬��Ҫ�л�ӭ���桢��Ϸ���С���Ϸ��������ʤ����ʧ�ܣ�
	ChessState rightNowState;		//��¼���������ϵ�״��
	ChessState oldState;			//��¼��һ����״̬
	int [][][]posiState;			//��¼�����ϻ�ͼ��״̬
	GameTactic tac;					//AI��
	int posX,posY;					//�϶�ʱ��λ��
	int ifMove;

    public mGameEvent(
    	MyCanvas c,
    	int width,int height
    	)
    {
    	this.c=c;
		Width = width;
		Height = height;
		stateNum =0;
		rightNowState = new ChessState();
		oldState      = new ChessState();
		tac=new GameTactic(rightNowState,true,0);
		ifMove = 0;

    	c.setMySize(width,height);
    	c.setState(stateNum);
    	posiState = new int[4][3][3];
   		init();
   		c.setPositionState(posiState);
    }
    private void init()
    {
    	//��ʼ������Ϊ����ʼ�������¿�ʼ��Ϸʱ����
    	for(int i = 0;i<4;i++)
   		{
   			for(int j =0;j<3;j++)
   			{
   				for(int k =0;k<3;k++)
   				{
   					posiState[i][j][k]=0;
   				}
   			}
   		}
    	posiState[0][1][1] = 1;
   		//======================
   		posiState[1][0][1] = 1;posiState[1][1][1]=1;posiState[1][2][1]=1;
   		//================================================================
   		posiState[2][0][1] = 1;posiState[2][1][1]=1;posiState[2][2][1]=1;
   		posiState[2][1][0] = 1;posiState[2][1][2]=1;
   		//================================================================
		posiState[3][0][0] = 1;posiState[3][1][0]=1;posiState[3][2][0]=1;
		posiState[3][0][2] = 1;posiState[3][1][2]=1;posiState[3][2][2]=1;
		posiState[3][1][1] = 1;
    }
    public void restart()
    {
    	init();
    	rightNowState.init();	//��������״̬
    }
    public void setStateNum(int n)
    {
    	/*
    	 *����״̬
    	 */
    	stateNum = n;
    	c.setState(stateNum);
    	c.repaint();
    }
    public boolean UpdataCanvas(int x,int y)
    {
    	/*
    	 *��������������״̬�����»���
    	 *�����ʵ���ʱ�󣬵��ü����AI�࣬��ü��������������״̬
    	 *����ֵ��������Ϊ��ʹ�����������ʾ��״̬�������гɹ��벻�ɹ����֣�
    	 */
		oldState.equalChessState(rightNowState);
    	if(updataChessStateOfPlayer(x,y))		//����������ĵ����������״̬
    	{
    		System.out.println("==========Player!=========");
    		rightNowState.printChess();
    		System.out.println("============================");
    		paintChessStateOnPosiState();		//���µ�����״̬�뻭ͼ�����Ӧ����
    		c.setPositionState(posiState);		//����ͼ��
    	}
    	else
    	{
    		return false;
    	}
    	try
		{
   			Thread.sleep(100);
		}
		catch(Exception e){}
		oldState.equalChessState(rightNowState);
		if(updataChessStateOfComputer())		//���ú�����ʵ�ּ������Ҷ�chessstate�ĸ���
		{
			paintChessStateOnPosiState();		//���µ�����״̬�뻭ͼ�����Ӧ����
    		c.setPositionState(posiState);		//����ͼ��
		}
		else
		{
			return false;
		}
		if(rightNowState.chessStateNow() == 2)
		{
			stateNum = 2;
			c.setState(stateNum);
			c.repaint();
		}
		if(rightNowState.chessStateNow() == 1)
		{
			stateNum = 3;
			c.setState(stateNum);
			c.repaint();
		}
    	return true;
    }
    private boolean updataChessStateOfPlayer(int x,int y)	//�˺����������������x��yλ�õ���󣬸���chessstate
    {
    	int pos = detectionPosition(x,y);
    	if(pos == 4) return false;
    	return rightNowState.addPieceInPos(pos,0);	//��posλ�ü�������
    }
    private void paintChessStateOnPosiState()
    {
    	/*
    	 *������״̬�뻭ͼ��¼PosiState��Ӧ����
    	 */
    	for(int i =0;i<4;i++)
    	{
    		if(rightNowState.pieceInBoardOfPlayer[i]>oldState.pieceInBoardOfPlayer[i])
    		{	//�����ҵĵ�λ
    			addPieceInPosiState(oldState.boardOfPos[i],0);
    			exchangePosiState(oldState.boardOfPos[i]);
    		}
    		if(rightNowState.pieceInBoardOfComputer[i]>oldState.pieceInBoardOfComputer[i])
    		{	//�����Եĵ�λ
    			addPieceInPosiState(oldState.boardOfPos[i],1);
    			exchangePosiState(oldState.boardOfPos[i]);
    		}
    	}
    }
    private boolean updataChessStateOfComputer()
    {
    	rightNowState = tac.accuPath(rightNowState);
    	System.out.println("==========Computer!=========");
    	rightNowState.printChess();
    	System.out.println("============================");
    	if(rightNowState == null)
    	{
    		return false;
    	}
		return true;
    }
    public int detectionPosition(int x,int y)
    {
    	//���������λ�ã��õ������λ��
    	if(x<(Width/2-5)&&y<(Height/2-5)&&x>5&&y>5)
    	{
    		return 0;
    	}
    	else if(x>(Width/2+5)&&y<(Height/2-5)&&x<(Width-5)&&y>5)
    	{
			return 1;
    	}
    	else if(x<(Width/2-5)&&y>(Height/2+5)&&x>5&&y<(Height-5))
    	{
    		return 2;
    	}
    	else if(x>(Width/2+5)&&y>(Height/2+5)&&x<(Width-5)&&y<(Height-5))
    	{
    		return 3;
    	}
    	else
    	{
    		return 4;
    	}
    }
    private void addPieceInPosiState(int pos,int playerOrCom)
    {
		//��posλ�ü���һ�����ӣ�����PosiState����������˳��ĶԵ���
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(posiState[pos][i][j] == 1)
				{
					posiState[pos][i][j] = playerOrCom+2;
					return;
				}
			}
		}
    }
    private void exchangePosiState(int pos)
    {
    	//��posλ�ü���һ�����ӣ�������Ӧ���̵�λ��
    	if(pos >0 )
    	{
    		//������ͼ���ݵ�λ��
    		int [][]temp = new int[3][3];
    		for(int i=0;i<3;i++)
    		{
    			for(int j =0;j<3;j++)
    			{
    				temp[i][j] = posiState[pos][i][j];
    				posiState[pos][i][j]   = posiState[pos-1][i][j];
    				posiState[pos-1][i][j] = temp[i][j];
    			}
    		}
    	}
    }
}