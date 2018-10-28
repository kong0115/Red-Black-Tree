/**Implements a generic version of simplified red black tree that supports insertion
 * @author Siang Swee Kong
 * @version 1.0.0
 */
public class RedBlackTree<E extends Comparable<E>> {
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	private static final boolean ZIGZIG = false;
	private static final boolean ZIGZAG = true;
	private Node<E> root;

	/**
	 *	Initialize the attributes with default constructor 
	 */
	public RedBlackTree() 
	{
		root = null; 
	}

	/**
	 * Test if the tree is empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty( )
	{
		return root == null;
	}

	//nested node class
	private static class Node<E extends Comparable<E>>
	{
		public E element;  
		public Node<E> leftChild; 
		public Node<E> rightChild;	
		public Node<E> parent;
		public boolean color;

		/**
		 * Constructor that take 5 arguments to initialize the attributes.
		 * @param key the data.
		 * @param left the left child 
		 * @param right	the right child
		 * @param p the parent
		 * @param c value to indicate if the node is red or black
		 */
		public Node(E key, Node<E> left, Node<E> right, Node<E> p, boolean c) 
		{
			element = key; leftChild = left; rightChild = right; parent = p; color = c;
		}
	}

	/**
	 * Insert into the tree.
	 * @param element the item to insert.
	 * @return true if insertion is successful
	 * @return false if insert a duplicate element
	 * @throws NullPointerException if the given element is null
	 */
	public boolean insert(E element)
	{
		if(element == null)
			throw new NullPointerException("The given element is null.");

		if(contains(element) == true) //insert duplicate
		{
			return false;
		}

		if(root == null) //insert an element into an empty tree
		{
			root = new Node<E>(element,null,null,null, BLACK);
		}

		else  //insert an element into a non-empty tree
			insert(element,root);

		//force the root to be black
		if(root.color == RED)
		{
			root.color = BLACK;
		}

		return true;
	}

	/**
	 * Internal method to insert into a non-empty tree.
	 * @param element the item to insert.
	 * @param node the target node
	 * @return update the tree after re-balancing.
	 */
	private void insert(E element, Node<E> node)
	{
		boolean flag = false;
		Node<E> current = root;
		Node<E> parent = root;
		while(current != null) //find the right spot to insert the element
		{
			if(element.compareTo(current.element) < 0) //go to left subtree
			{
				parent = current;
				current = current.leftChild;
			}
			else   //go to right subtree
			{
				parent = current;
				current = current.rightChild;
			}
		}
		current = new Node<E>(element,null,null,parent, RED); //insert the new element to the tree
		if(element.compareTo(parent.element) < 0)//update the parent
		{
			parent.leftChild = current; 
		}
		else 
			parent.rightChild = current;

		flag = isDoubleRed(current); //check for double red condition
		while(flag == true) //re-balance and update the tree
		{
			Node<E> ggp = current.parent.parent.parent;
			Node<E> temp = rebalance(current);
			if(ggp == null)
			{
				root = temp;
			}
			else if(current.element.compareTo(ggp.element) < 0){
				ggp.leftChild = temp;
			}
			else {
				ggp.rightChild = temp;
			}

			flag = isDoubleRed(temp);
			current = temp;
		}
	}

	/**
	 * Internal method to check if the color of the node and its parent are red. 
	 * @param node the target node
	 * @return true if there is a double red condition
	 * @return false if it does not have a double red condition
	 */
	private boolean isDoubleRed(Node<E> node)
	{
		if(node.parent!=null) //if the node has a parent
			if(node.color == RED && node.parent.color == RED)
			{
				return true;
			}
		return false;
	}

	/**
	 * Internal method to balance the node depends on the color of the uncle node and the case type. 
	 * @param node the target node
	 * @return the new balanced subtree.
	 */
	private Node<E> rebalance(Node<E> node) 
	{
		boolean uColor;
		if(getUncleNode(node) == null) //null node is black
			uColor = BLACK;
		else
			uColor = getUncleNode(node).color; //get the color of the uncle node
		if(uColor == BLACK) //case 1 when the uncle node is black, restructuring
		{
			boolean cType = caseType(node);
			if(cType == ZIGZIG) //zigzig type
			{
				Node<E> parent = node.parent;
				Node<E> grandparent = node.parent.parent;
				grandparent.color = RED;
				parent.color = BLACK;
				if(node == parent.rightChild) //right-right case
					node = singleRotateWithRightChild(grandparent);
				else //left-left case
					node = singleRotateWithLeftChild(grandparent);
			}
			else if(cType == ZIGZAG)//zigzag type
			{
				Node<E> parent = node.parent;
				Node<E> grandparent = node.parent.parent;
				grandparent.color = RED;
				node.color = BLACK;
				if(parent.leftChild == node) //right-left case
					node = doubleRightLeft(grandparent);
				else //left-right case
					node = doubleLeftRight(grandparent);	
			}
		}

		if(uColor == RED) //case 2 when the uncle node is red, re-coloring
		{ 
			Node<E> parent = node.parent;
			Node<E> grandparent = node.parent.parent;
			Node<E> uncle = getUncleNode(node);		
			parent.color = BLACK;
			uncle.color = BLACK;
			grandparent.color = RED;
			node = grandparent;
		}

		return node;
	}

	/**
	 * Internal method to get the parent sibling 
	 * @param node the target node
	 * @return the uncle node
	 */
	private Node<E> getUncleNode(Node<E> node)
	{
		Node<E> grandparent = node.parent.parent;
		if(node.element.compareTo(grandparent.element) < 0)
		{
			return grandparent.rightChild;
		}
		else
			return grandparent.leftChild;
	}

	/**
	 * Internal method to decide the ZIGZIG case or ZIGZAG case
	 * @param node the target node
	 * @return a boolean value to indicate the case type
	 */
	private boolean caseType(Node<E> node)
	{
		Node<E> parent = node.parent;
		Node<E> grandparent = node.parent.parent;
		if(node.element.compareTo(parent.element) < 0 && node.element.compareTo(grandparent.element) < 0)
			return ZIGZIG;
		if (node.element.compareTo(parent.element) > 0 && node.element.compareTo(grandparent.element) > 0)
			return ZIGZIG;

		return ZIGZAG;
	}

	/**
	 * Internal method to do the single rotation for left-left case
	 * @param node the target node
	 * @return the root of the updated subtree
	 */
	private Node<E> singleRotateWithLeftChild( Node<E> k2 )
	{

		Node<E> k1 = k2.leftChild;
		k2.leftChild = k1.rightChild;
		k1.rightChild = k2;
		k1.parent = k2.parent;
		k2.parent = k1;
		return k1;
	}

	/**
	 * Internal method to do the single rotation for right-right case
	 * @param node the target node
	 * @return the root of the updated subtree
	 */
	private Node<E> singleRotateWithRightChild( Node<E> k1 )
	{
		Node<E> k2 = k1.rightChild;
		k1.rightChild = k2.leftChild;
		k2.leftChild = k1;
		k2.parent = k1.parent;
		k1.parent = k2;
		return k2;
	}

	/**
	 * Internal method to do the double rotation for right-left case
	 * @param node the target node
	 * @return the root of the updated subtree
	 */
	private Node<E> doubleRightLeft( Node<E> k1 )
	{
		k1.rightChild = singleRotateWithLeftChild( k1.rightChild );
		return singleRotateWithRightChild( k1 );
	}

	/**
	 * Internal method to do the double rotation for left-right case
	 * @param node the target node
	 * @return the root of the updated subtree
	 */
	private Node<E> doubleLeftRight( Node<E> k3 )
	{
		k3.leftChild = singleRotateWithRightChild( k3.leftChild );
		return singleRotateWithLeftChild( k3 );
	}

	/**
	 * Find an element in the tree.
	 * @param object the element to search for.
	 * @return true if the item is found in the tree.
	 * @return false if the item is not found in the tree.
	 */ 
	public boolean contains(E object)
	{
		return contains( object, root );
	}

	/**
	 * Internal method to find an element in the tree.
	 * @param object the element to search for.
	 * @param n the node that roots the subtree.
	 * @return true if the item is found in the tree.
	 * @return false if the item is not found in the tree.
	 */
	private boolean contains( E object, Node<E> n )
	{
		if( n == null ) //not found
			return false;

		if( object.compareTo( n.element ) < 0 )
			return contains( object, n.leftChild );
		else if( object.compareTo( n.element ) > 0 )
			return contains( object, n.rightChild );
		else
			return true;    //found
	}

	/**
	 * Method to concatenate all the element in the tree using pre-order traversal.
	 * An element located in a red node should be preceded by a single asterisk.
	 * An element located in a black node should not be preceded by a single asterisk.
	 * @return a string that contains all the elements using pre-order traversal. Return an empty tree message if the tree is empty.
	 */
	public String toString( )
	{
		String output = "";
		if( isEmpty( ) )
			output = "Empty tree";
		else
		{
			output = toString( root, output);
		}

		return output;
	}

	/**
	 * Internal method to concatenate all the element in the tree using pre-order traversal.
	 * Every pair of adjacent elements should be separated by whitespace in the printing.
	 * @param n the node that roots the subtree.
	 * @param temp a variable to store the element into a string
	 * @return a string that contains all the elements using pre-order traversal
	 */
	private String toString( Node<E> n, String temp)
	{
		if(n.color == RED)
		{
			temp = "*" + String.valueOf(n.element) + " ";
		}

		else if(n.color == BLACK)
		{
			temp = String.valueOf(n.element) + " ";
		}

		if( n.leftChild != null )
		{
			temp =  temp + toString(n.leftChild, temp);
		}

		if(n.rightChild != null)
		{
			temp =  temp + toString(n.rightChild, temp);
		}

		return temp;
	}
}
