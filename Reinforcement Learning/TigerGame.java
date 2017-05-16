import java.util.*;

class TigerGame {

	private TigerGame(int timeSteps) {
		movesLeft = timeSteps;
		resetTiger();
	}
	
	private boolean openLeftDoor() {
		movesLeft--;
		if (tigerLocation!='L') {
			score+=10;
			resetTiger();
			return false;
		}
		score-=100;
		resetTiger();
		return true;
	}
	
	private boolean openRightDoor() {
		movesLeft--;
		if (tigerLocation!='R') {
			score+=10;
			resetTiger();
			return false;
		}
		score-=100;
		resetTiger();
		return true;
	}
	
	private char listen() {
		movesLeft--;
		score--;
		boolean accurate = (Math.random()<0.8);
		char ans = tigerLocation;
		if (!accurate)	if (ans=='L') 	ans='R';
				else		ans='L';
		return ans;
	}
	
	private int getScore() {
		return score;
	}
	
	private boolean gameOver() {
		return (movesLeft<=0);
	}
	
	
	
	
	
	
	
	
	private int movesLeft = 0;
	private int score = 0;
	private char tigerLocation = '?';
	
	private void resetTiger() {
		if (Math.random()<0.5)	tigerLocation='L';
		else			tigerLocation='R';
	}
	
	
	
	
	
	public static void main(String[] args) {
		AgentFunction.init();
		
		int trials = 1;
		int timesteps = 10;
		for (int i = 0; i < args.length; i+=2)
			if (args[i].equals("-trials") == true) {
				trials = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("-timesteps")) {
				timesteps = Integer.parseInt(args[i+1]);
			}
		int totalScore = 0;
		for (int i=0; i<trials; i++)
			totalScore += playGame(timesteps);
		
		System.out.println("AVERAGE SCORE = " + 1.0*totalScore/trials);
		
		AgentFunction.exit();
	}
	
	public static int playGame(int timeSteps) {
		
		
		System.out.println("----------------------------------\n");
		System.out.println("Wecome to the tiger game\n");
		
		TigerGame myGame = new TigerGame(timeSteps);
		
		AgentFunction myAgent = new AgentFunction();
		
		char listen = 'x';
		while (!myGame.gameOver()) {
			int myAction = myAgent.process(myGame.getScore(), listen=='L', listen=='R');
			if (myAction==Action.OPEN_LEFT_DOOR || myAction==Action.OPEN_RIGHT_DOOR) {
				listen = 'x';
				boolean tiger = false;
				if (myAction==Action.OPEN_LEFT_DOOR) {
					System.out.println(" > You decide to open the left door...");
					tiger = myGame.openLeftDoor();
				} else { 
					System.out.println(" > You decide to open the right door...");
					tiger = myGame.openRightDoor();
				}
				if (tiger) 
					System.out.println("There was a tiger behind that door! -1000 points");
				else
					System.out.println("Wow gold was behind the door! +100 points");
			//If they listen...
			} else if (myAction==Action.LISTEN) {
				listen = myGame.listen();
				System.out.println(" > You decide to to listen...");
				if (listen=='L')
					System.out.println("You hear a rumble behind the left door");
				else
					System.out.println("You hear a rumble behind the right door");
			} else {
				System.err.println("   CRITICAL ERROR: AgentFunction.process() must return either:");
				System.err.println("   Action.LISTEN, Action.OPEN_LEFT_DOOR, or Action.OPEN_RIGHT_DOOR\n");
				System.err.println("   Please ensure that your Agent complies with this requirement.\n");
				System.err.println("   (Leslie Kaelbling looks at you with disapproval)\n");
				System.exit(1);
			}
			System.out.println();
		}
		myAgent.cleanup(myGame.getScore());
		System.out.println("Thank you for playing the Tiger Game!");
		System.out.println("SCORE      = " + myGame.getScore());
		System.out.println("----------------------------------\n");
		return myGame.getScore();
	}
	
}
