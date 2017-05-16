
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

import java.util.*;

class AgentFunction {
	
	
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
	private Random rand;
    public String[][] internalMemoryModel = new String[4][4]; 
    private int historyOfPrevAction = -1;
    private char memoryDirectionOfAgent = 'E';
    private int memoryRow = 3;
    private int memoryColumn = 0;


	public AgentFunction()
	{
		// for illustration purposes; you may delete all code
		// inside this constructor when implementing your 
		// own intelligent agent

		// this integer array will store the agent actions
		actionTable = new int[9];
				  
		actionTable[0] = Action.NO_OP;
		actionTable[1] = Action.GO_FORWARD;
		actionTable[2] = Action.GO_FORWARD;
		actionTable[3] = Action.GO_FORWARD;
		actionTable[4] = Action.TURN_RIGHT;
		actionTable[5] = Action.TURN_LEFT;
		actionTable[6] = Action.GRAB;
		actionTable[7] = Action.SHOOT;


        rand = new Random();

        int worldSize = 4;
        for (int i = 0; i < worldSize; i++) {
            for (int j = 0; j < worldSize; j++) {
                internalMemoryModel[i][j] = ""; 
            }
        }
    }

    public boolean checkNeighbor(int row, int col) {
        if (row >= 0 && col >= 0 && row <= 3 && col <= 3 ) {
            return true;
        }
        return false;
    }

    public void setDirection(char direction) {
        this.memoryDirectionOfAgent = direction;
    }
    public void addFunction(int a, int b, ArrayList<int[]> possiblePositions)
    {
    	possiblePositions.add(new int[]{a, b});
    }

    public ArrayList<int[]> possiblePositionsFunction( ArrayList<int[]> possiblePositions)
    {
    	  if (checkNeighbor(this.memoryRow - 1, memoryColumn)) { //top
          	addFunction(this.memoryRow - 1,memoryColumn, possiblePositions);
          }
          if (checkNeighbor(this.memoryRow, memoryColumn - 1)) { //Left
          	addFunction(this.memoryRow,memoryColumn -1, possiblePositions);
          }
          if (checkNeighbor(this.memoryRow, memoryColumn + 1)) { //right
          	addFunction(this.memoryRow,memoryColumn +1, possiblePositions);
          }
          if (checkNeighbor(this.memoryRow + 1, memoryColumn)) { //bottom
          	addFunction(this.memoryRow + 1,memoryColumn, possiblePositions);   
          }

         
          return possiblePositions;
    }
    public ArrayList<int[]> possibleAgentNavigatorPosition(){

        ArrayList<int[]> possiblePositions = new ArrayList<>();
  
        return possiblePositionsFunction(possiblePositions);
    }
    public void chkNeighbourMakeOk(int a, int b)
    {
    	if (checkNeighbor(a, b)) { //top
        	makeOk(a, b, internalMemoryModel);
        }
    }
    public void makeOk(int a, int b, String[][] internalMemoryModel)
	{
		 internalMemoryModel[a][b] = "OK";
	}
    public void chkNeighborIML(int a, int b, String[][] internalMemoryModel)
    {
    	 if (checkNeighbor(a, b)) { //top
             if (internalMemoryModel[a][b] == "") {
                 internalMemoryModel[a][b] = "W?";
             }
         }
    }
    
	public void chkNeighborIMLMakeP(int a, int b, String[][] internalMemoryModel, String what)
	{
		 if (checkNeighbor(a, b)) { //top

             if (internalMemoryModel[a][b] == "") {
                 internalMemoryModel[a][b] = what;
             }
         }
	}
	
