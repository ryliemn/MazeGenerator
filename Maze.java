import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a randomized maze.
 * 
 * @author Rylie Nelson and Eric Preston
 */
public class Maze {
	
	/** Two-dimensional array of nodes to represent the maze. */
	private Node[][] maze;
	
	/** A random object used to randomly pick a path through the maze. */
	private Random rand;
	
	/** The width of the maze. */
	private int maze_width;
	
	/** The depth of the maze. */
	private int maze_depth;
	
	/** While debugging a visual representation of the maze will be displayed as it is generated. */
	private boolean isDebugOn;
	
	/**
	 * Constructor for a Maze object.
	 * 
	 * @param width The width of the maze.
	 * @param depth The depth of the maze.
	 * @param debug	True for debugging mode, false otherwise.
	 */
	public Maze(int width, int depth, boolean debug) {
		rand = new Random();
		maze_width = width;
		maze_depth = depth;
		isDebugOn = debug;
		maze = new Node[maze_depth][maze_width];
		populateArray();
		makeConnections();
		createSpanningTree(maze[0][0], null);
	}
	
	/**
	 * Populates the maze array by placing a new node in each available position.
	 */
	private void populateArray() {
		for (int i = 0; i < maze_depth; i++) {
			for (int j = 0; j < maze_width; j++) {
				maze[i][j] = new Node();
			}
		}
	}
	
	/**
	 * Adds references to each node which point to that node's neighbors in the maze object.
	 * Nodes that appear on the outer portions of the maze do not reference anything outside of
	 * the boundaries of the maze.
	 */
	private void makeConnections() {
		for (int curDepth = 0; curDepth < maze_depth; curDepth++) {
			for (int curWidth = 0; curWidth < maze_width; curWidth++) {
				if (curDepth > 0 && maze[curDepth - 1][curWidth] != null) { // if there is a node above
					maze[curDepth][curWidth].above = maze[curDepth - 1][curWidth];
				}
				if (curWidth < maze_width - 1 && maze[curDepth][curWidth + 1] != null) { // if there is a node to the right
					maze[curDepth][curWidth].right = maze[curDepth][curWidth + 1];
				}
				if (curDepth < maze_depth - 1 && maze[curDepth + 1][curWidth] != null) { // if there is a node below
					maze[curDepth][curWidth].below = maze[curDepth + 1][curWidth];
				}
				if (curWidth > 0 && maze[curDepth][curWidth - 1] != null) { // if there is a node to the left
					maze[curDepth][curWidth].left = maze[curDepth][curWidth - 1];
				}
			}
		}
	}
	
	/**
	 * Creates a spanning tree of the maze object. This method is where the path of the maze is forged.
	 * This is a recursive function that will explore every node in the maze.
	 * 
	 * @param currentNode The node that is currently being evaluated.
	 * @param prevNode	The node that was evaluated previous to the current node.
	 */
	private void createSpanningTree(Node currentNode, Node prevNode) {
		currentNode.visited = true;
		
		List<Node> unvisitedNeighbors = new ArrayList<Node>();
		
		if (currentNode.above != null && !currentNode.above.visited) { // if the above node exists and has not been visited
			unvisitedNeighbors.add(currentNode.above);
		} else if (currentNode.above != null && currentNode.above.visited && currentNode.above != prevNode) { // if the above node exists and has been visited and isn't where we came from
			currentNode.above = null;
			if (isDebugOn) {
				display();
			}
		}
		
		if (currentNode.right != null && !currentNode.right.visited) { 
			unvisitedNeighbors.add(currentNode.right);
		} else if (currentNode.right != null && currentNode.right.visited && currentNode.right != prevNode) {
			currentNode.right = null;
			if (isDebugOn) {
				display();
			}
		}
		
		if (currentNode.below != null && !currentNode.below.visited) {
			unvisitedNeighbors.add(currentNode.below);
		} else if (currentNode.below != null && currentNode.below.visited && currentNode.below != prevNode) {
			currentNode.below = null;
			if (isDebugOn) {
				display();
			}
		}
		
		if (currentNode.left != null && !currentNode.left.visited) {
			unvisitedNeighbors.add(currentNode.left);
		} else if (currentNode.left != null && currentNode.left.visited && currentNode.left != prevNode) {
			currentNode.left = null;
			if (isDebugOn) {
				display();
			}
		}
		
		while (!unvisitedNeighbors.isEmpty()) {
			Node temp = unvisitedNeighbors.get(rand.nextInt(unvisitedNeighbors.size()));
			if (!temp.visited) { //if our randomly gotten unvisited neighbor has still not been visited
				createSpanningTree(temp, currentNode);
			} else { //if it has been visited, kill this node's reference to it
				if (temp == currentNode.above) {
					currentNode.above = null;
				} else if (temp == currentNode.below) {
					currentNode.below = null;
				} else if (temp == currentNode.right) {
					currentNode.right = null;
				} else if (temp == currentNode.left) {
					currentNode.left = null;
				}
				if (isDebugOn) {
					display();
				}
			}
			unvisitedNeighbors.remove(temp);
		}
		
		
	}
	
	/** Calls on the toString() method to give a visual representation of the maze. */
	public void display() {
		System.out.println(toString());
	}
	
	/**
	 * Displays a visual representation of the maze.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[][] mazeDisp = new String[maze_width * 2][maze_depth * 2];
		
		for (int width = 0; width < maze_width * 2 + 1 ; width++) { //this loop prints the top border of the maze
			sb.append("X ");
			if (width == 0) { //creates the opening of the maze in the top left corner
				sb.append("  ");
				width++;
			}
		}
		
		sb.append("\n");
		
		for (int depth = 0; depth < maze_depth * 2; depth++) {
			sb.append("X ");
			for (int width = 0; width < maze_width * 2; width++) {
				if (depth % 2 == 0 && width % 2 == 0) { //if both these values are even then we are in a "node" spot and therefore must be a space
					mazeDisp[width][depth] = "  ";
				} else if (depth % 2 == 0 && width % 2 == 1) { //check if the nodes to the left and right of this spot are connected
					if (maze[depth / 2][width / 2].right != null && (width + 1) / 2 < maze_width && maze[depth / 2][(width + 1) / 2].left != null) {
						mazeDisp[width][depth] = "  ";
					} else {
						mazeDisp[width][depth] = "X ";
					}
				} else if (depth % 2 == 1 && width % 2 == 0) { //check if the nodes above and below are connected
					if (depth == maze_depth * 2 - 1 && width == maze_width * 2 - 2) { //creates the exit in the lower right
						mazeDisp[width][depth] = "  ";
					} else {
						if (maze[depth / 2][width / 2].below != null && (depth + 1) / 2 < maze_depth && maze[(depth + 1) / 2][width / 2].above != null) {
							mazeDisp[width][depth] = "  ";
						} else {
							mazeDisp[width][depth] = "X ";
						}
					}
				} else if (depth % 2 == 1 && width % 2 == 1) { //all odd spots will be walls of the maze
					mazeDisp[width][depth] = "X ";
				}
				sb.append(mazeDisp[width][depth]);
			}
			sb.append("\n");
		}

		sb.append("\n");
		
		return sb.toString();
	}
	
	private class Node {
		
		private Node above;
		private Node right;
		private Node below;
		private Node left;
		private boolean visited;
		
		private Node() {
			above = null;
			right = null;
			below = null;
			left = null;
			visited = false;
		}
	}

}
