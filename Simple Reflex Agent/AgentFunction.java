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

import java.util.Random;

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

	public AgentFunction()
	{
		// for illustration purposes; you may delete all code
		// inside this constructor when implementing your 
		// own intelligent agent

		// this integer array will store the agent actions
		actionTable = new int[8];
				  
		actionTable[0] = Action.NO_OP;
		actionTable[1] = Action.GO_FORWARD;
		actionTable[2] = Action.GO_FORWARD;
		actionTable[3] = Action.GO_FORWARD;
		actionTable[4] = Action.TURN_RIGHT;
		actionTable[5] = Action.TURN_LEFT;
		actionTable[6] = Action.GRAB;
		actionTable[7] = Action.SHOOT;
		
		// new random number generator, for
		// randomly picking actions to execute
		rand = new Random();
	}

	public int process(TransferPercept tp)
	{
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
		int action = 0;
	
		if(glitter==true) //Checking if glitter is true and grabbing the gold
		{
			action = 6;
		}
		else if (bump == false && glitter == false && breeze == false && stench == false && scream == false) 
		{
			action = 1; // moving forward if there is no blockage in terms of bump, breeze or stench and no glitter
		}
		else {

			if(bump == true)
			{
				action = 4; //Turing right if there is bump. It can be turn left too.
			}
			else if(stench==true)
			{
				double d = Math.random();  //Generating random value
				
				if(d < .05)
				{ 
					action = 7;   // kill wumpus
				}
				else
				{
					action = 0;   // no operation
				}
			}
			else if(scream == true && breeze == false)
			{
				action = 1; //move forward
			}	
			else 
			{
				action = 0;  // no operation
			}
		}
	
	    return actionTable[action];	    
	}
	
	// public method to return the agent's name
	// do not remove this method
	public String getAgentName() {
		return agentName;
	}
}