	 public void chkNeighbourIMLsafeLocs(int a, int b, String[][] internalMemoryModel, ArrayList<int[]> safelocs)
     {
     	if(checkNeighbor(a,b))
     	{
     		if(internalMemoryModel[a][b] =="OK" || internalMemoryModel[a][b]=="NW")
     		{
     			safelocs.add(new int[]{a,b});
     		}
     	}
     }
    public void updateState() {
        // Update the internal representation based on the past action and current state

        // Forward action

        if (historyOfPrevAction < 4 && historyOfPrevAction >= 1) {
          
            if (this.memoryDirectionOfAgent == 'N') {  this.memoryRow = memoryRow - 1;} 
            else if (memoryDirectionOfAgent == 'S') {  this.memoryRow = memoryRow + 1;} 
            else if (memoryDirectionOfAgent == 'E') { this.memoryColumn = memoryColumn + 1;} 
            else if (memoryDirectionOfAgent == 'W') { this.memoryColumn = memoryColumn - 1;}

            if (this.memoryColumn > 3) { this.memoryColumn = 3; }
            if (this.memoryColumn < 0) { this.memoryColumn = 0; }

            if (this.memoryRow > 3) { this.memoryRow = 3; }
            if (this.memoryRow < 0) { this.memoryRow = 0; }
        }

        // Update the direction
        else if (this.historyOfPrevAction == 4) {
            if (memoryDirectionOfAgent == 'N') setDirection('E');
            else if (memoryDirectionOfAgent == 'E') setDirection('S');
            else if (memoryDirectionOfAgent == 'S') setDirection('W');
            else if (memoryDirectionOfAgent == 'W') setDirection('N');
            
        } else if (this.historyOfPrevAction == 5) {
            if (memoryDirectionOfAgent == 'N') setDirection('W');
            else if (memoryDirectionOfAgent == 'E') setDirection('N');
            else if (memoryDirectionOfAgent == 'S') setDirection('E');
            else if (memoryDirectionOfAgent == 'W') setDirection('S');
        }


        if (!bump && (!glitter) && (!breeze) && (!stench) && !scream) {
        	//making locations as OK
        	chkNeighbourMakeOk(this.memoryRow - 1, memoryColumn);
        	chkNeighbourMakeOk(this.memoryRow , memoryColumn - 1);
        	chkNeighbourMakeOk(this.memoryRow , memoryColumn + 1);
        	chkNeighbourMakeOk(this.memoryRow + 1, memoryColumn);
        }
        chkNeighbourMakeOk(this.memoryRow, memoryColumn);

      //entire row/column updated to denote no wumpus
        if (scream == true) {
            if (memoryDirectionOfAgent == 'N' || memoryDirectionOfAgent == 'S') {
     
                for (int i = 0; i < 4; i++) {
                	
                    String piece = internalMemoryModel[i][memoryColumn];
                    if (internalMemoryModel[i][memoryColumn] == "NP") {
                        internalMemoryModel[i][memoryColumn] = "OK";
                    } else if(piece !="") {
                        internalMemoryModel[i][memoryColumn] = "NW";
                    }
                }
            }

            if (memoryDirectionOfAgent == 'W' || memoryDirectionOfAgent == 'E') {
                for (int i = 0; i < 4; i++) {
                    String piece = internalMemoryModel[memoryRow][i];
                    if (internalMemoryModel[memoryRow][i] == "NP") {
                        internalMemoryModel[memoryRow][i] = "OK";
                    } else if(piece != "" ){
                        internalMemoryModel[memoryRow][i] = "NW";
                    }

                }
            }
        }
        
        if (stench && !breeze) {
        	chkNeighborIML(this.memoryRow - 1, memoryColumn, internalMemoryModel);
        	chkNeighborIML(this.memoryRow , memoryColumn -1, internalMemoryModel);
        	chkNeighborIML(this.memoryRow , memoryColumn +1, internalMemoryModel);
        	chkNeighborIML(this.memoryRow + 1, memoryColumn, internalMemoryModel);
        }

        if (breeze && !stench) {

            if (internalMemoryModel[this.memoryRow][memoryColumn] == "") {
                internalMemoryModel[this.memoryRow][memoryColumn] = "P?";
            }
            chkNeighborIMLMakeP(this.memoryRow - 1, memoryColumn, internalMemoryModel,"P?");
        	chkNeighborIMLMakeP(this.memoryRow , memoryColumn -1, internalMemoryModel,"P?");
        	chkNeighborIMLMakeP(this.memoryRow , memoryColumn + 1, internalMemoryModel,"P?");
        	chkNeighborIMLMakeP(this.memoryRow + 1, memoryColumn, internalMemoryModel,"P?");
        
        }

        if (breeze && stench) {

            if (internalMemoryModel[this.memoryRow][memoryColumn] == "") {
                internalMemoryModel[this.memoryRow][memoryColumn] = "P?W?";
            }
           
            chkNeighborIMLMakeP(this.memoryRow - 1, memoryColumn, internalMemoryModel,"P?W?");
        	chkNeighborIMLMakeP(this.memoryRow , memoryColumn -1, internalMemoryModel,"P?W?");
        	chkNeighborIMLMakeP(this.memoryRow , memoryColumn + 1, internalMemoryModel,"P?W?");
        	chkNeighborIMLMakeP(this.memoryRow + 1, memoryColumn, internalMemoryModel,"P?W?");
        }



    }

