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
	MyCanvas c;						//画图界面
	int Width,Height;				//窗口的大小
	int stateNum;					//记录程序的状态，来自主界面，主要有欢迎界面、游戏运行、游戏结束（分胜利和失败）
	ChessState rightNowState;		//记录现在棋盘上的状况
	ChessState oldState;			//记录上一步的状态
	int [][][]posiState;			//记录棋盘上画图的状态
	GameTactic tac;					//AI类
	int posX,posY;					//拖动时的位置
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
    	//初始化，作为程序开始或者重新开始游戏时调用
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
    	rightNowState.init();	//重置棋盘状态
    }
    public void setStateNum(int n)
    {
    	/*
    	 *设置状态
    	 */
    	stateNum = n;
    	c.setState(stateNum);
    	c.repaint();
    }
    public boolean UpdataCanvas(int x,int y)
    {
    	/*
    	 *鼠标点击后更新棋盘状态、更新画面
    	 *并在适当延时后，调用计算机AI类，获得计算机走棋后的棋盘状态
    	 *返回值的设置是为了使主界面更新显示的状态（走棋有成功与不成功两种）
    	 */
		oldState.equalChessState(rightNowState);
    	if(updataChessStateOfPlayer(x,y))		//根据玩家鼠标的点击更新棋盘状态
    	{
    		System.out.println("==========Player!=========");
    		rightNowState.printChess();
    		System.out.println("============================");
    		paintChessStateOnPosiState();		//将新的棋盘状态与画图数组对应起来
    		c.setPositionState(posiState);		//画出图像
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
		if(updataChessStateOfComputer())		//调用函数，实现计算机玩家对chessstate的更新
		{
			paintChessStateOnPosiState();		//将新的棋盘状态与画图数组对应起来
    		c.setPositionState(posiState);		//画出图像
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
    private boolean updataChessStateOfPlayer(int x,int y)	//此函数的作用是鼠标在x，y位置点击后，更新chessstate
    {
    	int pos = detectionPosition(x,y);
    	if(pos == 4) return false;
    	return rightNowState.addPieceInPos(pos,0);	//在pos位置加入棋子
    }
    private void paintChessStateOnPosiState()
    {
    	/*
    	 *将棋盘状态与画图记录PosiState对应起来
    	 */
    	for(int i =0;i<4;i++)
    	{
    		if(rightNowState.pieceInBoardOfPlayer[i]>oldState.pieceInBoardOfPlayer[i])
    		{	//检测玩家的单位
    			addPieceInPosiState(oldState.boardOfPos[i],0);
    			exchangePosiState(oldState.boardOfPos[i]);
    		}
    		if(rightNowState.pieceInBoardOfComputer[i]>oldState.pieceInBoardOfComputer[i])
    		{	//检测电脑的单位
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
    	//根据鼠标点击位置，得到点击的位置
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
		//在pos位置加入一颗棋子，更新PosiState（不做棋盘顺序的对调）
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
    	//在pos位置加入一颗棋子，调换对应棋盘的位置
    	if(pos >0 )
    	{
    		//交换画图数据的位置
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