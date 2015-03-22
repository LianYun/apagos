/**
 * @(#)ChessState.java
 *
 *
 * @author LianYun
 * @version 1.00 2011/10/13
 */
import java.util.List;

public class ChessState {
	/*���ݴ洢˵��
	 *========================
	 *���̱��
	 *���Ϊ0��Ӧ��������1
	 *���Ϊ1��Ӧ��������3
	 *���Ϊ2��Ӧ��������5
	 *���Ϊ3��Ӧ��������7
	 *=========================
	 *λ�ñ��
	 *λ��1,2,3,4�ֱ����Ӧ���0,1,2,3��Ӧ
	 */

	int []posOfBoard;		//��¼ÿ��λ���ϵ�����
	int []boardOfPos;		//��¼ÿ���������ڵ�λ��
	//�������ߵĹ�ϵ����1������λ��1.����
	//boardOfPos[0] = 0;		posOfBoard[boardIfPos[0]] = 0
	//����	posOfBoard[boardOfPos[i]] = i	(0<=i<=3)
	//		  boardOfPos[posOfBoard[i]] = i	  (0<=i<=3)
	//====================================================================
	int []pieceInBoardOfPlayer;		//��¼ÿ����������ҵģ������ӣ��ĸ���
	int []pieceInBoardOfComputer;	//��¼ÿ�������ϵ��Եģ������ӣ��ĸ���

    public ChessState() {
    	posOfBoard = new int[4];
    	boardOfPos = new int[4];
    	pieceInBoardOfPlayer   = new int[4];
    	pieceInBoardOfComputer = new int[4];
    	sign=0;
    	player=false;//�������
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
    	 *�Ѿ��õ�posOfBoard[]���飬���¶�Ӧ��boardOfPos[]���飬ʹ����ͳһ
    	 */
    	for(int i=0;i<4;i++)
    		boardOfPos[posOfBoard[i]] = i;
    }
    private void equalDateBoardToPos()
    {
    	/*
    	 *�Ѿ��õ�boardOfPos[]���飬���¶�Ӧ��posOfBoard[]���飬ʹ����ͳһ
    	 */
    	for(int i=0;i<4;i++)
    		posOfBoard[boardOfPos[i]] = i;
    }
    void exChangePosAndBoard(int pos)
    {
    	/*
    	 *��posλ���ϼ������Ӻ󣬵�������λ��
    	 *��ͬʱ�ı�posOfBoard[]��boardOfPos[]����
    	 */
    	int temps = posOfBoard[pos];
    	posOfBoard[pos] = posOfBoard[pos-1];
    	posOfBoard[pos-1] = temps;
    	equalDatePosToBoard();
    }
    public boolean movePieceXtoY(int posX,int posY,int playerOrCom)
    {
    	/*
    	 *�����Ӵ�posX�ƶ���posYλ��
    	 *����ɹ�����true�����򷵻�false
    	 */
    	if(playerOrCom == 0)		//���Ӷ�Ӧ�����ϵĶ�Ӧ������
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
    	 *��λ��pos�ϼ���һ����playerOrCom��ʶ������
    	 *pos��0-3
    	 *playerOrCom:0(����ڷ�����)��1������췽���������
    	 *����posλ�ü������ӣ��򷵻�true�����򷵻�false
    	 */
    	if(ifEmpty(pos))
    	{
    		int board = posOfBoard[pos];
    		if(playerOrCom == 0)		//���Ӷ�Ӧ�����ϵĶ�Ӧ������
    		{
    			pieceInBoardOfPlayer[board]++;
    		}
    		else if(playerOrCom == 1)
    		{
    			pieceInBoardOfComputer[board]++;
    		}
    		if(pos>0){					//��������Ӳ���λ��0������Ҫ����λ��
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
    		return 2;	//���ʤ��������2
    	}
    	else if(ifAllFill()&&(pieceInBoardOfPlayer[board]<pieceInBoardOfComputer[board]))
    	{
    		return 1;	//���ʧ�ܣ�����1
    	}
    	else
    		return 0;	//�������������0
    }
    public void printChess()
    {
    	System.out.println("==========================================");
		System.out.println(posOfBoard[0]+","+posOfBoard[1]+","+posOfBoard[2]+","+posOfBoard[3]);
		System.out.println(boardOfPos[0]+","+boardOfPos[1]+","+boardOfPos[2]+","+boardOfPos[3]);
		System.out.println(pieceInBoardOfPlayer[0]+","+pieceInBoardOfPlayer[1]+","+pieceInBoardOfPlayer[2]+","+pieceInBoardOfPlayer[3]);
		System.out.println(pieceInBoardOfComputer[0]+","+pieceInBoardOfComputer[1]+","+pieceInBoardOfComputer[2]+","+pieceInBoardOfComputer[3]);
    }

        //�ж�ÿ�������Ƿ�����
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