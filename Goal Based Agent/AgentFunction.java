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

//Class that holds behavior and state of a square in the world.
class Location
{
	public int xPoint;
	public int yPoint;
	public Location()
	{
		xPoint= 0; yPoint = 0;
	}
	public Location(int a, int b)
	{
		xPoint =a; yPoint =b;
	}
}


class AgentEnvironment
{
	public int visitedLocation;	//if visited
	public  int safePosition;		//if safe
	public  int dangerousPlace;		//if dangerous
	public  int numberVisited;	//no of times visited

	public AgentEnvironment()
	{
	   visitedLocation = 0;
		safePosition = 0;
		dangerousPlace = 0;
		numberVisited = 0;
	}

	public AgentEnvironment(int visitedloc, int safeloc, int dangerousloc, int number)
	{
		visitedLocation = visitedloc;
		safePosition = safeloc;
		dangerousPlace = dangerousloc;
		numberVisited = number;
	}
}
class AgentFunction
{
	
	private class room {
		//each room position is defined by its row and column
		public int RoomRow;
		public int RoomCol;
		public boolean Pit;//defined if pit there or not
		public boolean Wampus;//defined if wampus there or not
		public boolean Safe;//defined if safe or not
		public int NumVist;//number of times agent has been to a room to prevent back tracking

		public room(){
			Wampus = true;//assumed wampus in room unless sensed otherwise
			Pit = true;//assumed pit in room unless sensed otherwise
			Safe = false; //assumed as unsafe / defined safe when no wampus or pit sensed
			NumVist = 0;//agent hasn't visited at start
		}
		
	}
	
	private class MyEnviroment{
		//player position defined by its column and row position
		public int playerPosCol;
		public int playerPosRow;
		//player orientation defined as 'N' = north 'S' = south 'E' = east 'W' = west
		public char Facing;
		//if agent has arrow or not
		public boolean HaveArrow;
		//enviroment consist of a 4x4 board of rooms
		room[][] Board = new room[4][4];
		
		public MyEnviroment(){
			//agent starts out facing east
			Facing = 'E';
			//agent starts out at room 0,0
			playerPosCol = 0;
			playerPosRow = 0;
			//agent starts out having an arrow
			HaveArrow = true;
			
			//initialize rooms
			for (int j =0; j<4;j++){
				for(int i = 0; i<4; i++){
					Board[i][j] = new room();
					Board[i][j].RoomCol = i;
					Board[i][j].RoomRow = j;
				}
			}			
		}
	
