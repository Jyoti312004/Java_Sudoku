import java.util.Scanner;
public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    boolean play = true;
    while(play) {
      playGame();
      System.out.println("Do you want to play the game again? y/n");
      String answer = sc.next();
      if((answer.substring(0,1)).toLowerCase().equals("n")) {
        play = false;
      }
    }
    System.out.println("Thanks for playing!");
  }
  public static void playGame() {
    int[][]board = new int[9][9];
    int[][]displayBoard = new int[9][9];
    boolean solved = false;

    while (!solved) {
      for(int i=0; i<9; i++) {
        board[i] = shuffle();
      }
    
      //Create Blanks
      for(int row=0; row<board.length; row++) {
        for(int column=0; column<board[0].length; column++) {
          int bigRow = (row/3)*3;
          int bigColumn = (column/3)*3;
          for(int i=bigRow; i<bigRow+3; i++) {
            for(int j=bigColumn; j<bigColumn+3; j++) {
              if(board[i][j] == board[row][column] && !(i==row && j==column)) {
                board[row][column]=0;
              }
            }
          }
        }
      }
    
      String column = "";
      for(int i=0; i<9; i++) {
        column = "";
        for(int j=0; j<9; j++) {
          if(column.indexOf(board[j][i]+"")!=-1) {
            board[j][i]=0;
          }
          else {
            column+=board[j][i] + " ";
          }
        }
      }
      //Create Display Board
      for(int i=0; i<9; i++) {
        for(int j=0; j<9; j++) {
          displayBoard[i][j] = board[i][j];
        }
      }
      //Test for Board Validity
      if(solveBoard(board)) {
        //System.out.println("Solved");
        solved = true;
      }
      else {
        //System.out.println("Unsolvable");
      }
    }
    //display(board);
    //System.out.println();
    //printBoard(displayBoard);
    System.out.println();
    boolean runGame = true;
    int strikes = 0;
    while(runGame) {
      display(displayBoard);
      String coordinates = getCoordinates();
      int number = getNumber();
      int row;
      int column;
      row = letterToNumber(coordinates.substring(0,1));
      column = letterToNumber(coordinates.substring(1));
      if(displayBoard[row][column]!=0) {
        System.out.println("There is already a number in that spot!");
      }
      else {
        if(board[row][column] == number) {
          displayBoard[row][column] = number;
          boolean findnumbers = true;
          int findRow = -1;
          while(findnumbers) {
            int count = 0;
            for(int i=0; i<9; i++) {
              for(int j=0; j<9; j++) {
                if(displayBoard[i][j] == number) {
                  count++;
                }
              }
            }
            if(count>8) {
              findnumbers = false;
            }
            findRow = (int)(Math.random()*9);
            for(int i=0; i<9; i++) {
              if(displayBoard[findRow][i]==0 && board[findRow][i] == number) {
                displayBoard[findRow][i] = number;
                findnumbers = false;
              }
            }
          }
        }
        else {
        System.out.println("WRONG");
        strikes++;
        }
      }
      System.out.println("Strikes: " + strikes);
      System.out.println();
      if(winCondition(displayBoard)) {
        display(displayBoard);
        System.out.println("Congratulations! You Win!");
        runGame = false;
      }
      if(strikes==3) {
        runGame=false;
        System.out.println("3 strikes! You lose!");
      }
    }
  }

    public static int[] shuffle() {
    int[] array = {1,2,3,4,5,6,7,8,9};
    for(int i=0; i<50; i++) {
      int index1 = (int)(Math.random()*9);
      int index2 = (int)(Math.random()*9);
      int temp = array[index1];
      array[index1] = array[index2];
      array[index2] = temp;
    }
    return(array);
  }
  
  public static void printBoard(int[][] board) {
    for(int row=0;row<9; row++) {
      for(int column = 0; column<9;column++) {
        System.out.print(board[row][column]+" ");
      }
      System.out.println();
    }
  }
  
  public static boolean rowCheck(int[][] board, int number, int row, int column) {
    for(int i=0; i<9; i++) {
      if(board[row][i]==number && i!=column) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean columnCheck(int[][] board, int number, int row, int column) {
    for(int i=0; i<9; i++) {
      if(board[i][column]==number&&i!=row) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean boxCheck(int[][] board, int number, int row, int column) {
    int localRow = row-row%3;
    int localColumn = column-column%3;
    for(int i=localRow; i<localRow+3; i++) {
      for(int j=localColumn; j<localColumn+3; j++) {
        if(board[i][j] == number && !(localRow==row && localColumn==column)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean isValid(int[][] board, int number, int row, int column) {
    return(!rowCheck(board, number, row, column) && !columnCheck(board, number, row, column) && !boxCheck(board, number, row, column));
  }
  
  public static boolean solveBoard(int[][] board) {
    for(int row=0; row<9; row++) {
      for(int column=0; column<9; column++) {
        if(board[row][column]==0) {
          for(int k=1; k<=9; k++) {
            if(isValid(board,k, row, column)){
              board[row][column] = k;
              if(solveBoard(board)) {
                return true;
              }
              else {
                board[row][column] = 0;
              }
            }
          }
          return false;
        }
      }
    }
    return true;
  }
  public static void display(int[][] board) {
    System.out.println("    A B C   D E F   G H I");
    for(int i=0; i<9; i++) {
      if(i%3==0) {
        System.out.println("  -------------------------");
      }
      System.out.print(numberToLetter(i) + " ");
      for(int j=0; j<9; j++) {
        if(j%3==0) {
          System.out.print("| ");
        }
        if(board[i][j]==0) {
          System.out.print("  ");
        }
        else {
          System.out.print(board[i][j] + " ");
        }
      }
      System.out.print("|" + "\n");
    }
    System.out.println("  -------------------------");
  }

  public static String numberToLetter(int number) {
    switch(number) {
      case 0:
        return "A";
      case 1:
        return "B";
      case 2:
        return "C";
      case 3:
        return "D";
      case 4:
        return "E";
      case 5:
        return "F";
      case 6:
        return "G";
      case 7:
        return "H";
      case 8:
        return "I";
      default:
        return "Error";
    }
  }
  public static int letterToNumber(String letter) {
    switch(letter) {
      case "A", "a":
        return 0;
      case "B", "b":
        return 1;
      case "C", "c":
        return 2;
      case "D", "d":
        return 3;
      case "E", "e":
        return 4;
      case "F", "f":
        return 5;
      case "G", "g":
        return 6;
      case "H", "h":
        return 7;
      case "I", "i":
        return 8;
      default:
        return 100;
    }
  }
  public static int[][] playerInput(int[][] solutionBoard, int[][] displayBoard) {
    
    return displayBoard;
  }
  public static String getCoordinates() {
    Scanner sc =  new Scanner(System.in);
    System.out.println("Enter the coordinates: ");
    String coordinates = sc.next();
    return coordinates;
  }
  public static int getNumber() {
    Scanner sc =  new Scanner(System.in);
    System.out.println("Enter the number: ");
    int number = sc.nextInt();
    return number;
  }
  public static boolean winCondition(int[][] displayboard) {
    for(int i = 0; i<9 ; i++) {
      for(int j=0; j<9; j++) {
        if(displayboard[i][j]==0)
          return false;
      }
    }
    return true;
  }
}