import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class CPSC331_Ass3 {

	// input and its reverse
	public static String logExp, logExpRev;
	
	// counters
	public static short counter, counter2, row, column;
	
	// stack to identify the subexpressions
	public static Stack<Character> logExpStack;
	
	// hashmap to store the subexpressions
	// 2nd one is to store them with the dictionary names
	// from the dictionary #1
	public static HashMap<String, String> dictionary, dictionary2;
	
	// list to keep the independent variables
	public static LinkedList<Character> indVarList;

	// list to keep the first row of the full truth table
	public static LinkedList<String> firstRow;
	
	// 2D array to keep the full truth table
	public static String [][] twoDimArray;
	
	// Case 1: -()	Case 2: -a(first)	Case3: -a(second)	Case4: -a(first) & -a(second)
	public static boolean case1 = false, case2 = false, case3 = false, case4 = false;
	
	/*
	 * This function takes a string in the form of a subexpression (length 3)
	 * Eg: a+c
	 * Then separates it into its ind. var's and symbol and checks the conditions
	 * (checks specifically for "or" or "and" and returns a boolean for T or F)
	 * if the taken subexpression is true or not, and returns the result as 'T' or 'F' 
	 */
	public static String checkTruth(String truth){
		char a = truth.charAt(0);
		char b = truth.charAt(1);
		char c = truth.charAt(2);
		
		boolean A = (a == 'T' ? true : false);
		boolean C = (c == 'T' ? true : false);
		
		boolean bool = (b == '+' ? A || C : A && C);
		String result = (bool == true ? "T" : "F");
		
		return result;
	}
	
	
	// Main function where everything is calculated
			public static void main(String[] args) {	
				// Importing the Scanner for user input
				Scanner inp = new Scanner(System.in);		
				
				
				/////////////////////////////////////////////////////////////////////
				//////////////////////  S1 - Entering the LE   //////////////////////
				/////////////////////////////////////////////////////////////////////
				
				/*
				===== Precondition: =====
				* Two strings that are at least length of 3 Eg:"(a)"
				* The entered characters must be elements of: 
				* 	1-) English Alphabet Letters or 
				* 	2-) Left and Right Parenthesis
				* 	3-) (*, +, -)
				* Input characters must be at least element of 1 and 2, at most 1, 2, 3
				===== Postcondition: =====
				* Print/+Ask for the string and get input from the user
				* Keep the user inputs in a string that is to be used later
				*/
				
				// User inputs their desired two decimal values (0-9 and . can be entered)
				System.out.println("Enter your logical expression");
				logExp = inp.nextLine();
				logExp = logExp.toUpperCase();	// Just for a better look
				
				
					/////////////////////////////////////////////////////////////
				//////////////////////  -- Initializations --  //////////////////////
					/////////////////////////////////////////////////////////////
				
				// initializations
				indVarList = new LinkedList<Character>();
				logExpStack = new Stack<Character>();
				dictionary = new HashMap<String, String>();
				dictionary2 = new HashMap<String, String>();
				
				// Calculate the lengths of the inputs so that you can loop through
				short inputLength = (short) logExp.length();

				// Search #1
//				for (counter = 0; counter < inputLength; counter++){
//					if(Character.isLetter(logExp.charAt(counter)) && !indVarList.contains(logExp.charAt(counter))){
//						indVarList.add(logExp.charAt(counter));
//					}		
//				}
//				
//				// Search #2
////				for (counter = 0; counter < inputLength; counter++){
////					if(Character.isLetter(logExp.charAt(counter)) && (indVarList.indexOf(logExp.charAt(counter)) == -1)){
////						indVarList.add(logExp.charAt(counter));
////					}		
////				}
				
				/////////////////////////////////////////////////////////////////////
				//////////////////////  		PART 1		   //////////////////////
				/////////////////////////////////////////////////////////////////////
				
				// Search #3
				
				/* 
				 * Every time you check a character in the input string, check if it's a letter
				 * If it's a letter, then check the indVarList's elements until you find the same
				 * char, if you can't add the letter to the list
				 * (If the char is in the list, set the "contains" boolean true, meaning there is 
				 * a char that's the same inside, thus only condition to add a char is to "contains"
				 * to be false
				*/ 
				boolean contains = false;
				for (counter = 0; counter < inputLength; counter++){
					if(Character.isLetter(logExp.charAt(counter))){
						for(counter2 = 0; counter2 < indVarList.size(); counter2++){
							if (logExp.charAt(counter) == indVarList.get(counter2)){
								contains = true;
								break;
							}
						}
						if (contains == false) {
							indVarList.add(logExp.charAt(counter));
						}
					}
					contains = false;
				}
				
				// This can be done with a for loop but since there is no restriction, I used this
				// for loop (loop from last to first and add it to the new string, and replace that with this)
				logExpRev = new StringBuilder(logExp).reverse().toString();
				
				// add all the characters of the input to the stack
				// Note: reverse+reverse = normal, so the string is in proper order
				for (counter = 0; counter < inputLength; counter++){
					logExpStack.push(logExpRev.charAt(counter));
				}

				// Initializations of some temporary strings and a char
				// To be explained within the loops
				String tempStr = "";
				String subExp = "";
				String subExpRev = "";
				String subExp2 = "";
				String tempHolder = "";	
				Character tempPop = ' ';
							
				counter2 = 0;
				
				/////////////////////////////////////////////////////////////////////
				//////////////////////  		PART 2		   //////////////////////
				/////////////////////////////////////////////////////////////////////
				
				/* 
				 * Loop through as many times as the length of the string
				 * In every loop, check the size of the stack to not get an error
				 * If not empty, then pop an element and add keep it in the temp char
				 * Until you reach a ')', keep adding the popped chars to a temp str
				 * When you reach a ')', do the following:
				 	* Increment the counter2 
				 		* (this is used for dictionary addings, since we want to keep them
				 		* as LE1, LE2, etc. starting from #1, this will take care of that)
				 	* subExp is the substring that will contain the subexpression within the
				 	* last '(' and first ')' (exclusive)
				 		* (since we are in the else case, ')' and later part is not included)
				 	* subExpRev is for the reverse of it 
				 		* since a+b and b+a are the same logic
				 */
				for (counter = 0; counter < inputLength; counter++){
					if(logExpStack.size() != 0){
						tempPop = logExpStack.pop();
					}
					if(tempPop != ')'){
						tempStr += tempPop;
					} else {
						counter2++;
						
						subExp = tempStr.substring(tempStr.lastIndexOf('(')+1);
						subExpRev = new StringBuilder(subExp).reverse().toString();
						
						/*
						 * Search algorithm that checks if the subexpression is already inside the dictionary
						 * Check the dictionary's elements and compare them with the current subexpression,
						 * if they are equal (reverse or regular one), move to else
						 */
						
						// IMPORTANT
						// whichLE: this value gives the which dictionary # the current "same" subExp belongs to,
						// we need this because if in the future we wanted to replace the 2-3 step older subExp,
						// counter2 wont suffice since it's the counter for how many elements will be in the 
						// dictionary
						
						short whichLE = 0;
						contains = false;
						for(short tempCounter = 1; tempCounter < dictionary.size()+1; tempCounter++){							
							whichLE++;
							if (dictionary2.get("LE" + tempCounter).equals(subExp) || dictionary2.get("LE" + tempCounter).equals(subExpRev)){
								contains = true;
								break;
							}
						}
						/*
						 * If (the subExp is in the dictionary):
						 	* keep the tempStr with the current same subExp without its parenthesis
						 		* Eg: (LE1+A+B
						 	* decrement counter2 
						 		* since it's not a new dictionary variable, we go back to
						 		*  the last dictionary value's number
						 	* Since the same subExp, push it back to stack as "LE#" ("#,E, L")
						 	* And then push back whatever you had before the current subExp (tempHolder)
						 	* set tempStr back to ""
						 */ 						
						if (contains == true) {
							
							tempHolder = tempStr.substring(0, tempStr.lastIndexOf('('));
							counter2--;
							String whichLERev = new StringBuilder(String.valueOf(whichLE)).reverse().toString();
							for(short checkPlusDigits = 0; checkPlusDigits < whichLERev.length(); checkPlusDigits++){
								logExpStack.push((whichLERev).charAt(checkPlusDigits));
							}
							// logExpStack.push(Short.toString(whichLE).charAt(0));
							logExpStack.push('E');
							logExpStack.push('L');
							for (short tempCounter = 0; tempCounter < tempHolder.length(); tempCounter++){
								logExpStack.push(tempHolder.charAt(tempHolder.length() - (tempCounter + 1)));
							}
							tempStr = "";
							whichLE = 0;
							
							
					/*
					 * Else (if the subExp is not in the dictionary):
					 	* set subExp2 to subExp so that you can keep the LE#'s and add them to dictionary 2
					 		* Useful for the final truth table 
					 			* Eg: LE3 = LE1 + LE2, then it will see them as LE3 = T+T
					 	* replace all the subExp's with their dictionary values and put them in '(', ')'
					 * put them in dictionaries
					 * then push the last equation as LE# value back to stack along with the rest of the string
					 * 																				(tempHolder)
					 */
					
						} else {	
							tempHolder = tempStr.substring(0, tempStr.lastIndexOf('(')+1);	
							subExp2 = subExp;
							for(short tempCounter2 = (short) (counter2+1); tempCounter2 > 0; tempCounter2--){						
								subExp = subExp.replaceAll(("LE"+tempCounter2), "(" + dictionary.get("LE"+tempCounter2) + ")");
							}
							
							dictionary.put("LE" + counter2, subExp);
							dictionary2.put("LE" + counter2, subExp2);
							
							tempStr = "";
							String counter2Rev = new StringBuilder(String.valueOf(counter2)).reverse().toString();
							for(short checkPlusDigits = 0; checkPlusDigits < counter2Rev.length(); checkPlusDigits++){
								logExpStack.push((counter2Rev).charAt(checkPlusDigits));
							}
							// logExpStack.push(Short.toString(counter2).charAt(0));
							logExpStack.push('E');
							logExpStack.push('L');
							for (short tempCounter = 1; tempCounter < tempHolder.length(); tempCounter++){
								logExpStack.push(tempHolder.charAt(tempHolder.length() - (tempCounter+1)));
							}
							
						}
						// If it's over, break the loop
						// If you need to continue looking for subExps, set the counter back to 0 to start
						// looking from the beginning since we put everything back to stack
						if (logExpStack.size() == 0){
							break;
						}
						counter = 0;
					}
				}
				
				// Calculate 2^indVarSize for T/F table for indVar's
				short possibleTF = 1;
				for(counter = 0; counter < indVarList.size(); counter++){
					possibleTF*=2;
				}
				
				// number of row + 1 (for the names)
				// nummer of Column = total # of names
				short numOfRows = (short) (possibleTF+1);
				short numOfColumns = (short) (indVarList.size()+dictionary.size());
						
				//firstRow = new ArrayList<String>();
				firstRow = new LinkedList<String>();
				
				/* 
				 * Fill up the first row manually here
				 	* Instead of having two different for loops, I came up with a
				 	* basic algorithm to add two different lists at once
				 	* See the difference between:
				 		* firstRow.add(indVarList.get(counter).toString());
				 		* firstRow.add("LE"+(counter+1-indVarList.size()));
				 		* - indVarList.size() takes care of the job here
				 		* Think of it as counter = # - # = 0 (start)
				 */
				
				for (counter = 0; counter < numOfColumns; counter++){
					if(indVarList.size() > counter){
						firstRow.add(indVarList.get(counter).toString());
					} 								
					else if (indVarList.size()+dictionary.size() >= counter){
						firstRow.add("LE"+(counter+1-indVarList.size()));
					}
				}
				
				// Initialize the 2D array
				twoDimArray = new String[numOfRows][numOfColumns];
				
				// Set up the first row
				for (counter = 0; counter < numOfColumns; counter++){
					twoDimArray[0][counter] = (String) firstRow.get(counter);
					
				}
				
				/*
				 * for(row) * for (column) -> this is trivial
				 * How this algorithm works is that 
				 */
				
				// counter3 has to be int because otherwise in the following algorithm, the value
				// of counter3 will reach 32767 (tested) and the program will fail
				int counter3 = 0;		
				for (short row=0; row<possibleTF; row++) {
		            for (short column=(short) (indVarList.size()-1); column > -1; column--) {	// >= 0
		            	short k = 1;
		            	for(counter = 0; counter < column; counter++){
							k *= 2;
						}
		            	String rep = Short.toString((short) ((row/k)%2));
		            	rep = rep.replace('0', 'T').replace('1', 'F');
		            	twoDimArray[row+1][counter3%indVarList.size()] = rep;
		            	counter3++;		// mod with this to keep it within the indList range
		            }
		        }

				// Temporary strings and short initialized
				// to be explained within the loops
				String tempP = "";
				String tempS = "";
				String tempNot = "";
				short numOfSym = 0;
				short contNot = 0;
				
				/////////////////////////////////////////////////////////////////////
				//////////////////////  		PART 3		   //////////////////////
				/////////////////////////////////////////////////////////////////////
				
				/*
				 * Note: -1 and - indVarList.size() since we already filled up those parts
				 	* 1 for the names and indVarList.Size for truth table T/F's
				 * temp Strings:
				 	* tempP: comes from the dictionary, whatever the sub expression is
				 	* tempS: subExp at tempP's truth or falsity checked from the T/F columns
				 * If cases:
				 	* length = 1:	single ind variable: (a)
				 		* since a single letter, either T or F from the T/F column
				 	* length = 2:	single ind var with -: (-a)
				 		* replace T/F's since it's the case "not"
				 	* length = 3:	single subExp: (aXb) -X = +/*
				 		* replace the letters with their T/F's until both letters
				 		* are replaced, the symbol will be ignored since it's not
				 		* contained in the ind var list and instead it will be
				 		* taken care of in the else case 
				 		* (a+b) - > T (if case) -> T+(else case) -> T+T (if case)
				 	* length > 3:	everything else
				 		* First we check the "-" cases (they are explained at top)
				 			* -( or -LE###, only one LE	->
				 		* 	Some explanations inside
				 */
				for (row = 0; row < twoDimArray.length-1; row++) {					
				    for (column = 0; column < twoDimArray[row].length-indVarList.size(); column++) {
				    	for(short counterB = 0; counterB < dictionary.get("LE"+(column+1)).length(); counterB++){
				    		if (dictionary.get("LE"+(column+1)).length() == 1){
				    			tempP += dictionary.get("LE"+(column+1)).charAt(counterB);
				    			tempS += twoDimArray[row+1][indVarList.indexOf(tempP.charAt(0))].charAt(0);
				    			break;
				    		} else if(dictionary.get("LE"+(column+1)).length() == 2){
				    			
				    			tempP += dictionary.get("LE"+(column+1)).charAt(counterB+1);
				    			tempS += tempP.replace(tempP.charAt(0), twoDimArray[row+1][indVarList.indexOf(tempP.charAt(0))].charAt(0));
				    			tempS = (tempS.charAt(0) == 'T' ? tempS.replace('T', 'F') : tempS.replace('F', 'T'));
				    			tempP = "";
				    			break;
				    		} else if (dictionary.get("LE"+(column+1)).length() == 3){
				    			if ((indVarList).contains(dictionary.get("LE"+(column+1)).charAt(counterB))){
				    				tempP += dictionary.get("LE"+(column+1)).charAt(counterB);				    			
				    				tempS += tempP.replace(tempP.charAt(0), twoDimArray[row+1][indVarList.indexOf(tempP.charAt(0))].charAt(0));
				    				tempP = "";
				    			} else{
				    				tempS += dictionary.get("LE"+(column+1)).charAt(counterB);
				    			}
				    		} else if (dictionary.get("LE"+(column+1)).length() > 3) { 
				    			short numberOfLEs = (short) 
				    					(dictionary2.get("LE"+(column+1)).length() - dictionary2.get("LE"+(column+1)).replace("LE", "L").length());
				    			if('-' == dictionary2.get("LE"+(column+1)).charAt(0) && '(' == dictionary2.get("LE"+(column+1)).charAt(1)){
				    				case1 = true;
				    			} else if ('-' == dictionary2.get("LE"+(column+1)).charAt(0)  &&
				    					  ('L' == dictionary2.get("LE"+(column+1)).charAt(1)  && 
				    					   'E' == dictionary2.get("LE"+(column+1)).charAt(2)) && 
				    					  numberOfLEs == 1) {	// If it's the only LE
				    				case1 = true;
				    			}
				    			
				    			// how many '-'s are there in the sub exp
				    			contNot = (short) 
				    					(dictionary2.get("LE"+(column+1)).length() - dictionary2.get("LE"+(column+1)).replaceAll("-","").length());
				    			
				    			// count the number of symbols
				    			for (short p = 0; p < dictionary2.get("LE"+(column+1)).length(); p++){
				    				
									if(!(Character.isLetterOrDigit(dictionary2.get("LE"+(column+1)).charAt(p)))){
										numOfSym++;
									}	
									
								}
				    			// symbols without "-"s
				    			numOfSym -= contNot;
				    			String[] tempP2 = new String [(numOfSym+1)];
				    			short contNotCopy = contNot;
				    			// Replace the symbols depending on if case1 = true/false
				    			// Notice the difference between replace and replaceAll
				    			// the first will be taken care of after the cases
				    			for(short d2 = 0; d2 < numOfSym+1; d2++){
				    				tempP = (case1 == true ? dictionary2.get("LE"+(column+1)).replace("-", "").replaceAll("[()]", "") :
				    							dictionary2.get("LE"+(column+1)));
				    				contNotCopy = (case1 == true ? contNotCopy-- : contNotCopy);
				    				// check where the "-"s are and set the cases
				    				// contNotCopy - 1 == 0 is to check if there is only 1 "-"
				    				if(tempP.contains("-") == true){
				    					if(tempP.charAt(0) == '-' && (contNotCopy-1) == 0){
				    						case2 = true;
				    					} else if (tempP.charAt(0) == '-' && (contNotCopy-1) != 0){
				    						case4 = true;
				    					} else {
				    						case3 = true;
				    					}
				    					
				    					// replace all after the cases are checked
				    					tempP = tempP.replaceAll("-", "");
				    				}
				    				
				    				// This sets up the two parts of the subexpression by
				    				// separating them by the symbols
				    				tempP2[d2] = tempP.split("\\+|\\*")[d2];
				    			}
				    			// handle the falsity cases if the tempP2 is set up
				    			// (if (the length of it is > 0))
				    			// first time check if 1st is -, case2
				    			// then check if the 2nd is -, case3
				    			// in both cases check also if case4 is true
				    			for(short d3 = 0; d3 < tempP2.length; d3++){
				    				if(d3 < tempP2.length-1) {
				    					tempNot += twoDimArray[(row+1)][firstRow.indexOf(tempP2[d3])];
				    					tempNot = ((case2 == true || case4 == true) ? 
				    							(tempNot.charAt(0) == 'T' ? tempNot.replace('T', 'F') : tempNot.replace('F', 'T'))
				    							: tempNot);
				    					tempS += tempNot + tempP.charAt(tempP2[d3].length());
				    					tempNot = "";
				    				} else {
				    					 
				    					tempNot += twoDimArray[(row+1)][firstRow.indexOf(tempP2[d3])];
				    					tempNot = ((case3 == true || case4 == true) ? 
				    							(tempNot.charAt(0) == 'T' ? tempNot.replace('T', 'F') : tempNot.replace('F', 'T'))
				    							: tempNot);
				    					tempS += tempNot;
				    					tempNot = "";
				    					break;
				    				}
				    				
				    			}
				    			tempP = "";
				    			numOfSym = 0;
				    			break;
				    			
				    		} 
				    	}
				    	// Handle the case1 first (if true), replace T/Fs
				    	tempS = (case1 == true ? (tempS.charAt(0) == 'T' ? tempS.replace('T', 'F') : tempS.replace('F', 'T')) : tempS);
				    	contNot = 0;
				    	case1 = false;
				    	// this is just to have the TXF forms in the truth table, not really needed
				    	twoDimArray[row+1][column + indVarList.size()] = tempS;
				    	
				    	// Since the algorithm sets everything into length 3 strings and
				    	// handles the "-" cases, the below will take care of the sub expressions
				    	if (tempS.length() == 3){
				    		twoDimArray[row+1][column + indVarList.size()] = checkTruth(tempS); 
				    	} 
				    	tempS = "";
				    }
				}
				
				/////////////////////////////////////////////////////////////////////
				//////////////////////  		PART 4		   //////////////////////
				/////////////////////////////////////////////////////////////////////
				
				// The following are print functions, since they are trivial, no
				// explanation needed
				
				System.out.println("\nSet of independent variables:");
				System.out.println("=============================");
				for (counter = 0; counter < indVarList.size(); counter++){
					System.out.printf("-> %s\n", indVarList.get(counter));
				}
				
				System.out.println("\nSet of logical subexpressions and logical expression:");
				System.out.println("=====================================================");
				for(counter = 1; counter < dictionary.size()+1; counter++){
					System.out.printf("-> LE%s: %s\n", counter, dictionary.get("LE"+counter));
				}
				
				System.out.println("\nTruth Table:");
				System.out.println("============");
				
				
				for (row = 0; row < twoDimArray.length; row++) {
				    for (column = 0; column < twoDimArray[row].length; column++) {
				        System.out.print(twoDimArray[row][column] + "\t");
				    }
				    System.out.println();
				}
				
			}
	
			// There might 1-2 small things that I might have forgotten to comment out
			// or some things might not be explained in very detail so:
			// I can explain them orally if I need to prove my algorithms
			
			/* 
			 * TEST CASES:	
			 * ------------
			 * (-((-(a+b))+(-(c+d))))
			 * (a+b)+c)	
			 * (((a+b)+(a+b))+(c+d))		
			 * ((a+b)+(c+d))	
			 * (-((a+b)*c)) 
			 * (-((a+b)+(c+d)))
			 * ((((a+b)+(c+d))*((a+b)+(k+j)))*(((a+b)+(c+d))*((a+b)+(k+j))))
			 * ((((a+b)+(a+b))*((a+b)+(a+b)))*(((a+b)+(a+b))*((a+b)+(a+b))))
			 * (((((a+b)+(a+b))*((a+b)+(a+b)))*(((a+b)+(a+b))*((a+b)+(a+b))))*p)
			 * ((((((((((((a+b)*c)*d)*e)*f)*g)*h)*i)*j)*k)*l)*p)
			 * ((((((((((((a+a)*a)*a)*a)*a)*a)*a)*a)*a)*a)*a)*a)
			 */
}
