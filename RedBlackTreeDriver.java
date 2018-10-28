/*
Name of the Student: Siang Swee Kong
	Class: SE 3345
	Section: 003
	Semester: Fall 2018
	Project: 4
	Description: A program that implements a generic red black tree that only support insertion operation.
	This program will read from an input file and perform the desired operations and write the results to an output file.  
	However, this red black tree only accept String and Integer object.
*/

import java.io.*;
import java.util.Scanner;

/**A driver program for red black tree that takes two command line arguments, the first argument is the input file name, and the second argument is the output file name.
* @author Siang Swee Kong
* @version 1.0.0
*/
public class RedBlackTreeDriver {
	public static void main(String[] args) { 
		try {

			String inputFileName = args[0];		//store the input file name
			File file = new File(inputFileName);
			Scanner scan1 = new Scanner(file); 

			String outputFileName = args[1];   //store the output file name
			FileWriter fileWriter = new FileWriter(outputFileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			String method = ""; 
			method = scan1.nextLine(); //read the first line from the input file to decide the data type that is stored in the red black tree

			if (method.equals("Integer")) 
			{
				RedBlackTree<Integer> t1 = new RedBlackTree<Integer>();//create an instance of RedBlackTree that store Integer object
				int num = 0;
				while (scan1.hasNextLine())  //read until the end of the file
				{
					method = scan1.nextLine();	//read until the end of the line
					String [] str = method.split(":"); //split the string into two string when : is found in the string
					if(str[0].equals("Insert") && str.length == 2) //if the line that read from the file begins with "Insert:"
					{
						try {
							String temp = str[1]; //store the rest of the line
							num = Integer.valueOf(str[1]); //convert the string to int
							printWriter.printf("%b%n",t1.insert(num)); //insert the element
						}
						catch(NumberFormatException ex) //catch the element that has wrong data type
						{
							printWriter.printf("Error in Line: %s:%s%n", str[0],str[1]);
						}
						catch(NullPointerException ex) //throw exception when the given element is null
						{
							printWriter.printf("Error in insert: NullPointerException raised. %s%n",ex.getMessage());
						}
					}

					else if(str[0].equals("PrintTree")) //if the line that read from the file begins with "PrintTree"
					{
						printWriter.printf("%s%n",t1.toString()); //print the tree with preorder traversal
					}

					else if(str[0].equals("Contains")&& str.length == 2) //if the line that read from the file begins with "Contains:"
					{
						try
						{
							String temp = str[1]; //store the rest of the line
							num = Integer.valueOf(str[1]); //convert the string to int
							printWriter.printf("%b%n",t1.contains(num)); //find the element
						}
						catch(NumberFormatException ex) //catch the element that has wrong data type
						{
							printWriter.printf("Error in Line: %s:%s%n", str[0],str[1]);
						}
						catch(NullPointerException ex)  //throw exception when the given element is null
						{
							printWriter.printf("Error in contains: NullPointerException raised. %s%n",ex.getMessage());
						}
					}
					else //any invalid operations
						printWriter.printf("Error in Line: %s%n", str[0]);
				}
			}

			else if (method.equals("String")) 
			{
				RedBlackTree<String> t1 = new RedBlackTree<String>();//create an instance of RedBlackTree that store String object
				while (scan1.hasNextLine())  //read until the end of the file
				{
					method = scan1.nextLine();	//read until the end of the line
					String [] str = method.split(":"); //split the string into two string when : is found in the string
					if(str[0].equals("Insert") && str.length == 2) //if the line that read from the file begins with "Insert:"
					{
						try {
							String temp = str[1]; //store the rest of the line
							printWriter.printf("%b%n",t1.insert(temp)); //insert the element
						}
						catch(NullPointerException ex) //throw exception when the given element is null
						{
							printWriter.printf("Error in insert: NullPointerException raised. %s%n",ex.getMessage());
						}
					}

					else if(str[0].equals("PrintTree")) //if the line that read from the file begins with "PrintTree"
					{
						printWriter.printf("%s%n",t1.toString()); //print the tree with preorder traversal
					}

					else if(str[0].equals("Contains")&& str.length == 2) //if the line that read from the file begins with "Contains:"
					{
						try
						{
							String temp = str[1]; //store the rest of the line
							printWriter.printf("%b%n",t1.contains(temp)); //find the element
						}
						catch(NullPointerException ex) //throw exception when the given element is null
						{
							printWriter.printf("Error in contains: NullPointerException raised. %s%n",ex.getMessage());
						}
					}
					else //any invalid operations
						printWriter.printf("Error in Line: %s%n", str[0]);
				}
			}
			
			else //If the object is not Integer and not String
				printWriter.printf("Cannot work with the object %s. Only works for objects Integer and String.%n",method);
				
		
			scan1.close(); //close the input file
			printWriter.close(); //close the output file
		}
		catch (IOException ex){ //catch the exception when the file is not found
			System.out.println("File not found.");
		} 
		catch (ArrayIndexOutOfBoundsException ex){
			System.out.println("Missing command line arguments.");
		} 

		System.exit(0); //exit the program
	}
}
