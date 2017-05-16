/*
 * Class that defines the agent function.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Last modified 2/19/07 
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
class Game{
	public int score;
	public ArrayList<Integer> actionList = new ArrayList<>();
}

class GameList{
	ArrayList<Game> gmList = new ArrayList<>();
}

class AgentFunction {
	previousMove prev;
	public enum previousMove{
		WRONG,RIGHT, ONELESS
	}
	prevMove prevmove;
	public enum prevMove{
		OPENLEFT, OPENRIGHT, LISTEN
	}
	lastMoveSteps lastMoveStepsWere;
	public enum lastMoveSteps{
		TWOSTEPS, THREESTEPS
	}
	int[] threeStepsArray = new int[3];
	int[] twoStepsArray = new int[2];
	static int count = 0;
	static HashMap<Integer, GameList> map= new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> possibileSequence = new HashMap<>();
    static HashMap<Integer, Integer> scoreMap = new HashMap<>();
	public static void init() {
		//Called at the start of the program
	
	}
	
	public AgentFunction() {
		//Do Nothing
		//
		//...
		// TO RUN THE SIMULATION:
		//   java TigerGame -trials 10000 -timesteps 10
		count+=1;
	
		
	}
	static int leftGrowlCount =0;
	static int rightGrowlCount = 0;
	static int rightMoves = 0;
	static int wrongMoves = 0;
	Game g = new Game();
	int ignoreValue = -1;
	int dontIgnoreValue = -1;
	 GameList gm = new GameList();
	int timestepCount = 0;
	
	int observations;
	private int state;
	
	possibleState s;
	public enum possibleState
	{
		Listen, OpenRight, OpenLeft, MayBeLeft, MayBeRight
	}
	possibleState parentState;
	possibleState dangerousState;
	stateObserved stateObs;
	public enum stateObserved
	{
		stateObservedNow, stateNotObservedNow
	}
	public int process(int score, boolean growlLeft, boolean growlRight) {
	//	System.out.println(stateObs);
	//	System.out.println(s);
		scoreMap.put(timestepCount, score);
		
		if(scoreMap.get(timestepCount - 1)!=null)
		{
			System.out.println("PREV - CURRENT"+ (score - scoreMap.get(timestepCount - 1)));
			if(score - scoreMap.get(timestepCount - 1)== -100 || score - scoreMap.get(timestepCount - 1)== 100 )
			{
				System.out.println("WRONG");
				wrongMoves+=1;
				prev = previousMove.WRONG;
			}
			else if(score - scoreMap.get(timestepCount - 1)==-10 || score - scoreMap.get(timestepCount - 1)== 10)
			{
				System.out.println("RIGHT");
				rightMoves+=1;
				prev = previousMove.RIGHT;
			}
			else
			{
				prev = previousMove.ONELESS;
			}
		}
		timestepCount+=1;
		if(prev == previousMove.WRONG )
		{
			if(s == possibleState.MayBeRight)
			{
				parentState = possibleState.Listen;
			}
			else if(s == possibleState.MayBeLeft)
			{
				parentState = possibleState.Listen;
			}
			else if(s== possibleState.OpenLeft)
			{
				parentState = possibleState.MayBeLeft;
			}
			else if(s==possibleState.OpenRight)
			{
				parentState = possibleState.MayBeRight;
			}
			
			if(parentState!=null)
			{
				dangerousState = parentState;
				
			}
		}
		if(s == possibleState.MayBeRight)
		{
			if(growlLeft==true && dangerousState == null)
			{
				g.actionList.add(0);
				s = possibleState.OpenRight;
				return Action.OPEN_RIGHT_DOOR;
				
			}
			else if(growlRight==true)
			{  
				g.actionList.add(1);
				s = possibleState.Listen;
				
			}
			dangerousState= null;
			return Action.LISTEN;
		}
		if(s == possibleState.MayBeLeft)
		{
			if(growlLeft==true)
			{
				g.actionList.add(0);
				s = possibleState.Listen;
				
			}
			if(growlRight==true && dangerousState == null)
			{
				g.actionList.add(1);
				s = possibleState.OpenLeft;
				return Action.OPEN_LEFT_DOOR;
			}
			dangerousState= null;
			return Action.LISTEN;
		}
		if(s == possibleState.OpenRight)
		{
			
			s = possibleState.Listen;
			return Action.LISTEN;
		}
		if(s == possibleState.OpenLeft)
		{
			s = possibleState.Listen;
			return Action.LISTEN;
		}
		if(stateObs != stateObserved.stateObservedNow || stateObs==null || s == possibleState.Listen)
		{ 
			dangerousState=null;
			if(growlLeft==true)
			{
				g.actionList.add(0);
				s = possibleState.MayBeRight;
			}
			if(growlRight==true)
			{
				g.actionList.add(1);
				s = possibleState.MayBeLeft;
			
			}
			return Action.LISTEN;
		}
		s= possibleState.Listen;
		return Action.LISTEN;
	
		/*
		scoreMap.put(timestepCount, score);
		System.out.println(scoreMap);
	
		if(scoreMap.get(timestepCount - 1)!=null)
		{
			System.out.println("PREV - CURRENT"+ (score - scoreMap.get(timestepCount - 1)));
			if(score - scoreMap.get(timestepCount - 1)== -100 || score - scoreMap.get(timestepCount - 1)== 100 )
			{
				System.out.println("WRONG");
				wrongMoves+=1;
				prev = previousMove.WRONG;
			}
			if(score - scoreMap.get(timestepCount - 1)==-10 || score - scoreMap.get(timestepCount - 1)== 10)
			{
				System.out.println("RIGHT");
				rightMoves+=1;
				prev = previousMove.RIGHT;
			}
		}
		
			if(growlLeft)
			{
				leftGrowlCount+=1;
			}
			else
			{
				rightGrowlCount+=1;
			}
			
			if(leftGrowlCount==2)
			{
				leftGrowlCount=0;
				prevmove = prevMove.OPENRIGHT;
				return Action.OPEN_RIGHT_DOOR;
			}
			else if(rightGrowlCount==2)
			{
				rightGrowlCount = 0;
				prevmove = prevMove.OPENLEFT;
				return Action.OPEN_LEFT_DOOR;
			}
			
			else if(leftGrowlCount >= 1 && rightGrowlCount>=1)
			{
				leftGrowlCount = 0;
				rightGrowlCount = 0;
				prevmove = prevMove.LISTEN;
				return Action.LISTEN;
			}
			prevmove = prevMove.LISTEN;
		return Action.LISTEN;
		*/
		/*
		System.out.println("CurrentScore ="+score);
		
		scoreMap.put(timestepCount, score);
		
		System.out.println(scoreMap);
		if(scoreMap.get(timestepCount - 1)!=null)
		{
			System.out.println("PREV - CURRENT"+ (score - scoreMap.get(timestepCount - 1)));
			if(score - scoreMap.get(timestepCount - 1)== -100 || score - scoreMap.get(timestepCount - 1)== 100 )
			{
				System.out.println("WRONG");
				wrongMoves+=1;
				prev = previousMove.WRONG;
			}
			if(score - scoreMap.get(timestepCount - 1)==-10 || score - scoreMap.get(timestepCount - 1)== 10)
			{
				System.out.println("RIGHT");
				rightMoves+=1;
				prev = previousMove.RIGHT;
			}
		}
		
		if(prev == previousMove.WRONG)
		{
			if(lastMoveStepsWere == lastMoveStepsWere.THREESTEPS)
			{
				ignoreValue = threeStepsArray[0];
			}
			else
			{
				ignoreValue = threeStepsArray[0];
			}
		}
		else if(prev == previousMove.RIGHT)
		{
			if(lastMoveStepsWere == lastMoveStepsWere.THREESTEPS)
			{
				dontIgnoreValue = threeStepsArray[0];
			}
			else
			{
				dontIgnoreValue = threeStepsArray[0];
			}
		}
		timestepCount+=1;
		if(growlLeft==true)
			{
				leftGrowlCount +=1;
				g.actionList.add(0);
				
			}
			else
			{
				rightGrowlCount +=1;
				g.actionList.add(1);
			
			}
		
		if(g.actionList.size()>3)
		{
			
			System.out.println(g.actionList.get(0));
			System.out.println(g.actionList.get(1));
			System.out.println(g.actionList.get(2));
			if(g.actionList.get(0) == dontIgnoreValue && g.actionList.get(1)!= ignoreValue && prev== previousMove.RIGHT)
			{
				threeStepsArray[0] = g.actionList.get(0);
				threeStepsArray[1] = g.actionList.get(1);
				threeStepsArray[2] = g.actionList.get(2);
				if(prevmove == prevMove.OPENRIGHT &&  g.actionList.get(0)==0 && prev== previousMove.RIGHT)
				{
					prevmove = prevMove.OPENRIGHT;
					scoreMap.put(count, score);
					lastMoveStepsWere = lastMoveSteps.THREESTEPS;
					return Action.OPEN_RIGHT_DOOR;
				}
				else if(prevmove == prevMove.OPENLEFT &&  g.actionList.get(0)==1 && prev== previousMove.RIGHT)
				{
					prevmove = prevMove.OPENLEFT;
					scoreMap.put(count, score);
					lastMoveStepsWere = lastMoveSteps.THREESTEPS;
					return Action.OPEN_LEFT_DOOR;
				}
			}
			if(g.actionList.get(0)== g.actionList.get(1) &&  g.actionList.get(1) ==g.actionList.get(2) )
			{
				threeStepsArray[0] = g.actionList.get(0);
				threeStepsArray[1] = g.actionList.get(1);
				threeStepsArray[2] = g.actionList.get(2);
				if(g.actionList.get(0)==0 )
				{
					scoreMap.put(count, score);
					lastMoveStepsWere=lastMoveSteps.THREESTEPS;
					
					prevmove = prevMove.OPENRIGHT;
					return Action.OPEN_RIGHT_DOOR;
				}
				else{
					scoreMap.put(count, score);
					lastMoveStepsWere = lastMoveSteps.THREESTEPS;
					prevmove = prevMove.OPENLEFT;
					return Action.OPEN_LEFT_DOOR;
				}
				
			}
			else if(g.actionList.get(0) == g.actionList.get(2) && prev== previousMove.RIGHT)
			{
				threeStepsArray[0] = g.actionList.get(0);
				threeStepsArray[1] = g.actionList.get(1);
				threeStepsArray[2] = g.actionList.get(2);
				lastMoveStepsWere = lastMoveSteps.THREESTEPS;
				prevmove = prevMove.LISTEN;
				scoreMap.put(count, score);
				return Action.LISTEN;
			}
			
		}
		else if(g.actionList.size()==2)
		{scoreMap.put(count, score);
			
			System.out.println(g.actionList.get(0));
			System.out.println(g.actionList.get(1));
			
			if(g.actionList.get(0)==g.actionList.get(1) )
			{
				twoStepsArray[0] = g.actionList.get(0);
				twoStepsArray[1] = g.actionList.get(1);
				if(g.actionList.get(0)==0 && prev== previousMove.RIGHT)
				{
					lastMoveStepsWere = lastMoveSteps.TWOSTEPS;
					prevmove = prevMove.OPENRIGHT;
					return Action.OPEN_RIGHT_DOOR;
				}
				
				else if(g.actionList.get(0)==1  && prev== previousMove.RIGHT)
				{
					
					lastMoveStepsWere = lastMoveSteps.TWOSTEPS;
					prevmove = prevMove.OPENLEFT;
					return Action.OPEN_LEFT_DOOR;
				}
				else
				{
					if(Math.random() > 0.5)
					{
						lastMoveStepsWere = lastMoveSteps.TWOSTEPS;
						prevmove = prevMove.OPENRIGHT;
						return Action.OPEN_RIGHT_DOOR;
					}
					else
					{
						lastMoveStepsWere = lastMoveSteps.TWOSTEPS;
						prevmove = prevMove.OPENLEFT;
						return Action.OPEN_LEFT_DOOR;
					}
				}
			}
		}
		
		prevmove = prevMove.LISTEN;
		return Action.LISTEN;
*/
		//return score;
	}
	
	public void cleanup(int score) {
		//Do nothing
		//
		//...
		//(If you want to save data or do any book keeping)
	
		System.out.println("Count"+count);
			g.score =score;
			gm.gmList.add(g);
			
			map.put(count, gm);
		
	}
	
	public static void exit() {
		//called at the end of the program
		/*
		for(Integer i: map.keySet())
		{
			System.out.println("Game"+ i);
			System.out.println(map.get(i).gmList.get(0).score);
			System.out.println("LEFT GROWL"+leftGrowlCount);
			System.out.println("RIGHT GROWL"+rightGrowlCount);
			System.out.println(map.get(i).gmList.get(0).actionList);
		}*/
		System.out.println("RIGHT MOVES"+rightMoves);
		System.out.println("WRONG MOVES"+wrongMoves);
		
	}




}
