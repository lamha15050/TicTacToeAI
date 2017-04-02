/**
 * 
 * @author Lamha Goel 2015050
 * 
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;

public class Game implements ActionListener{
	//GUI and cases for playing
	//Inner class grid which handles the main grid
	private JFrame frame;
	private Grid grid;
	private JPanel buttonsGrid;
	private JLabel outputBox;
	private JButton gameMode = null;
	private boolean userturn=false;
	private int cur;
	private Timer timer;
	private static final Color buttonColor = new Color(40,200,200);
	private String[] usernames=new String[2];
	private final char[] mark={'X','O'};
	public Game()
	{
		frame=new JFrame();
		//TODO set location of frame?
		//frame.set
		frame.setTitle("Tic Tac Toe");
		frame.setSize(300, 300);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen();
	}
	
	//Inner class for Grid
	public class Grid{
		char[][] board = new char[3][3];
		public Grid()
		{
			/*int i,j;
			JButton temp;
			for(i=0;i<3;i++)
			{
				for(j=0;j<3;j++)
				{
					board[i][j]=' ';
					temp = (JButton)buttonsGrid.getComponent(i*3+j);
					temp.setText("");
				}
			}*/
			this.reset();
		}
		public void reset()
		{
			int i,j;
			JButton temp;
			for(i=0;i<3;i++)
			{
				for(j=0;j<3;j++)
				{
					board[i][j]=' ';
					temp = (JButton)buttonsGrid.getComponent(i*3+j);
					temp.setText("");
					temp.setForeground(Color.BLACK);
				}
			}
		}
		public void del(int x,int y)
		{
			//Not deleting from main grid because only used internally in the minmax algo
			if(x>=0 && x<3 && y>=0 && y<3)
			{
				board[x][y]=' ';
			}
		}
		public int put(int x,int y,char val,boolean forMinmax)
		{
			if(x>=0 && x<3 && y>=0 && y<3 && board[x][y]==' ')
			{
				board[x][y]=val;
				if(forMinmax==false)
				{
					JButton temp = (JButton)buttonsGrid.getComponent(x*3+y);
					temp.setText("" + val);
				}
				return 0;
			}
			else
			{
				return 1;
			}
		}
		public char[][] get()
		{
			return board;
		}
		public void print()
		{
			int i,j;
			for(i=0;i<3;i++)
			{
				for(j=0;j<3;j++)
				{
					System.out.print(board[i][j]+" ");
				}
				System.out.println();
			}
		}
		public char checkState(boolean forMinmax)
		{
			int i,j;
			JButton temp;
			for(i=0;i<3;i++)
			{
				if(board[i][0]==board[i][1] && board[i][1]==board[i][2] && board[i][0]!=' ')
				{
					if(forMinmax==false)
					{
						temp = (JButton)buttonsGrid.getComponent(i*3);
						temp.setForeground(Color.RED);
						temp = (JButton)buttonsGrid.getComponent(i*3+1);
						temp.setForeground(Color.RED);
						temp = (JButton)buttonsGrid.getComponent(i*3+2);
						temp.setForeground(Color.RED);
					}
					return board[i][0];
				}
				if(board[0][i]==board[1][i] && board[1][i]==board[2][i] && board[0][i]!=' ')
				{
					if(forMinmax==false)
					{
						temp = (JButton)buttonsGrid.getComponent(i);
						temp.setForeground(Color.RED);
						temp = (JButton)buttonsGrid.getComponent(3+i);
						temp.setForeground(Color.RED);
						temp = (JButton)buttonsGrid.getComponent(6+i);
						temp.setForeground(Color.RED);
					}
					return board[0][i];
				}
			}
			if(board[0][0]==board[1][1] && board[1][1]==board[2][2] && board[0][0]!=' ')
			{
				if(forMinmax==false)
				{
					temp = (JButton)buttonsGrid.getComponent(0);	//0*3+0=0
					temp.setForeground(Color.RED);
					temp = (JButton)buttonsGrid.getComponent(4);	//1*3+1 = 4
					temp.setForeground(Color.RED);
					temp = (JButton)buttonsGrid.getComponent(8);		//2*3+2 = 8
					temp.setForeground(Color.RED);
				}
				return board[0][0];
			}
			if(board[0][2]==board[1][1] && board[1][1]==board[2][0] && board[2][0]!=' ')
			{
				if(forMinmax==false)
				{
					temp = (JButton)buttonsGrid.getComponent(2);	//0*3+2=2
					temp.setForeground(Color.RED);
					temp = (JButton)buttonsGrid.getComponent(4);	//1*3+1=4
					temp.setForeground(Color.RED);
					temp = (JButton)buttonsGrid.getComponent(6);	//2*3+0=6;
					temp.setForeground(Color.RED);
				}
				return board[0][2];
			}
			for(i=0;i<3;i++)
			{
				for(j=0;j<3;j++)
				{
					if(board[i][j]==' ')
					{
						return 'C';		//continue
					}
				}
			}
			return 'T';		//tie
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		String action = ((JButton)e.getSource()).getName();
		boolean changed=false;
		if(action.equals("Exit"))
		{
			//frame.dispose();
			System.exit(0);
		}
		else if(action.equals("Start Game"))
		{
			startGame();
		}
		else if(action.equals("User vs User"))
		{
			changed=gameModeChange((JButton)e.getSource());
			if(changed==true)
			{
				userVsUserGame();
			}
		}
		else if(action.equals("User vs CPU"))
		{
			changed=gameModeChange((JButton)e.getSource());
			if(changed==true)
			{
				userVsCPUGame();
			}
		}
		else if(action.equals("CPU vs AI"))
		{
			changed=gameModeChange((JButton)e.getSource());
			if(changed==true)
			{
				CPUvsAIGame();
			}
			
		}
		else if(action.equals("User vs AI"))
		{
			changed=gameModeChange((JButton)e.getSource());
			if(changed==true)
			{
				userVsAIGame();
			}
		}
		//else it's a grid button
		//So check if a mode is active and it's user's turn
		else if(gameMode!=null && userturn==true)
		{
			int index = Integer.parseInt(action);
			int x,y;
			y=index%3;
			x=index/3;
			switch(gameMode.getName())
			{
			case "User vs User" : userVsUserInput(x,y);
									break;
			case "User vs CPU" : userVsCPUInput(x,y);
									break;
			case "User vs AI" : userVsAIInput(x,y);
									break;
			}
		}
	}
	private boolean gameModeChange(JButton newGameMode)
	{
		boolean changeConfirmed=true;
		if(gameMode!=null)
		{
			changeConfirmed=confirmChange();
		}
		if(changeConfirmed==true)
		{
			if(timer!=null && timer.isRunning())
			{
				timer.stop();
			}
			if(gameMode!=null)
			{
				gameMode.setBackground(buttonColor);
			}
			gameMode=newGameMode;
			gameMode.setBackground(Color.ORANGE);
			grid.reset();
			return true;
		}
		return false;
	}
	private boolean confirmChange()
	{
		int option = JOptionPane.showConfirmDialog(frame,"Are you sure you want to reset the game?","Confirm change",JOptionPane.YES_NO_OPTION);
		if(option==JOptionPane.YES_OPTION)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private char workOnCheckState(String representingX,String representingO)
	{
		char state;
		state=grid.checkState(false);
		if(state=='O')
		{
			outputBox.setText(representingO + " wins");
			if(gameMode!=null)
			{
				gameMode.setBackground(buttonColor);
				gameMode=null;
			}
			cur=0;
			userturn=false;
			usernames[0]=null;
			usernames[1]=null;
		}
		else if(state=='X')
		{
			outputBox.setText(representingX + " wins");
			if(gameMode!=null)
			{
				gameMode.setBackground(buttonColor);
				gameMode=null;
			}
			cur=0;
			userturn=false;
			usernames[0]=null;
			usernames[1]=null;
		}
		else if(state=='T')
		{
			outputBox.setText("It's a Tie!");
			if(gameMode!=null)
			{
				gameMode.setBackground(buttonColor);
				gameMode=null;
			}
			cur=0;
			userturn=false;
			usernames[0]=null;
			usernames[1]=null;
		}
		else
		{
			cur=1-cur;
			if(mark[cur]=='O')
			{
				outputBox.setText(representingO + "\'s turn");
			}
			else
			{
				outputBox.setText(representingX + "\'s turn");
			}
		}
		return state;
	}
	private void CPUvsAIGame()
	{
		Random rand=new Random();
		int x=rand.nextInt(2);
		userturn=false;
		final boolean AI;
		if(x==0)
		{
			outputBox.setText("AI's turn");
			AI=true;
		}
		else
		{
			outputBox.setText("CPU's turn");
			AI=false;
		}
		cur=0;
		timer = new Timer(1000,null);
		timer.addActionListener(new ActionListener(){
			private boolean turnOfAI=AI;
			public void actionPerformed(ActionEvent e)
			{
				char state;
				if(turnOfAI)
				{
					AIMove();
					outputBox.setText("CPU's turn");
				}
				else
				{
					CPUMove();
					outputBox.setText("AI's turn");
				}
				state=grid.checkState(false);
				if(state==mark[cur])	//Other mark not possible
				{
					if(turnOfAI)
					{
						outputBox.setText("AI wins");
					}
					else
					{
						outputBox.setText("CPU wins");
					}
					if(gameMode!=null)
					{
						gameMode.setBackground(buttonColor);
						gameMode=null;
					}
					cur=0;
					userturn=false;
					timer.stop();
				}
				else if(state=='T')
				{
					outputBox.setText("It's a Tie!");
					if(gameMode!=null)
					{
						gameMode.setBackground(buttonColor);
						gameMode=null;
					}
					cur=0;
					userturn=false;
					timer.stop();
				}
				else
				{
					if(turnOfAI)
					{
						outputBox.setText("CPU's turn");
					}
					else
					{
						outputBox.setText("AI's turn");
					}
				}
				userturn=false;
				cur=1-cur;
				turnOfAI=!(turnOfAI);
			}
		});
		
		timer.start();
	}
	private void userVsCPUGame()
	{
		getSingleUsername();
		Random rand=new Random();
		int x=rand.nextInt(2);
		if(x==0)
		{
			outputBox.setText(usernames[0] + "\'s turn");
			cur=0;
			userturn=true;
		}
		else
		{
			outputBox.setText("CPU's turn");
			cur=0;
			userturn=false;
			CPUMoveVsUser();
			outputBox.setText(usernames[0] + "\'s turn");
			cur=1;
			userturn=true;
		}
	}
	private void CPUMoveVsUser()
	{
		CPUMove();
		char state;
		if(mark[cur]=='O')	//cur represents CPU
		{
			state=workOnCheckState(usernames[0],"CPU");
		}
		else
		{
			state=workOnCheckState("CPU",usernames[0]);
		}
		if(state=='C')
		{
			userturn=true;
		}
	}
	private void CPUMove()
	{
		ArrayList<Integer> empty = new ArrayList<Integer>();
		Random rand = new Random();
		int i,j,count=0,x,y;
		for(i=0;i<3;i++)
		{
			for(j=0;j<3;j++)
			{
				if(grid.get()[i][j]==' ')
				{
					empty.add(i*10+j);
					count++;
				}
			}
		}
		x=rand.nextInt(count);
		y=empty.get(x)%10;
		x=empty.get(x)/10;
		grid.put(x, y, mark[cur],false);	//Return value not required since CPU precisely chose a valid location
	}
	private void userVsCPUInput(int x, int y)
	{
		char state;
		int temp;
		temp=grid.put(x, y, mark[cur],false);
		if(temp==0)
		{
			if(mark[cur]=='O')	//cur represents user[0]
			{
				state=workOnCheckState("CPU",usernames[0]);
			}
			else
			{
				state=workOnCheckState(usernames[0],"CPU");
			}
			if(state=='C')
			{
				userturn=false;
				CPUMoveVsUser();
			}
		}
	}
	private void getSingleUsername()
	{
		usernames[0]=null;
		usernames[1]=null;
		JTextField user1 = new JTextField();
		Object[] message = {"User 1: ", user1};;
		boolean firstPass=true;
		do
		{
			if(firstPass==false)
			{
				Object[] temp = new Object[3];
				for(int i=0;i<2;i++)
				{
					temp[i]=message[i];
				}
				temp[2] = "Don't enter any empty string";
				message=temp;
			}
			int option = JOptionPane.showConfirmDialog(frame,message,"Enter users' names",JOptionPane.DEFAULT_OPTION);
			if(option==JOptionPane.OK_OPTION)
			{
				if(!user1.getText().equals(""))
				{
					usernames[0]=user1.getText();
				}
			}
			firstPass=false;
		}while(usernames[0]==null);
	}

	private void userVsUserInput(int x,int y)
	{
		int temp;
		temp=grid.put(x, y, mark[cur],false);
		if(temp==0)
		{
			workOnCheckState(usernames[0],usernames[1]);	//return value not required
		}
	}
	public void userVsUserGame()
	{
		usernames[0]=null;
		usernames[1]=null;
		getUsernames();
		cur=0;
		userturn=true;
		outputBox.setText(usernames[0]+ "\'s turn");
	}
	private void getUsernames()
	{
		usernames[0]=null;
		usernames[1]=null;
		JTextField user1 = new JTextField();
		JTextField user2 = new JTextField();
		Object[] message = {"User 1: ", user1, "User 2: ", user2};;
		boolean firstPass=true;
		do
		{
			if(firstPass==false)
			{
				Object[] temp = new Object[5];
				for(int i=0;i<4;i++)
				{
					temp[i]=message[i];
				}
				temp[4] = "Empty string or duplicate names not allowed";
				message=temp;
			}
			int option = JOptionPane.showConfirmDialog(frame,message,"Enter users' names",JOptionPane.DEFAULT_OPTION);
			if(option==JOptionPane.OK_OPTION)
			{
				if(!user1.getText().equals("") && !user1.getText().equals(usernames[1]))
				{
					usernames[0]=user1.getText();
				}
				if(!user2.getText().equals("") && !user2.getText().equals(usernames[0]))
				{
					usernames[1]=user2.getText();
				}
			}
			firstPass=false;
		}while(usernames[0]==null || usernames[1]==null);
	}
	
	private void userVsAIGame()
	{
		getSingleUsername();
		Random rand=new Random();
		int p=rand.nextInt(2);
		if(p==0)
		{
			outputBox.setText(usernames[0] + "\'s turn");
			cur=0;
			userturn=true;
		}
		else
		{
			outputBox.setText("AI's turn");
			cur=0;
			userturn=false;
			//Starts at any corner for first move
			int temp,x,y;
			temp=rand.nextInt(4);
			if(temp==0)
			{
				x=0;
				y=0;
			}
			else if(temp==1)
			{
				x=0;
				y=2;
			}
			else if(temp==2)
			{
				x=2;
				y=0;
			}
			else
			{
				x=2;
				y=2;
			}
			grid.put(x, y, mark[cur],false);
			outputBox.setText(usernames[0] + "\'s turn");
			cur=1;
			userturn=true;
		}
	}
	private void AIMoveVsUser()
	{
		AIMove();
		char state;
		if(mark[cur]=='O')	//cur represents AI
		{
			state=workOnCheckState(usernames[0],"AI");
		}
		else
		{
			state=workOnCheckState("AI",usernames[0]);
		}
		if(state=='C')
		{
			userturn=true;
		}
	}
	private void AIMove()
	{
		int x,y;
		int[] result=minmax(0,grid,1,mark[cur]);
		x=result[1];
		y=result[2];
		grid.put(x, y, mark[cur],false);
	}
	private int[] minmax(int depth,Grid game,int AI,char cur)
	{
		depth++;
		char gameover=game.checkState(true);
		if(gameover!='C')
		{
			char mysign;
			if(AI==1)
			{
				mysign=cur;
			}
			else
			{
				if(cur=='O')
				{
					mysign='X';
				}
				else
				{
					mysign='O';
				}
			}
			int score=evaluatescore(game,mysign,depth);
			return new int[] {score,-1,-1};
		}
		ArrayList<Integer> allowedmoves = new ArrayList<Integer>();
		int i,j;
		for(i=0;i<3;i++)
		{
			for(j=0;j<3;j++)
			{
				if(game.get()[i][j]==' ')
				{
					allowedmoves.add(i*10+j);
				}
			}
		}
		int bestscore,curscore,row=-1,col=-1;
		if(AI==1)
		{
			bestscore=Integer.MIN_VALUE;
		}
		else
		{
			bestscore=Integer.MAX_VALUE;
		}
		for(int curmove : allowedmoves)
		{
			game.put(curmove/10,curmove%10,cur,true);
			if(AI==1)
			{
				if(cur=='O')
				{
					curscore=minmax(depth,game,0,'X')[0];
				}
				else
				{
					curscore=minmax(depth,game,0,'O')[0];
				}
				if(curscore>bestscore)
				{
					bestscore=curscore;
					row=curmove/10;
					col=curmove%10;
				}
			}
			else
			{
				if(cur=='O')
				{
					curscore=minmax(depth,game,1,'X')[0];
				}
				else
				{
					curscore=minmax(depth,game,1,'O')[0];
				}
				if(curscore<bestscore)
				{
					bestscore=curscore;
					row=curmove/10;
					col=curmove%10;
				}	
			}
			game.del(curmove/10,curmove%10);
		}
		return new int[] {bestscore,row,col};
	}
	private int evaluatescore(Grid game, char mysign, int depth)
	{
		char state=game.checkState(true);
		if(state==mysign)
		{
			return 10-depth;
		}
		else if(state=='T')
		{
			return 0;
		}
		else
		{
			return depth-10;
		}
	}
	private void userVsAIInput(int x, int y)
	{
		char state;
		int temp;
		temp=grid.put(x, y, mark[cur],false);
		if(temp==0)
		{
			if(mark[cur]=='O')	//cur represents user[0]
			{
				state=workOnCheckState("AI",usernames[0]);
			}
			else
			{
				state=workOnCheckState(usernames[0],"AI");
			}
			if(state=='C')
			{
				userturn=false;
				AIMoveVsUser();
			}
		}
	}
	public void startGame()
	{
		frame.setVisible(false);
		frame.setSize(650, 500);
		frame.getContentPane().removeAll();		//empty the frame
		//frame.revalidate();
		
		frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.X_AXIS));
		frame.add(Box.createRigidArea(new Dimension(60,0)));
		//Create panel for buttons
		JPanel buttonPanel = createStartGameButtonPanel();		
		
		
		//Panel for grid and output Box
		JPanel outputAndGrid = createOutputAndGridPanel();
		
		frame.add(buttonPanel);
		frame.add(Box.createRigidArea(new Dimension(60,0)));
		frame.add(outputAndGrid);
		frame.add(Box.createRigidArea(new Dimension(60,0)));
		frame.setVisible(true);
		grid = new Grid();
	}
	private JPanel createOutputAndGridPanel()
	{
		JPanel outputAndGrid = new JPanel();
		outputAndGrid.setLayout(new BoxLayout(outputAndGrid,BoxLayout.Y_AXIS));
		outputAndGrid.add(Box.createRigidArea(new Dimension(0,30)));
		
		//Create panel for grid
		JPanel grid = createGridPanel();
		addToPanel(outputAndGrid,grid);
		this.buttonsGrid=grid;
		//Blank space
		outputAndGrid.add(Box.createRigidArea(new Dimension(0,30)));
		
		//Create output box
		JLabel outputBox = new JLabel("Choose game mode");
		
		outputBox.setHorizontalTextPosition(SwingConstants.CENTER);
		outputBox.setOpaque(true);
		outputBox.setHorizontalAlignment(SwingConstants.CENTER);
		outputBox.setFont(new Font("Calibri",Font.BOLD,20));
		outputBox.setBackground(Color.WHITE);
		outputBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		outputBox.setMinimumSize(new Dimension(350,70));
		outputBox.setMaximumSize(new Dimension(350,70));
		outputBox.setPreferredSize(new Dimension(350,70));
		outputAndGrid.add(outputBox);
		this.outputBox=outputBox;
		//Blank space
		outputAndGrid.add(Box.createRigidArea(new Dimension(0,30)));
				
		return outputAndGrid;
	}
	private JPanel createGridPanel()
	{
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(3,3));
		
		//create buttons
		JButton[] buttons = new JButton[9];
		for(int i=0;i<9;i++)
		{
			buttons[i]=new JButton("");
			buttons[i].setName(String.valueOf(i));
			buttons[i].setFont(new Font("Calibri",Font.BOLD,40));
			buttons[i].addActionListener(this);
			addToPanel(grid,buttons[i],BorderFactory.createLineBorder(Color.BLACK),Color.WHITE,Color.BLACK,null,null,null);
			//grid.add(buttons[i]);
		}
		return grid;
	}
	private JPanel createStartGameButtonPanel()
	{
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		
		//blank space
		//buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		//User1 vs User2 button
		JButton userVsUser = new JButton("User 1 vs User 2");
		userVsUser.setName("User vs User");
		userVsUser.addActionListener(this);
		addToPanel(buttonPanel,userVsUser);

		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		//User vs CPU button
		JButton userVsCPU = new JButton("User vs CPU");
		userVsCPU.setName("User vs CPU");
		userVsCPU.addActionListener(this);
		addToPanel(buttonPanel,userVsCPU);

		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		//CPU vs AI Bot Button
		JButton CPUvsAI = new JButton("CPU vs AI Bot");
		CPUvsAI.setName("CPU vs AI");
		CPUvsAI.addActionListener(this);
		addToPanel(buttonPanel,CPUvsAI);
		
		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));

		//User vs AI Bot Button
		JButton userVsAI = new JButton("User vs AI Bot");
		userVsAI.setName("User vs AI");
		userVsAI.addActionListener(this);
		addToPanel(buttonPanel,userVsAI);
		

		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,90)));
		
		//exit Button
		JButton exit = new JButton("Exit");
		exit.setName("Exit");
		exit.addActionListener(this);
		addToPanel(buttonPanel,exit);

		return buttonPanel;
	}

	private void startScreen()
	{
		//Create a panel
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		
		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,60)));
		
		//add label
		JLabel name=new JLabel("Tic-Tac-Toe");
		name.setFont(new Font("Calibri",Font.BOLD,20));
		addToPanel(buttonPanel,name);
		
		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		//startGame button
		JButton startGame=new JButton("Start Game");
		startGame.setName("Start Game");
		startGame.addActionListener(this);
		addToPanel(buttonPanel,startGame);
		//System.out.println(startGame.getText());
		//startGame.setBounds(95, 80, 100, 30);
		
		//blank space
		buttonPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		//exit button
		JButton exit = new JButton("Exit");
		exit.setName("Exit");
		exit.addActionListener(this);
		addToPanel(buttonPanel,exit);
		
		//add Panel to frame
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
		frame.add(buttonPanel);
		frame.setVisible(true);
	}
	private void addToPanel(JPanel p,JComponent c, Border b, Color bgcolor, Color forecolor, Dimension max, Dimension min, Dimension pref )
	{
		if(c instanceof JButton)
		{
			c.setBorder(b);
			c.setBackground(bgcolor);
			c.setForeground(forecolor);
			c.setMinimumSize(min);
			c.setMaximumSize(max);
		}
		c.setAlignmentX(Component.CENTER_ALIGNMENT);
		//c.setAlignmentY(Component.CENTER_ALIGNMENT);
		//c.setPreferredSize(new Dimension(100,30));
		c.setPreferredSize(pref);
		p.add(c);
		
	}

	private void addToPanel(JPanel p,JComponent c)
	{
		/*Default values : 
		c.setBorder(BorderFactory.createLineBorder(Color.BLACK,2,true));
		c.setBackground(new Color(40,200,200));
		c.setForeground(Color.WHITE);
		c.setMinimumSize(new Dimension(100,30));
		c.setMaximumSize(new Dimension(100,30));
		c.setPreferredSize(new Dimension(100,30);
		*/
		
		addToPanel(p,c,BorderFactory.createLineBorder(Color.BLACK,2,true), buttonColor,Color.WHITE,new Dimension(100,30),new Dimension(100,30),new Dimension(100,30));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//TODO font size of buttons, colors of side buttons
		//TODO OK cancel option, confirm to change mode, return to start screen?
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		Game game=new Game();
	}

}
