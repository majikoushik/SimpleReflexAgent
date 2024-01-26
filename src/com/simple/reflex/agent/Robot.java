package com.simple.reflex.agent;

import com.simple.reflex.environment.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simple.reflex.environment.Environment;
import com.simple.reflex.environment.Position;
import com.simple.reflex.environment.Tile;
import com.simple.reflex.environment.TileStatus;

/**
 * Represents a simple-reflex agent cleaning a particular room. The robot only
 * has one sensor - the ability to retrieve the the status of all its
 * neighboring tiles, including itself.
 */
public class Robot {
	private Environment env;
	private Position previousPosition;

	/** Initializes a Robot on a specific tile in the environment. */
	public Robot(Environment env) {
		this.env = env;
	}

	/*
	 * Modify the getAction method below in order to simulate the
	 * passage of a single time-step. At each time-step, the Robot decides whether
	 * to clean the current tile or move tiles.
	 * 
	 * Your task for this Problem is to modify the method below such that the
	 * Robot agent is able to clean at least 70% of the available tiles on a given
	 * Environment. 5 out of the 10 graded test cases, with explanations on how to
	 * create new Environments, are available under the test package.
	 * 
	 * This method should return a single Action from the Action class. -
	 * Action.CLEAN - Action.DO_NOTHING - Action.MOVE_UP - Action.MOVE_DOWN -
	 * Action.MOVE_LEFT - Action.MOVE_RIGHT
	 */

