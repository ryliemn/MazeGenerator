
public class Main {

	/**
	 * The main method used to test Maze.java and MazeSolver.java.
	 * 
	 * @param args Unused by this implementation.
	 */
	public static void main(String[] args) {
		// generate a maze with n by m cells
		
		Maze maze = new Maze(5,5, true);
		maze.display();
		maze = new Maze(5,5, false);
		maze.display();
		
		maze = new Maze(15,10, false);
		maze.display();
		
		MazeSolver solver = new MazeSolver("mazeTest.txt");
		System.out.println(solver);
	}

}