    public ArrayList<int[]> getsafelocations(){
		/*
		  Gets the list of possible safe locations
		 */
        ArrayList<int[]> safelocs = new ArrayList<int[]>();
        chkNeighbourIMLsafeLocs(this.memoryRow - 1, memoryColumn, internalMemoryModel, safelocs);
        chkNeighbourIMLsafeLocs(this.memoryRow , memoryColumn - 1, internalMemoryModel, safelocs);
        chkNeighbourIMLsafeLocs(this.memoryRow , memoryColumn + 1, internalMemoryModel, safelocs);
        chkNeighbourIMLsafeLocs(this.memoryRow + 1, memoryColumn, internalMemoryModel, safelocs);
        chkNeighbourIMLsafeLocs(this.memoryRow, memoryColumn, internalMemoryModel, safelocs);

        return safelocs;
    }

    public char getDirection(int[] pos) {
		
        int rowdiff =  pos[0] - memoryRow;
        int coldiff = pos[1] - memoryColumn ;

        if(rowdiff == 0 && coldiff == 0){
            return 'C'; //
        }

        if(rowdiff == -1) { 
            if(memoryDirectionOfAgent == 'N') { return 'N';}
            if(memoryDirectionOfAgent == 'E') { return 'L';}
            if(memoryDirectionOfAgent == 'W') { return 'R';}
        }

        if(rowdiff == 1) {
            if (memoryDirectionOfAgent == 'E') {return 'R';}
            if (memoryDirectionOfAgent == 'W') {return 'L';}
            if (memoryDirectionOfAgent == 'S') {return 'N';}
        }

        if (coldiff == 1) { 
            if (memoryDirectionOfAgent == 'E') {
                return 'N';
            }
            if (memoryDirectionOfAgent == 'S') {
                return 'L';
            }
            if (memoryDirectionOfAgent == 'N') {
                return 'R';
            }
        }
        if(coldiff == -1){ 
            if(memoryDirectionOfAgent == 'W'){return 'N';}
            if(memoryDirectionOfAgent == 'N'){return 'L';}
            if(memoryDirectionOfAgent == 'S'){return 'R';}
        }

        return 'W'; 
    }


    public Integer[] getavailableactions(ArrayList<int[]> candpos) {

        ArrayList<Integer> avactions = new ArrayList<>();

        for(int[] pos : candpos) {
            char direction = getDirection(pos);
            switch (direction){
                case 'N': avactions.add(1);
                    break;

                case 'L': avactions.add(5);
                    break;

                case 'R' :avactions.add(4);
                    break;

                default: avactions.add(8);
                    break;

            }
        }
        Integer[] actionlist = new Integer[avactions.size()];
        return avactions.toArray(actionlist);

    }
    