	/**
	 * The implementation of getAction() aims for the robot agent to clean at least
	 * 70% of the tiles in the environment. In order to achieve this, the code starts by
	 * creating a map that stores information about the neighboring tiles. Then, the
	 * code retrieves the tiles that are above, below, left, and right of the robot's
	 * current position. 
	 * Then, a hash map is used to correlate the actions (right, above,
	 * left, below) with the tiles. The hash map makes it efficient in retrieving the action and
     * its connected value (Tile). Initially, my code prioritizes the down, right, up, left 
	 * movement as the order of steps. After this the code start checking the status of tiles.
	 * First, the code checks if the current tile that the agent is on is dirty and if it is
     * then the agent cleans that specific tile. Then, given that a tile is not dirty, meaning it is 
	 * clean or has a impassable wall. The agent is trained to do nothing at the initial
	 * moment and then an array list was created which holds all the actions that can be performed.
	 * Next, the intention is to prioritize the neighboring tiles that are dirty. When the neighboring 
	 * tiles are not dirty then it is time to care of the clean and impassable walls. Here, my implementation
	 * will prioritize the right steps and move to the right. Then, the implementation checks if there is no
	 * impassable wall on the right then a random order of cleaning the neighboring tiles is chosen. 
	 * This is done by using the shuffle method. Lastly, the code follows the random order as long as the 
	 * neighboring tile is passable. 
	 * 
	 * @return Action which refers to the action that the agent needs to perform which are move left, right, up, or down.
	 */
	public Action getAction() {
		/*
		 * This example code demonstrates the available methods and actions, such as
		 * retrieving its senses (neighbor tiles), getting the status of those tiles,
		 * and returning the different available Actions env.getNeighboringTiles(this)
		 * will return a Map with key-value pairs for each neighbor, using a String key
		 * for a Tile value. 
		 */

	
		//Responsible for getting information about the tile's neighbors and store it in a map
		Map<String, Tile> positions = env.getNeighborTiles(this);
		
		//Get the tile that the robot is currently on
		Tile self = positions.get("self");
		
		//Get the above tile of the robot's current position
		Tile above = positions.get("above"); // Either a Tile object or null
		
		//Get the below tile of the robot's current position
		Tile below = positions.get("below"); // Either a Tile object or null
		
		//Get the left tile of the robot's current position
		Tile left = positions.get("left"); // Either a Tile object or null
		
		//Get the right tile of the robot's current position
		Tile right = positions.get("right"); // Either a Tile object or null
		
		//Gets robot's current position in the grid
		Position selfPos = env.getRobotPosition(this);
	
		//Hash map with key of various actions (up, down, left, right) and value as Tile which associates
		// Actions with Tile object
		HashMap<Action, Tile> tilesMap = new HashMap<Action, Tile>();
		// i will follow down, right, up, left order to clean dirty tiles first as a
		// priority
		
		//prioritize cleaning down by moving down first
		tilesMap.put(Action.MOVE_DOWN, below);
		
		//next priority is given to the right tiles by moving right
		tilesMap.put(Action.MOVE_RIGHT, right);
		
		//next priority is given to the above tiles by moving above
		tilesMap.put(Action.MOVE_UP, above);
		
		//last priority is given to the left tiles by moving left
		tilesMap.put(Action.MOVE_LEFT, left);

		System.out.println("self: " + self);
		System.out.println("above: " + above);
		System.out.println("left: " + left);
		System.out.println("below: " + below);
		System.out.println("right: " + right);

		// getStatus will return TileStatus of the agent's current Position,
		// which can be either DIRTY, CLEAN, or IMPASSABLE

		//if the tile that the robot is currently on is dirty, then clean the tile
		if (self.getStatus() == TileStatus.DIRTY) {
			previousPosition =  new Position(selfPos.getRow(), selfPos.getCol());
			return Action.CLEAN;
		} else { // if the tile is clean or has impassable wall then go into this block - takes care of moving part

			//Initialize action to be doing nothing at the beginning = no movement 
			Action tempAction = Action.DO_NOTHING;
			
			// dirty tiles are always priority
			
			//Creates an array list of all the keys from the Tiles map = actions 
			List<Action> keySets = new ArrayList<Action>(tilesMap.keySet());
			
			//Iterate over the key sets
			for (Action keyName : keySets) {
				
				//perform null checks and then check if the neighboring tiles are dirty
				if (tilesMap.get(keyName) != null && tilesMap.get(keyName).getStatus() == TileStatus.DIRTY) {
					//action to be performed
					tempAction = keyName;
					break;
				}

			}

			// if there is no dirty tiles are found, then all the four tiles around are
			// clean or impassable
			if (tempAction == Action.DO_NOTHING) {
				
				if (tilesMap.get(Action.MOVE_RIGHT) != null && tilesMap.get(Action.MOVE_RIGHT).getStatus() != TileStatus.IMPASSABLE && (previousPosition != null && previousPosition.getCol() != selfPos.getCol() + 1)) {
					tempAction = Action.MOVE_RIGHT;
				}
				else if (tilesMap.get(Action.MOVE_DOWN) != null && tilesMap.get(Action.MOVE_DOWN).getStatus() != TileStatus.IMPASSABLE && (previousPosition != null && previousPosition.getRow() != selfPos.getRow() + 1)) {
					tempAction = Action.MOVE_DOWN;
				}
				else if (tilesMap.get(Action.MOVE_LEFT) != null && tilesMap.get(Action.MOVE_LEFT).getStatus() != TileStatus.IMPASSABLE && (previousPosition != null && previousPosition.getCol() != selfPos.getCol() - 1)) {
					tempAction = Action.MOVE_LEFT;
				}
				else if (tilesMap.get(Action.MOVE_UP) != null && tilesMap.get(Action.MOVE_UP).getStatus() != TileStatus.IMPASSABLE && (previousPosition != null && previousPosition.getRow() != selfPos.getRow() - 1)) {
					tempAction = Action.MOVE_UP;
				}
				
				//if there is impassable in the right then I will do a random order
				// else {
				// 	Collections.shuffle(keySets);
				// 	//Iterate through the keySets
				// 	for (Action keyName : keySets) {
				// 		//As long as the neighboring tile is passable then go to the random tile by performing a random action
				// 		if (tilesMap.get(keyName) != null && tilesMap.get(keyName).getStatus() != TileStatus.IMPASSABLE) {
				// 			if((keyName == Action.MOVE_RIGHT && previousPosition != null && previousPosition.getCol() != selfPos.getCol() + 1) ||
				// 			(keyName == Action.MOVE_DOWN && previousPosition != null && previousPosition.getRow() != selfPos.getRow() + 1) ||
				// 			(keyName == Action.MOVE_LEFT && previousPosition != null && previousPosition.getCol() != selfPos.getCol() - 1) ||
				// 			(keyName == Action.MOVE_UP && previousPosition != null && previousPosition.getRow() != selfPos.getRow() - 1))
				// 			{
				// 				tempAction = keyName;
				// 				break;
				// 			}						

				// 		}

				// 	}
				// }

			}
			//Return the desired action
			previousPosition =  new Position(selfPos.getRow(), selfPos.getCol());
			return tempAction;
		}

	}

	@Override
	public String toString() {
		return "Robot [neighbors=" + env.getNeighborTiles(this) + "]";
	}
}