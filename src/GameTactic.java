import java.util.*;
public class GameTactic {

	ChessState nextststate;

	public GameTactic(ChessState firstState){
			nextststate=accuPath(firstState);
	}

	public GameTactic(ChessState firstState,boolean player,int n){
		if(player)
			nextststate=accuPath(firstState);
		else
			nextststate=nextComState(firstState,player,n);
	}

	//随机查找一个位置放子
	ChessState randomFind(ChessState firstState)
	{
		Random random=new Random();
		int r=random.nextInt(4);
		ChessState state;
		state=nextComState(firstState, true, r);
		while(state==null){
			r=random.nextInt(4);
			state=nextComState(firstState, true, r);
		}

		return state;
	}

	//player=true代表电脑落子，player=false代表玩家落子,返回将子落到第n个棋盘后的状态
	ChessState nextComState(ChessState firststate,boolean player,int n)
    {
		if(firststate.isPieceFull(n))
			return null;
    	ChessState newState;
    	newState=new ChessState();
    	for(int i=0;i<4;i++){
    	newState.boardOfPos[i]=firststate.boardOfPos[i];
    	newState.pieceInBoardOfComputer[i]=firststate.pieceInBoardOfComputer[i];
    	newState.pieceInBoardOfPlayer[i]=firststate.pieceInBoardOfPlayer[i];
    	newState.posOfBoard[i]=firststate.posOfBoard[i];
    	}

    	if(!newState.isPieceFull(n))
		{
			if(player)
				newState.pieceInBoardOfComputer[newState.posOfBoard[n]]
					=newState.pieceInBoardOfComputer[newState.posOfBoard[n]]+1;
			else
				newState.pieceInBoardOfPlayer[newState.posOfBoard[n]]
    					=newState.pieceInBoardOfPlayer[newState.posOfBoard[n]]+1;
		}
		else
			return null;

    	if(n!=0)
    		newState.exChangePosAndBoard(n);

		return newState;
    }

	//返回state的评价值，其评价值根据他的下一步确定，如果该步应该电脑走，则其评价值等于下一步中评价值最大的
	//否则其评价值等于下一步中最小的
	int playerJudge(ChessState state)
	{
		int sign;
		sign=state.child.get(0).sign;
		for(int i=0;i<state.child.size();i++)
		{
			if(state.player)
			{
				if(sign<state.child.get(i).sign)
					sign=state.child.get(i).sign;
			}
			else
			{
				if(sign>state.child.get(i).sign)
					sign=state.child.get(i).sign;
			}
		}

		return sign;
	}

	//下一个子由player走,采用广度优先算法遍历棋子的状态，根据广度优先的顺序将每个节点压入栈中并返回
	//返回的栈用来自下而上进行评价值赋值
	Stack<ChessState> getTree(ChessState firstState,boolean player)
	{
		ChessState temp,temp1;
		Stack<ChessState> stack=new Stack<ChessState>();    //生成返回数据的栈
		Queue<ChessState> queue=new LinkedList<ChessState>();   //生成队列，用来进行广度优先遍历
		boolean play;
		temp=firstState;
		play=player;
		temp.child=new ArrayList<ChessState>();
		for(int i=0;i<4;i++)      //将firstState的所有下一步状态放入队列和栈中
		{
			temp1=nextComState(temp, play, temp.boardOfPos[i]);
			if(temp1!=null)
			{
				temp1.parent=temp;
				temp.child.add(temp1);
				temp1.player=play;
				stack.add(temp1);
				queue.add(temp1);
			}
		}

		while(!queue.isEmpty())      //遍历所有状态，采用广度优先算法生成树
		{
			temp=queue.poll();
			if(!temp.ifAllFill()){
				temp.child=new ArrayList<ChessState>();
				for(int i=0;i<4;i++)
				{
					temp1=nextComState(temp, !temp.player, temp.boardOfPos[i]);
					if(temp1!=null)
					{
						temp1.parent=temp;
						temp.child.add(temp1);
						temp1.player=!temp.player;
						stack.add(temp1);
						queue.add(temp1);
					}
				}

				if(temp.child.size()==0)
					temp.child=null;
			}
		}

		return stack;
	}


	//该函数返回firstState的下一步状态
	ChessState accuPath(ChessState firstState)
	{
		ChessState temp;
		Stack<ChessState> stack;
		Random random;
		if(firstState.numAll()<5)     //如果总棋子数比较少，总放可放7个子的棋盘上放子
			return nextComState(firstState, true, firstState.boardOfPos[3]);
		try{
		if(!firstState.ifAllFill())
		{
			stack=getTree(firstState, true);
			while(!stack.isEmpty())
			{
				temp=stack.pop();
				if(temp.isLeap())
				{       //叶子节点的评价函数是第一个棋盘上的点脑子-玩家子
					temp.sign=temp.pieceInBoardOfComputer[temp.posOfBoard[0]]
							-temp.pieceInBoardOfPlayer[temp.posOfBoard[0]];
				}
				else
				{  //为每个非叶子节点计算评价值
					temp.sign=playerJudge(temp);
				}
			}


			//返回评价值最大的状态，如果几个状态的评价值相同，则随机返回一个
			int s=0,sign;
			sign=firstState.child.get(0).sign;
			List<Integer> list=new ArrayList<Integer>();
			for(int i=0;i<firstState.child.size();i++)
			{
				if(sign<firstState.child.get(i).sign)
				{
					list.clear();
					sign=firstState.child.get(i).sign;
					list.add(new Integer(i));
				}
				else
				{
					if(sign==firstState.child.get(i).sign)
						list.add(new Integer(i));
				}
			}

			if(list.size()>1)
			{
				random=new Random();
				int ran=random.nextInt(list.size()-1);
				s=list.get(ran).intValue();
			}

			return firstState.child.get(s);
		}
		else
			return null;
		}
		catch(OutOfMemoryError e)
		{
			return randomFind(firstState);
		}
	}
}
