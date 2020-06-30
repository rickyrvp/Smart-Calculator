# Smart Calculator
Java Program that perform functions of a smart calculator. Has following features:
    
    - converts arithmetic expressions from infix to postfix notation and then evaluates them
    - assign values and store values in variables

To learn more about this project, please visit the [Hyperskill Website](https://hyperskill.org/projects/42?goal=7)

The Source files for this project can be found by navigating to:
      
      Smart Calculator/task/src/calculator
      
This Directory contains 2 files:

    Main.java - Create a new instance of Calculator and initiates the program.
    Calculator.java - This file contains the methods that run the program.

The Calculator class contains the following methods:
  1. *turnOn()*
     - Encapsulates the program and calls two methods, menu() and turnOff().
  
  2. *menu()*
     - Main menu which prompts the user to enter commands, initiate/assign variables or evaluate expressions. 
            
            a) Program accepts two commands
                  - /help - entering command gives you general information about the program
                  - /exit - entering command terminates the program
                  
            b) to initiate or assign variables then must be in the form:
                  i.e var = 123 OR var = var
                  - (left) var is the identifier/variable (program accepts variable names that only contain letters [a-zA-Z])
                  - (right) is the assignment which can be a numeric value or another existing variable
                    
            c) To evaluate an expression you must enter it as a command in a single line in infix notation
                  i.e 3 + b * ((4 + 3) * 2 + 1) - 6 / (2 + 1)
  
  3. *checkVariable(String input)*
     - Checks if the user input of a variable assignment is valid. Will produce an error message if the user entered a variable or assignment that does not meet the criteria entered above.
     
  4. *assignVar(String input)*
     - Takes a user input of a variable assignment and assigns/updates the value
 
  5. *calculate(String input)*
     - Calculates the arithmetic expression entered by the user
 
  6. *simplifyExpression(String input)*
     - Transforms the user's arithmetic expression to the desired format
     - Returns a String[] object containing all the operands and operators as elements of the arrays
 
  7. *convertInfixToPostfix(String[] expression)*
     - Takes the String[] containing the elements of the arithmetic expression and converts it to postfix notation
     - Returns a String version of the postfix expression

  8. *precedence(String op)*
     - Takes the String representation of an operator and assigns a priority.
     - Returns an integer representing the priority of the operator. The higher the value, the greater its priority.
         | Operator(s) | Rank |
         | ----------- | ---- |
         |  + , -      |  1   |
         |  * , /      |  2   |
         |  ^          |  3   |
 
  9. *turnOff()*
     - Terminates the program.
