import java.io.*;
import java.util.Scanner;

enum Difficulty {
	normal, hard
}
/**
 * Ghost is a word game that requires the player
 * to name letters of a word. In this version with two
 * difficulty levels, you play against the computer.
 * 
 * http://en.wikipedia.org/wiki/Ghost_(game)
 * @author thui
 *
 */

public class Ghost {

	private static Node root;
	private static Node curr_node;
	private static Difficulty difficulty;
	
	// boolean turn determines whether it's your turn
	private static boolean turn = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		root = new Node();		
		try {
			setup();
			play();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void play() {
		String word = "";
		String letter = "";
		int result = 0;
		int length = 0;
		boolean game = false;
		curr_node = root;
		Scanner s = new Scanner(System.in);
		
		System.out.println("GHOST: endless fun with words!");

		printASCIIArt();
		
		difficulty(s);
		
		// escape the game if the input letters are not part of any word
		// or game ends (with whole word being detected and length
		// of word is more than 3 letters)
		while(game == false) {

			if (turn) {
				System.out.println("\nEnter a letter: ");
				letter = s.next();
			} else {
				System.out.println("Computer's turn..");
				Node n = curr_node.chooseLetter(length,difficulty);
				letter = Character.toString(n.getLetter());
			}
			
			// check if user entered invalid input
			if (letter.length() > 1 || !letter.matches("[a-zA-Z]")) {
				System.out.println("Your input was invalid. Try again");
			}  else {
				result = checkLetter(letter);
				if (result != 2) word = word + letter;
				length = word.length();
				game = gameEnd(result,length);
			}
						
			System.out.println("Letters: " + word);
		}
	}
	
	/** gameEnd(int result, int length) determines whether the game
	 * should continue or end.
	 * @param result integer result of checkLetter function
	 * @param length the length of the input string so far
	 * @return a boolean determining whether game should end.
	 */
	public static boolean gameEnd(int result, int length) {
		
		boolean ret = false;
		if (result == 2) {
			
			// if input string is less than 4 letters
			// and human already has entered gibberish,
			// give the human another chance..
			if (length < 4) {
				System.out.println("Ain't no word start like that. Try again");
				turn = true; 
				ret =  false;
				
			}else {
				System.out.println("There's no word with that spelling! You lose");
				return true;
			}
			
		}
		
		// if input string is just part of a word and not a word..
		if (result == 0){
			turn = !turn;
			ret = false;
		}

		// if input string is a word and more than 3 letters..
		if (result == 1 && length > 3) {
			if (turn)
				System.out.println("You spelled a word! YOU LOSE!");
			else
				System.out.println("You win!");
			return true;
		}
		return ret;
	}
	
	
	public static void difficulty(Scanner s) {
		System.out.println("Enter a difficulty (normal or hard): ");
		String difficulty = s.next();
		
		if (difficulty.matches("normal|hard")) {
			setDifficulty(difficulty);
		}else {
			System.out.println("You entered something invalid: " + difficulty);
			difficulty(s);
		}
	}

	public static void setDifficulty(String d) {
		if (d.compareTo("normal") == 0) difficulty = Difficulty.normal;
		else if (d.compareTo("hard") == 0) difficulty = Difficulty.hard;
	}
	
	/** checkLetter(String string, Node node) 
	 * checks and traverses through tree for the 
	 * game's letters thus far. 
	 *  @return 3 possible integers
	 * 0 if the input string is not a word, but part of a word
	 * 1 if the input string is a word
 	 * 2 if the input string is neither a word or part of a word
	 **/
	public static int checkLetter(String string) {
		if (string.length() > 1) return -1;
		char c = string.charAt(0);
		Node n = curr_node.getChild(c);
		
		if (n == null) {
			return 2;
		} else {
			
			// update currently checked node
			curr_node = n;

			if (n.isWord() == true) 
				return 1;
			else
				return 0;
		}
	}

	public static void setup() throws IOException {
		String path = "WORD.LST";
		FileReader fr = new FileReader(path);
		BufferedReader textreader = new BufferedReader(fr);
		String line;
		while( (line = textreader.readLine()) != null) {
			boolean word = false;
			int length = line.length();
			char c = line.charAt(0);
			
			// establish root node for word, has a height of length - 1
			Node parent = addNodeorTraverse(root,c,word,length-1);
			Node prev = parent;
			
			for(int i = 1; i < length; i++) {
				if (i == length - 1) word =  true;
				prev = addNodeorTraverse(prev,line.charAt(i), word, length - i - 1);
			}
		}		
	}
	
	// checks whether the parent node contains the searched child
	// with the given character as key
	public static Node addNodeorTraverse(Node parent, char c, boolean word, int height) {
		Node child = null;
		if ((child = parent.getChild(c)) == null) {
			Node n = new Node(c,word,height);
			parent.addChild(n);
			return n;
		} else {
			
			// replace the height of child if the currently added word
			// has a smaller length.
			if (height < child.getHeight()) child.setHeight(height);
			return child;
		}
	}
	
	// just for kicks
	public static void printStats() {
	    System.out.println("Total memory (bytes): " +
	      Runtime.getRuntime().totalMemory());
	    
	    long maxMemory = Runtime.getRuntime().maxMemory();
	    
	    System.out.println("Maximum memory (bytes): " + 
	      (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));	
	}
	
	public static void printASCIIArt() {
		System.out.println( 
				"               _.-, 					\n" +
				"          _ .-'  / .._					\n" +
				"        .-:'/ - - \\:::::-.			\n" +
				"      .::: '  e e  ' '-::::.			\n" +
				"     ::::'(    ^    )_.::::::			\n" +
				"    ::::.' '.  o   '.::::'.'/_			\n" +
				".  :::.'       -  .::::'_   _.:		\n" +
				".-''---' .'|      .::::' '''::::		\n" + 
				"'. ..-:::'  |    .::::'     ::::		\n" +
				"'.' ::::    \\ .::::'       ::::		\n" +
				"   ::::   .::::'           ::::		\n" +
				"    ::::.::::'._          ::::			\n" +
				"     ::::::' /  '-      .::::			\n" +
				"      '::::-/__    __.-::::'			\n" +
				"        '-::::::::::::::-'				\n" +
				"            '''::::'''					");
		
	}
}