		//funtion to update the position of agent if move forward based on facing
			public void PosUpdate( int move){
				if(Action.GO_FORWARD == move){
					if(Facing == 'N')
					{
						if(playerPosRow == 3)
						{}
						else
						{playerPosRow++;}
					
					}
					else if(Facing == 'S')
					{
						if(playerPosRow == 1)
						{}
						else
						{playerPosRow--;}
					}
					else if(Facing == 'E')
					{
						if(playerPosCol == 3)
						{}
						else
						{playerPosCol++;}
					}
					else if(Facing == 'W')
					{
						if(playerPosCol == 0)
						{}
						else
						{playerPosCol--;}
					}
				}

				return;
			}
			//function to update facing of agent if action is turn right or turn left
		public void FacingUpdate(int act)
			{
				if(act == Action.TURN_LEFT)
				{
					if(Facing == 'N')
					{Facing = 'W';}
					else if(Facing == 'W')
					{Facing = 'S';}
					else if(Facing == 'S')
					{Facing = 'E';}
					else if(Facing == 'E')
					{Facing = 'N';}
				}
				if(act == Action.TURN_RIGHT)
				{
					if(Facing == 'N')
					{Facing = 'E';}
					else if(Facing == 'E')
					{Facing = 'S';}
					else if(Facing == 'S')
					{Facing = 'W';}
					else if(Facing == 'W')
					{Facing = 'N';}
				}
			
			}
		//function to update the pressence of pits if no breeze detected
		// turns boolean pit to false in adjacent rooms
		public void NoBreezeDetectUpdate()
		{
			if((playerPosRow < 3))
				{Board[playerPosRow+1][playerPosCol].Pit = false;}
			if((playerPosRow > 0))
				{Board[playerPosRow-1][playerPosCol].Pit = false;}
			if((playerPosCol < 3))
				{Board[playerPosRow][playerPosCol+1].Pit = false;}
			if((playerPosCol > 0))
				{Board[playerPosRow][playerPosCol-1].Pit = false;}
		}
		//function to update the pressence of wampus if no stench detected
		// turns boolean wampus to false in adjacent rooms
		public void NoStenchDetectUpdate()
		{
			if((playerPosRow < 3))
				{Board[playerPosRow+1][playerPosCol].Wampus = false;}
			if((playerPosRow > 0))
				{Board[playerPosRow-1][playerPosCol].Wampus = false;}
			if((playerPosCol < 3))
				{Board[playerPosRow][playerPosCol+1].Wampus = false;}
			if((playerPosCol > 0))
				{Board[playerPosRow][playerPosCol-1].Wampus = false;}
		}
		//function to update the pressence of wampus if scream detected
		// turns boolean wampus to false in all rooms
		public void ScreamDetectUpdate()
		{
			for (int j =0; j<4;j++){
				for(int i = 0; i<4; i++){
					Board[i][j].Wampus = false;
				}
			}
			
		}
		//function to check if rooms are safe
		//checks all rooms to see if both no wampus and no pit
		//when no pit or wampus room determined as safe
		public void SafetyUpdate()
		{
			for (int j =0; j<4;j++){
				for(int i = 0; i<4; i++){
					if((Board[i][j].Wampus == false)&&(Board[i][j].Pit == false))
					{
						Board[i][j].Safe = true;
					}
				}
			}
			
		}
	}
	
	
	// string to store the agent's name
	// do not remove this variable
	private String agentName = "Snitch";
	
	// all of these variables are created and used
	// for illustration purposes; you may delete them
	// when implementing your own intelligent agent
	private int[] actionTable;
	private boolean bump;
	private boolean glitter;
	private boolean breeze;
	private boolean stench;
	private boolean scream;

    private AgentEnvironment agentMap[][];		
    private Location agentLocation;		
	private int facingDirection;		
	public int previousAction;			
	private Location toLocation;		
	private MyEnviroment state;

	public AgentFunction()
	{
		actionTable = new int[8];
		  
		actionTable[0] = Action.GO_FORWARD;
		actionTable[1] = Action.GO_FORWARD;
		actionTable[2] = Action.GO_FORWARD;
		actionTable[3] = Action.GO_FORWARD;
		actionTable[4] = Action.TURN_RIGHT;
		actionTable[5] = Action.TURN_LEFT;
		actionTable[6] = Action.GRAB;
		actionTable[7] = Action.SHOOT;
		state = new MyEnviroment();
		
		
		agentMap = new AgentEnvironment[4][4];
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				agentMap[i][j] = new AgentEnvironment();
		agentLocation = new Location();
		facingDirection = 1;
	    previousAction = Action.NO_OP;
		toLocation = new Location();
	}
	public char getNextRoom(MyEnviroment CS)
	{	
		int NnumVist;
		int SnumVist;
		int EnumVist;
		int WnumVist;
		
		if((CS.playerPosRow < 3))
			if((CS.Board[CS.playerPosRow +1][CS.playerPosCol].Safe))
				{NnumVist = CS.Board[CS.playerPosRow+1][CS.playerPosCol].NumVist;}
			else
			{NnumVist = 100;}
		else
			{NnumVist = 100;}
		if((CS.playerPosRow > 0))
			if((CS.Board[CS.playerPosRow -1][CS.playerPosCol].Safe))
				{SnumVist = CS.Board[CS.playerPosRow-1][CS.playerPosCol].NumVist;}
			else
			{SnumVist = 100;}
		else
			{SnumVist = 100;}
		if((CS.playerPosCol <3))
			if((CS.Board[CS.playerPosRow][CS.playerPosCol+1].Safe))
				{EnumVist = CS.Board[CS.playerPosRow][CS.playerPosCol+1].NumVist;}
			else
			{EnumVist = 100;}
		else
			{EnumVist = 100;}
		if((CS.playerPosCol >0))
			if((CS.Board[CS.playerPosRow][CS.playerPosCol-1].Safe))
				{WnumVist = CS.Board[CS.playerPosRow][CS.playerPosCol-1].NumVist;}
			else
			{WnumVist = 100;}
		else
			{WnumVist = 100;}

		if((NnumVist < SnumVist)&&(NnumVist < EnumVist)&&(NnumVist < WnumVist))
		{return 'N';}
		else if((SnumVist < EnumVist)&&(SnumVist < WnumVist))
		{return 'S';}
		else if(EnumVist < WnumVist)
		{return 'E';}
		else if(WnumVist == 100)
		{return 'U';}
		else
		{return 'W';}
	}