    public int actionDecider(double value, int action1, int action2)
    {
    	int action;
    	double d = Math.random();
    	if(d < value)
		{ 
			action = action1;  
		}
		else
		{
			action = action2;  
		}
    	return action;
    }
    public int process(TransferPercept tp) {
    	// To build your own intelligent agent, replace
    	// all code below this comment block. You have
    	// access to all percepts through the object
    	// 'tp' as illustrated here:
    			
    	// read in the current percepts
        bump = tp.getBump();
        glitter = tp.getGlitter();
        breeze = tp.getBreeze();
        stench = tp.getStench();
        scream = tp.getScream();


        updateState();
 

        int action = 4;
        
        int arrowNumber = 0;
        
        ArrayList<int[]> safelocations = getsafelocations();
        ArrayList<int[]> availablePos = possibleAgentNavigatorPosition();
        Integer[] availableactions = getavailableactions(safelocations);
        List<Integer> arrayList = Arrays.asList(availableactions);


        if (glitter) {
            action = 6;//grab the gold
        }

        else if (this.memoryRow == 3 && this.memoryColumn == 0 && safelocations.size() == 1 && breeze) {
            action = 0;
		}
        else if ((safelocations.size() == availablePos.size() + 1) && !bump) {

           if (historyOfPrevAction == 5 || historyOfPrevAction == 4 && arrayList.contains(1)) {
                action = 1;
            } else {
            	action = actionDecider(.50, 5, 4);
            }
        }
        else if (bump) {
        	action = actionDecider(.50, 5, 4);
        }

        else if(breeze){

            if(safelocations.size() == 1){
                action = 4;
            }
            else {
                int c = rand.nextInt(arrayList.size());
                action = arrayList.get(c);
            }
        }

        else {
        	action = actionDecidingFunc(action, availablePos, arrayList, arrowNumber);   
        }

         this.historyOfPrevAction = action;
         return actionTable[action];
        }


    public int actionDecidingFunc(int action, ArrayList<int[]> availablePos, List<Integer> arrayList, int arrowNumber){
    	
    	  for (int[] p : availablePos) {
              char dir = getDirection(p);

              String str = internalMemoryModel[p[0]][p[1]];
              if(dir == 'N' && str.equals("OK") || str.equals("NW") && arrayList.contains(1)) {
                  action = 1;
              }

              else if (dir == 'N' && (str).equals("W!")) {
  				if(arrowNumber == 0) {
  					action = 7;
  					arrowNumber = 1;
  				}

  				else {

  					if(rand.nextInt(2) == 0){
  						action = 4;
  					}
  					else{
  						action = 5;
  					}
  				}
              }

              else if (getDirection(p) == 'N' && (internalMemoryModel[p[0]][p[1]]).equals("P!")) {
          
              	action = actionDecider(.25, 5, 0); 
              }

              else if (getDirection(p) == 'N' && (internalMemoryModel[p[0]][p[1]]).equals("P?W?")) {
                  if(arrowNumber == 0){
                  	action = actionDecider(.55, 7, 0);
                  }
                  else{
                  	action = actionDecider(.05, 4, 0);   
                  }

              }

              else if (getDirection(p) == 'N' && (internalMemoryModel[p[0]][p[1]]).equals("W?")) {

               
                  if(this.historyOfPrevAction == 7 && arrayList.contains(1)) {
                      action = 1;
                  }
                  else {
                      if(arrowNumber == 0) {
                      	action = actionDecider(.60, 7, 0); 
                      }
                      else{
                      	action = actionDecider(.60, 5, 0);   	
                      }
                  }
              }
           }
    	  return action;
      }
  	
    // public method to return the agent's name
    // do not remove this method
    public String getAgentName() {
        return agentName;
    }
}
