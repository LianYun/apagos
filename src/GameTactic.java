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

	//�������һ��λ�÷���
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

	//player=true����������ӣ�player=false�����������,���ؽ����䵽��n�����̺��״̬
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

	//����state������ֵ��������ֵ����������һ��ȷ��������ò�Ӧ�õ����ߣ���������ֵ������һ��������ֵ����
	//����������ֵ������һ������С��
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

	//��һ������player��,���ù�������㷨�������ӵ�״̬�����ݹ�����ȵ�˳��ÿ���ڵ�ѹ��ջ�в�����
	//���ص�ջ�������¶��Ͻ�������ֵ��ֵ
	Stack<ChessState> getTree(ChessState firstState,boolean player)
	{
		ChessState temp,temp1;
		Stack<ChessState> stack=new Stack<ChessState>();    //���ɷ������ݵ�ջ
		Queue<ChessState> queue=new LinkedList<ChessState>();   //���ɶ��У��������й�����ȱ���
		boolean play;
		temp=firstState;
		play=player;
		temp.child=new ArrayList<ChessState>();
		for(int i=0;i<4;i++)      //��firstState��������һ��״̬������к�ջ��
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

		while(!queue.isEmpty())      //��������״̬�����ù�������㷨������
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


	//�ú�������firstState����һ��״̬
	ChessState accuPath(ChessState firstState)
	{
		ChessState temp;
		Stack<ChessState> stack;
		Random random;
		if(firstState.numAll()<5)     //������������Ƚ��٣��ܷſɷ�7���ӵ������Ϸ���
			return nextComState(firstState, true, firstState.boardOfPos[3]);
		try{
		if(!firstState.ifAllFill())
		{
			stack=getTree(firstState, true);
			while(!stack.isEmpty())
			{
				temp=stack.pop();
				if(temp.isLeap())
				{       //Ҷ�ӽڵ�����ۺ����ǵ�һ�������ϵĵ�����-�����
					temp.sign=temp.pieceInBoardOfComputer[temp.posOfBoard[0]]
							-temp.pieceInBoardOfPlayer[temp.posOfBoard[0]];
				}
				else
				{  //Ϊÿ����Ҷ�ӽڵ��������ֵ
					temp.sign=playerJudge(temp);
				}
			}


			//��������ֵ����״̬���������״̬������ֵ��ͬ�����������һ��
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
