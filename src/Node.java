import java.util.*;

/** @author thui
 * Node is a node of this tree structure
 * letter is the letter the node represents
 * word is a boolean of whether the path to
 * 	this node represents a full word, i.e. this letter
 * 	node is the last letter of a word
 * height is the number of nodes away from the closest full word
 */
public class Node {

	Hashtable<Character,Node> children;
	private char letter;
	private boolean word;
	private int height;
	
	Node() {
		children = new Hashtable<Character,Node>();
	}
	
	Node (Hashtable<Character,Node >children, char letter, boolean word,int height) {
		this(letter,word,height);
		this.children = children;
	}
	
	Node (char letter, boolean word, int height) {
		this();
		this.letter = letter;
		this.word = word;
		this.height = height;
	}
	
	/** chooseLetter(int curr_length) chooses a neighboring
	 * letter node based on the height of the node. That is, the distance
	 * from either a leaf node or a letter node that ends a word.
	 * @param curr_length current number of letters in the game.
	 * @return The computer's chosen node
	 */
	
	public Node chooseLetter(int curr_length, Difficulty d) {
		
		Character key;
		Character max_key = null;
		Node n = null;
		
		// boolean that determines whether winning is a possibility
		boolean winning = false;
		ArrayList<Node> winning_nodes = new ArrayList<Node>();

		int max = 0;
		Enumeration<Character> keys = children.keys();

		while (keys.hasMoreElements()) {

			key = keys.nextElement();
			n = children.get(key);			

			int child_height = n.getHeight();
			
			// computer wants to pick a child node
			// of height 1. That is, choose a node that
			// is one node away from a word so human loses.
			// choose nodes that satisfies this condition.

			boolean choose = chooseNodebyDifficulty(d,child_height);
			
			if (choose) {
				winning_nodes.add(n);
				winning = true;
			} else if (child_height > max) {
				
				// if there are no nodes with height of 1,
				// choose the node with the largest height to 
				// prolong the game as much as possible.
				max = child_height;
				max_key = key;
			}
		}
		
		if (winning) {
			Random r = new Random();
			int random_index = r.nextInt(winning_nodes.size());
			n = winning_nodes.get(random_index);
		}
		return n;
	}
	
	/** chooseNodebyDifficulty(Difficulty d, int height)
	 * @param difficulty setting of difficulty
	 * @param height height of the node we're checking for
	 * @return a boolean ofwhether the node should be picked 
	 * 	or not based on given height
	 */
	private static boolean chooseNodebyDifficulty(Difficulty d, int height) {
		if (d == Difficulty.hard) {
			if (height == 1) {
				return true;
			}
		} else {
			if (height % 2 == 1) {
				return true;
			}
		}
		return false;
	}

	public Hashtable<Character,Node> getChildren() {
		return children;
	}

	public void setChildren(Hashtable<Character,Node> children) {
		this.children = children;
	}
	
	public void addChild(Node n){
		this.children.put(n.getLetter(), n);
	}
	
	
	public Node getChild(char letter) {
		return children.get(letter);
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public boolean isWord() {
		return word;
	}

	public void setWord(boolean word) {
		this.word = word;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
