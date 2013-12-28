import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class finds a path through a maze which contains no cycles.
 * 
 * @author Rylie Nelson and Eric Preston
 */
public class MazeSolver {
	
	/** Stores the representation of the maze in a two-dimensional array of Strings. */
	private String[][] mazeArray;
	
	/** The height of the maze. */
	private int mazeHeight = 0;
	
	/** The width of the maze. */
	private int mazeWidth = 0;
	
	/**
	 * The constructor for the maze.
	 * @param filename The name of the file that contains a representation of a maze.
	 */
	public MazeSolver(String filename) {
		String mazeString = readFile(filename); 
		createArrayOfMaze(mazeString);
		solveMaze(0, 1);
	}
	
	/**
	 * Reads a file and returns a single string representation of that file.
	 * 
	 * @param filename The name of the file to be read.
	 * @return a string representation of the file.
	 */
	private String readFile(String filename) {
		File file = new File(filename);
		Scanner reader = null;
		try {
			reader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		reader.useDelimiter("");
		
		String theMaze = "";
		
		mazeHeight++;
		
		while (reader.hasNext()) {
			String temp = reader.next();
			if (temp.equals("\n")) {
				mazeHeight++;
				mazeWidth = 0;
			} else if (temp.equals("X") || temp.equals(" ")) {
				mazeWidth++;
			}
			theMaze += temp;
		}
		
		mazeWidth = mazeWidth / 2;
		
		reader.close();
		
		return theMaze;
	}
	
	/**
	 * First this method removes all newline characters, then it places the string representation of the maze
	 * into a two-dimensional array.
	 * 
	 * @param mazeString The string to be processed.
	 */
	private void createArrayOfMaze(String mazeString) {
		for (int i = 0; i < mazeString.length(); i++) { //removes all instances of newline characters, as they disrupt the process of the loop below
			if (mazeString.charAt(i) == '\n') {
				mazeString = mazeString.substring(0, i) + mazeString.substring(i + 1);
			}
		}
		
		mazeArray = new String[mazeHeight][mazeWidth];
		for (int height = 0; height < mazeHeight; height++) {
			for (int width = 0; width < mazeWidth; width++) {
				if (!mazeString.isEmpty()) {
					mazeArray[height][width] = mazeString.substring(0, 2);
					mazeString = mazeString.substring(2);
				}
			}
		}
	}
	
	/**
	 * A recursive function that determines the path through the maze. 
	 * 
	 * @param row The row of the maze.
	 * @param column The column of the maze.
	 * @return True when the function reaches the end of the maze. False otherwise.
	 */
	private boolean solveMaze(int row, int column) {
		mazeArray[row][column] = "+ ";
		if (row == mazeHeight - 1 && column == mazeWidth - 2) { //base case, we have found the exit
			return true;
		}
		
		boolean mazeFound = false;
		
		if (row - 1 >= 0 && mazeArray[row - 1][column].equals("  ")) { //if above is valid to visit
			mazeFound = solveMaze(row - 1, column);
		}
		if (column + 1 < mazeWidth && mazeArray[row][column + 1].equals("  ") && !mazeFound) { //if right is valid to visit
			mazeFound = solveMaze(row, column + 1);
		}
		if (row + 1 < mazeHeight && mazeArray[row + 1][column].equals("  ") && !mazeFound) { //if below is valid to visit
			mazeFound = solveMaze(row + 1, column);
		}
		if (column - 1 >= 0 && mazeArray[row][column - 1].equals("  ") && !mazeFound) { //if left is valid to visit
			mazeFound = solveMaze(row, column - 1);
		}
		
		if (mazeFound) {
			//mazeArray[row][column] = "+ ";
		} else {
			mazeArray[row][column] = "  ";
		}
		
		return mazeFound;
	}
	
	/**
	 * Prints a graphical representation of the maze as it is being solved.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int height = 0; height < mazeHeight; height++) {
			for (int width = 0; width < mazeWidth; width++) {
				sb.append(mazeArray[height][width]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
