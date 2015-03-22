/**
 * @(#)ChessState.java
 *
 *
 * @author LianYun
 * @version 1.00 2011/10/13
 */
import java.util.List;

public class ChessState {
	/*数据存储说明
	 *========================
	 *棋盘编号
	 *编号为0对应棋子数：1
	 *编号为1对应棋子数：3
	 *编号为2对应棋子数：5
	 *编号为3对应棋子数：7
	 *=========================
	 *位置编号
	 *位置1,2,3,4分别与对应编号0,1,2,3对应
	 */

	int []posOfBoard;		//记录每个位置上的棋盘
	int []boardOfPos;		//记录每个棋盘所在的位置
	//以上两者的关系：如1棋盘在位置1.则有
	//boardOfPos[0] = 0;		posOfBoard[boardIfPos[0]] = 0
	//即：	posOfBoard[boardOfPos[i]] = i	(0<=i<=3)
	//		  boardOfPos[posOfBoard[i]] = i	  (0<=i<=3)
	//====================================================================
	int []pieceInBoardOfPlayer;		//记录每个棋盘上玩家的（即黑子）的个数
	int []pieceInBoardOfComputer;	//记录每个棋盘上电脑的（即红子）的个数

    public ChessState() {
    	posOfBoard = new int[4];
    	boardOfPos = new int[4];
    	pieceInBoardOfPlayer   = new int[4];
    	pieceInBoardOfComputer = new int[4];
    	sign=0;
    	player=false;//玩家先走
    	floor=0;
    	parent=null;
    	child=null;
    	init();
    }
    public void init()
    {
    	for(int i=0;i<4;i++)
    		pieceInBoardOfPlayer[i] = 0;
    	for(int i=0;i<4;i++)
    		pieceInBoardOfComputer[i] = 0;
    	for(int i=0;i<4;i++)
    		posOfBoard[i] = i;
    	for(int i=0;i<4;i++)
    		boardOfPos[i] = i;
    }
	public void equalChessState(ChessState cs)
	{
		for(int i=0;i<4;i++)
    	{
    		this.posOfBoard[i] = cs.posOfBoard[i];
			this.boardOfPos[i] = cs.boardOfPos[i];
    		this.pieceInBoardOfPlayer[i]   = cs.pieceInBoardOfPlayer[i];
    		this.pieceInBoardOfComputer[i] = cs.pieceInBoardOfComputer[i];
    	}
	}
    private void equalDatePosToBoard()
    {
    	/*
    	 *已经得到posOfBoard[]数组，更新对应的boardOfPos[]数组，使两者统一
    	 */
    	for(int i=0;i<4;i++)
    		boardOfPos[posOfBoard[i]] = i;
    }
    private void equalDateBoardToPos()
    {
    	/*
    	 *已经得到boardOfPos[]数组，更新对应的posOfBoard[]数组，使两者统一
    	 */
    	for(int i=0;i<4;i++)
    		posOfBoard[boardOfPos[i]] = i;
    }
    void exChangePosAndBoard(int pos)
    {
    	/*
    	 *在pos位置上加入棋子后，调换棋盘位置
    	 *即同时改变posOfBoard[]和boardOfPos[]数组
    	 */
    	int temps = posOfBoard[pos];
    	posOfBoard[pos] = posOfBoard[pos-1];
    	posOfBoard[pos-1] = temps;
    	equalDatePosToBoard();
    }
    public boolean movePieceXtoY(int posX,int posY,int playerOrCom)
    {
    	/*
    	 *将棋子从posX移动到posY位置
    	 *如果成功返回true，否则返回false
    	 */
    	if(playerOrCom == 0)		//增加对应棋盘上的对应方棋子
    	{
    		if(pieceInBoardOfPlayer[posX]>0 && ifEmpty(posY)){
    			pieceInBoardOfPlayer[posY]++;
    			pieceInBoardOfPlayer[posX]--;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	else
    	{
    		if(pieceInBoardOfComputer[posX]>0&&ifEmpty(posY)){
    			pieceInBoardOfComputer[posY]++;
    			pieceInBoardOfComputer[posX]--;
    		}
    		else
    		{
    			return false;
    		}
   		}
   		return true;
    }
    public boolean addPieceInPos(int pos,int playerOrCom)
    {
    	/*
    	 *在位置pos上加入一颗由playerOrCom标识的棋子
    	 *pos：0-3
    	 *playerOrCom:0(代表黑方、人)，1（代表红方、计算机）
    	 *能在pos位置加入棋子，则返回true，否则返回false
    	 */
    	if(ifEmpty(pos))
    	{
    		int board = posOfBoard[pos];
    		if(playerOrCom == 0)		//增加对应棋盘上的对应方棋子
    		{
    			pieceInBoardOfPlayer[board]++;
    		}
    		else if(playerOrCom == 1)
    		{
    			pieceInBoardOfComputer[board]++;
    		}
    		if(pos>0){					//加入的棋子不在位置0，则需要调换位置
    			exChangePosAndBoard(pos);
    		}
    		return true;
    	}
    	else
    		return false;
    }
    private boolean ifEmpty(int pos)
    {
    	int board = posOfBoard[pos];
    	if(board == 0&&(pieceInBoardOfPlayer[0]+pieceInBoardOfComputer[0])>=1)
    	{
    		return false;
    	}
    	if(board == 1&&(pieceInBoardOfPlayer[1]+pieceInBoardOfComputer[1])>=3)
    	{
    		return false;
    	}
    	if(board == 2&&(pieceInBoardOfPlayer[2]+pieceInBoardOfComputer[2])>=5)
    	{
    		return false;
    	}
    	if(board == 3&&(pieceInBoardOfPlayer[3]+pieceInBoardOfComputer[3])>=7)
    	{
    		return false;
    	}
    	return true;
    }
    boolean ifAllFill()
    {
    	if( (pieceInBoardOfPlayer[0]+pieceInBoardOfComputer[0])==1&&
    		(pieceInBoardOfPlayer[1]+pieceInBoardOfComputer[1])==3&&
    		(pieceInBoardOfPlayer[2]+pieceInBoardOfComputer[2])==5&&
    		(pieceInBoardOfPlayer[3]+pieceInBoardOfComputer[3])==7)
    		{
    			return true;
    		}
    	else
    		return false;
    }
    public int chessStateNow()
    {
    	int board = posOfBoard[0];
    	if(ifAllFill()&&(pieceInBoardOfPlayer[board]>pieceInBoardOfComputer[board]))
    	{
    		return 2;	//玩家胜利，返回2
    	}
    	else if(ifAllFill()&&(pieceInBoardOfPlayer[board]<pieceInBoardOfComputer[board]))
    	{
    		return 1;	//玩家失败，返回1
    	}
    	else
    		return 0;	//其他情况，返回0
    }
    public void printChess()
    {
    	System.out.println("==========================================");
		System.out.println(posOfBoard[0]+","+posOfBoard[1]+","+posOfBoard[2]+","+posOfBoard[3]);
		System.out.println(boardOfPos[0]+","+boardOfPos[1]+","+boardOfPos[2]+","+boardOfPos[3]);
		System.out.println(pieceInBoardOfPlayer[0]+","+pieceInBoardOfPlayer[1]+","+pieceInBoardOfPlayer[2]+","+pieceInBoardOfPlayer[3]);
		System.out.println(pieceInBoardOfComputer[0]+","+pieceInBoardOfComputer[1]+","+pieceInBoardOfComputer[2]+","+pieceInBoardOfComputer[3]);
    }

        //判断每块棋盘是否已满
    boolean isPieceFull(int n)
    {
    	int i=this.posOfBoard[n];
    	if(pieceInBoardOfPlayer[i]+pieceInBoardOfComputer[i]==2*i+1)
    		return true;
    	else
    		return false;
    }

    boolean isComputerNone(int n)
    {
    	int i=this.posOfBoard[n];
    	if(this.pieceInBoardOfComputer[i]==0)
    		return true;
    	else
    		return false;
    }

    boolean isPlayerNone(int n)
    {
    	int i=this.posOfBoard[n];
    	if(this.pieceInBoardOfPlayer[i]==0)
    		return true;
    	else
    		return false;
    }

    boolean isLeap()
    {
    	if(child==null)
    		return true;
    	else
    		return false;

    }
    int numAll()
    {
    	int num=0;
    	for(int i=0;i<4;i++)
    	{
    		num=num+this.pieceInBoardOfComputer[i];
    		num=num+this.pieceInBoardOfPlayer[i];
    	}

    	return num;
    }
    ChessState parent;
	List<ChessState> child;
	boolean player;
	int sign;
	int floor;
}