	public int process(TransferPercept tp)
	{
		
		bump = tp.getBump();
		glitter = tp.getGlitter();
		breeze = tp.getBreeze();
		stench = tp.getStench();
		scream = tp.getScream();
		
		
		int action = -1;

		if(glitter)					
		{
			action = Action.GRAB;
		}
		else			
		{
			if(previousAction==Action.GO_FORWARD || previousAction==Action.NO_OP)	//Only if the prev. action is Forward or NoOp try to look for a square to go to
			{
				agentLocation.xPoint = toLocation.xPoint;
				agentLocation.yPoint = toLocation.yPoint;
				ArrayList<Location> sqs = UpdateModel(tp);
				toLocation = ChooseLocation(sqs);
			}

			action = ChooseAction(tp);
		}

		TakeAction(action);

		previousAction = action;

        return action;
	}
	//Marking Safe
		private void MarkSafe(ArrayList<Location> sqs)
		{
			for (int i=0; i<sqs.size(); i++)
				if(agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint].visitedLocation==0)
					agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint] = new AgentEnvironment(0, 1, 0, 0);
		}

	//Updates agent direction for the action taken
    private void TakeAction(int action)
	{
		if (action == Action.TURN_RIGHT)
		{
			if (facingDirection == 0) facingDirection = 1;
			else if (facingDirection == 1) facingDirection = 2;
			else if (facingDirection == 2) facingDirection = 3;
			else if (facingDirection == 3) facingDirection = 0;
		}
		else if (action == Action.TURN_LEFT)
		{
			if (facingDirection == 0) facingDirection = 3;
			else if (facingDirection == 1) facingDirection = 0;
			else if (facingDirection == 2) facingDirection = 1;
			else if (facingDirection == 3) facingDirection = 2;
		}
	}

    private int ChooseAction(TransferPercept tp)
    {
    	bump = tp.getBump();
		glitter = tp.getGlitter();
		breeze = tp.getBreeze();
		stench = tp.getStench();
		scream = tp.getScream();
		//starting room is safe if game doesnt end at start
		state.Board[0][0].Safe = true;
		//increase the number of times current room is visited by one
		state.Board[state.playerPosRow][state.playerPosCol].NumVist++;//update the current position number of times visted

		
		//check for gold pick up if found
		
        if(glitter == true){
            return Action.GRAB;
        }
        //update state based on percepts
        //check for scream and update the current state
        if(scream == true)
        {
        	state.ScreamDetectUpdate();
        }
        //check for breeze update state if no breeze detected
        if (breeze == false)
        {
        	state.NoBreezeDetectUpdate();
        }
        //check for stench update sate if no stench detected
        if (stench == false)
        {
        	state.NoStenchDetectUpdate();
        }
        //update the safety of rooms based on percepts
        state.SafetyUpdate();
        //find a safe room with lowest times visited
        char NearestSafe;
        NearestSafe = getNextRoom(state);
        //'U' means no safe room found
        if(NearestSafe == 'U')
        {
        	//if stench fires arrow for chance at creating safe room
        	if(stench && state.HaveArrow)
        	{
        		state.HaveArrow = false;//updates presence of arrow due to shooting action
        		return Action.SHOOT;
        	}
        	else
        	{return Action.NO_OP;}//no action if no arrow
        }
        //checks if facing correct room 
        else if(NearestSafe == state.Facing)
        	{
        	state.PosUpdate(Action.GO_FORWARD);//updates position before it moves foward
        	return Action.GO_FORWARD;//moves forward
        	}
        //if not facing correct room turns toward if based on orientation
        else if(NearestSafe == 'N')
        	{
        		if (state.Facing == 'W')
					{
						state.FacingUpdate(Action.TURN_RIGHT);
						return Action.TURN_RIGHT;
					}
					else
					{
						state.FacingUpdate(Action.TURN_LEFT);
						return Action.TURN_LEFT;
					}
        	}
        else if(NearestSafe == 'E')
    	{
    		if (state.Facing == 'N')
				{
					state.FacingUpdate(Action.TURN_RIGHT);
					return Action.TURN_RIGHT;
				}
				else
				{
					state.FacingUpdate(Action.TURN_LEFT);
					return Action.TURN_LEFT;
				}
    	}
        else if(NearestSafe == 'S')
    	{
    		if (state.Facing == 'E')
				{
					state.FacingUpdate(Action.TURN_RIGHT);
					return Action.TURN_RIGHT;
				}
				else
				{
					state.FacingUpdate(Action.TURN_LEFT);
					return Action.TURN_LEFT;
				}
    	}

        else if(NearestSafe == 'W')
    	{
    		if (state.Facing == 'S')
    			{
    				state.FacingUpdate(Action.TURN_RIGHT);
    				return Action.TURN_RIGHT;
    			}
    		else
    			{
    			state.FacingUpdate(Action.TURN_LEFT);
    			return Action.TURN_LEFT;
    			}
    	}
        //default no op
        return Action.NO_OP;

    }


	private Location ChooseLocation(ArrayList<Location> locations)
	{
	    Location l = new Location();
		ArrayList<int[]> locationData = GatherData(locations);
		ArrayList<Integer> safeLocations = GetSafeInds(locationData);
		if (safeLocations.size()!=0)
		{
			ArrayList<int[]> newLocationData = SelectData(locationData, safeLocations);
			ArrayList<Location> newLocation = SelectSquares(locations, safeLocations);
		
		
			 int min_ind = 0;

				int min_val = 1000000;
				for(int i=0; i<newLocationData.size(); i++)
				{
					int temp[] = newLocationData.get(i);
					if(temp[4] < min_val)
					{
						min_val = temp[4];
						min_ind = i;
					}
				}
				
			l = newLocation.get(min_ind);
		}
		else if(safeLocations.size()==0)
		{
			ArrayList<Integer> visitedLocations = GetVisitedInds(locationData);
			if (visitedLocations.size()!=0)
			{
				ArrayList<int[]> new_data = SelectData(locationData, visitedLocations);
				ArrayList<Location> new_sqs = SelectSquares(locations, visitedLocations);
				
				 int min_ind = 0;

					int min_val = 1000000;
					for(int i=0; i<new_data.size(); i++)
					{
						int temp[] = new_data.get(i);
						if(temp[4] < min_val)
						{
							min_val = temp[4];
							min_ind = i;
						}
					}
				l = new_sqs.get(min_ind);
			}
			else
			{
				l = agentLocation;
			}
		}
		return l;
	}

	//Marking Visited
	private void MarkVisited()
	{
	    agentMap[agentLocation.xPoint][agentLocation.yPoint] = new AgentEnvironment(1, 1, 0, agentMap[agentLocation.xPoint][agentLocation.yPoint].numberVisited+1);
	}


	//Marking dangerous
	private void MarkDangerous(ArrayList<Location> sqs)
	{
		for (int i=0; i<sqs.size(); i++)
			if(agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint].safePosition==0)
				agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint] = new AgentEnvironment(0, 0, 1, 0);
	}

	private ArrayList<Integer> GetVisitedInds(ArrayList<int[]> data)
	{
		ArrayList<Integer> inds = new ArrayList<Integer>();
		for(int i=0; i<data.size(); i++)
		{
			int temp[] = data.get(i);
			if(temp[0]==1&&temp[2]==0&&temp[1]<=5)
				inds.add(i);
		}
		return inds;
	}

	
	ArrayList<Location> SelectSquares(ArrayList<Location> locations, ArrayList<Integer> safeIndexLocations)
	{
		ArrayList<Location> squares = new ArrayList<Location>();
		for(int i=0; i<safeIndexLocations.size(); i++)
			squares.add(locations.get(safeIndexLocations.get(i)));
		return squares;
	}


	private ArrayList<int[]> SelectData(ArrayList<int[]> locationdata, ArrayList<Integer> safe_inds)
	{
	    ArrayList<int[]> data = new ArrayList<int[]>();
		for(int i=0; i<safe_inds.size(); i++)
			data.add(locationdata.get(safe_inds.get(i)));
		return data;
	}


	ArrayList<Integer> GetSafeInds(ArrayList<int[]> data)
	{
	   ArrayList<Integer> inds = new ArrayList<Integer>();
		for(int i=0; i<data.size(); i++)
		{
			int temp[] = data.get(i);
			if(temp[0]==0&&temp[2]==0)
				inds.add(i);
		}
		return inds;
	}


	ArrayList<int[]> GatherData(ArrayList<Location> sqs)
	{
	    ArrayList<int[]> data = new ArrayList<int[]>();
		for (int i=0; i<sqs.size(); i++)
		{
			data.add(new int[] {agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint].visitedLocation,agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint].numberVisited,
					            agentMap[sqs.get(i).xPoint][sqs.get(i).yPoint].dangerousPlace,getDirectionOfLocation(sqs.get(i)),GetCostToSquare(sqs.get(i))});
		}

		return data;
	}


    int GetCostToSquare(Location sq)
	{

		if(Math.abs(getDirectionOfLocation(sq) - facingDirection)==3)
		{
			return 2;
		}
		return Math.abs(getDirectionOfLocation(sq) - facingDirection)+1;
	}

	//Get the direction of location 
	int getDirectionOfLocation(Location sq)
	{
		int y = agentLocation.yPoint; int x = agentLocation.xPoint;
		if(sq.yPoint == y+1) return 1;
		if(sq.yPoint == y-1) return 3;
		if(sq.xPoint == x+1) return 0;
		if(sq.xPoint == x-1) return 2;
		return 0;
	}

	//Update model
	private ArrayList<Location> UpdateModel(TransferPercept tp)
	{
		bump = tp.getBump();
		glitter = tp.getGlitter();
		breeze = tp.getBreeze();
		stench = tp.getStench();
		scream = tp.getScream();
		
		MarkVisited();
		ArrayList<Location> sqs = GetNeighbors();
        if(!breeze && !stench && !bump && !glitter && !scream)
			MarkSafe(sqs);
        if(breeze ||stench)
			MarkDangerous(sqs);
		return sqs;
	}

	
	//Get neighbours
	private ArrayList<Location> GetNeighbors()
	{
		ArrayList<Location> sqs = new ArrayList<Location>();
		sqs.add(new Location(agentLocation.xPoint+1, agentLocation.yPoint));
		sqs.add(new Location(agentLocation.xPoint-1, agentLocation.yPoint));
		sqs.add(new Location(agentLocation.xPoint, agentLocation.yPoint+1));
		sqs.add(new Location(agentLocation.xPoint, agentLocation.yPoint-1));

		ArrayList<Location> sqs1 = new ArrayList<Location>();
		for (int i=0; i<sqs.size(); i++)
		{
			if(sqs.get(i).xPoint==-1 || sqs.get(i).xPoint > 3)
				continue;

			sqs1.add(sqs.get(i));
		}

		sqs = sqs1;
		sqs1 = new ArrayList<Location>();
		for (int i=0; i<sqs.size(); i++)
		{
			if(sqs.get(i).yPoint==-1 || sqs.get(i).yPoint > 3)
				continue;

			sqs1.add(sqs.get(i));
		}
		return sqs1;
	}

	// public method to return the agent's name
	// do not remove this method
	public String getAgentName() {
		return agentName;
	}
}
