package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Field {

    // FIELDS
    private final int rows;
    private final int columns;

    private final int mines;

    private final String[][] field;

    private List<List<Integer>> minePositions;


    // CONSTRUCTOR
    public Field(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.field = generateField(mines);
    }

    // GETTERS
    public List<List<Integer>> getMinePositions() {
        return this.minePositions;
    }


    public int getRows() {
        return rows;
    }

    public String getCellData(int column, int row) {
        return this.field[row][column];
    }

    private String[][] generateField(int mines) {
        /* method takes in number of mines, generates field and returns it */
        String[][] generatedField = new String[rows][columns];

        // build a string representation of game field
        String gameString = generateGameString(mines);

        // convert string representation to array representation
        populateArrayAndStoreMinesCoordinates(gameString, generatedField);

        // place hint digits about nearby mines
        positionHelperDigits(generatedField);

        return generatedField;
    }

    private void populateArrayAndStoreMinesCoordinates(String source, String[][] generatedField) {
        /* this method takes source string, generates field and remembers and sets mine positions */
        List<List<Integer>> mines = new ArrayList<>(); // array to store all mines coordinates
        int stringCurrentPosition = 0;
        for (int row = 0; row < generatedField.length; row++) {
            for (int column = 0; column < generatedField[row].length; column++) {
                generatedField[row][column] = String.valueOf(source.charAt(stringCurrentPosition));
                // check if data is a mine, create array and add to array of mines
                if ("X".equals(generatedField[row][column])) {
                    List<Integer> arr = new ArrayList<>();
                    arr.add(column + 1);
                    arr.add(row + 1);
                    mines.add(arr);
                }
                stringCurrentPosition++;
            }
        }
        this.minePositions = mines; // set field with mines
    }

    private String generateGameString(int mines) {
        /* this method takes in mines, uses random method to determine probability of mine appearance
        and returns string field representation
         */

        int fieldLength = rows * columns;

        double chanceToPlaceMineCoefficient = mines * 1.0 / fieldLength;
        StringBuilder sb = new StringBuilder();

        // generate StringBuilder object until number of mines is correct
        while (mines > 0) {
            mines = this.mines;
            sb.setLength(0);
            for (int i = 0; i < fieldLength; i++) {
                double randomNumber = Math.random();
                if (randomNumber < chanceToPlaceMineCoefficient && mines > 0) {
                    sb.append("X");
                    mines--;
                } else {
                    sb.append(".");
                }
            }
        }
        return sb.toString();
    }

    public void printField() {
        /* prints field in a formatted way */
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int row = 0; row < rows; row++) {
            System.out.print(row + 1 + "|");
            for (int column = 0; column < columns; column++) {
                if ("X".equals(field[row][column])) {
                    System.out.print('.');
                } else {
                    System.out.print(field[row][column]);
                }
            }
            System.out.print("|\n");
        }
        System.out.println("-|---------|");
    }

    private void positionHelperDigits(String[][] field) {
        /* helper method to calculate and position digits hinting about nearby mines */
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field.length; column++) {
                if (".".equals(field[row][column])) {
                    int nearbyMines = calculateNearbyMinesFor(row, column, field);
                    if (nearbyMines > 0) {
                        field[row][column] = String.valueOf(nearbyMines);
                    }
                }
            }
        }
    }

    private int calculateNearbyMinesFor(int row, int column, String[][] field) {
        /* this method takes in row and column and field and calculates number of nearby mines
        for a single cell.
         */
        int count = 0;

        // DIRECTIONS
        // up
        if (row - 1 >= 0 && "X".equals(field[row - 1][column])) {
            count++;
        }
        // up-right
        if (row - 1 >= 0 && column + 1 < field.length && "X".equals(field[row - 1][column + 1])) {
            count++;
        }
        // right
        if (column + 1 < field.length && "X".equals(field[row][column + 1])) {
            count++;
        }
        // down-right
        if (row + 1 < field.length && column + 1 < field.length && "X".equals(field[row + 1][column + 1])) {
            count++;
        }
        // down
        if (row + 1 < field.length && "X".equals(field[row + 1][column])) {
            count++;
        }
        // down-left
        if (row + 1 < field.length && column - 1 >= 0 && "X".equals(field[row + 1][column - 1])) {
            count++;
        }
        // left
        if (column - 1 >= 0 && "X".equals(field[row][column - 1])) {
            count++;
        }
        // up-left
        if (row - 1 >= 0 && column - 1 >= 0 && "X".equals(field[row - 1][column - 1])) {
            count++;
        }

        return count;
    }


    public void markToggle(int column, int row) {
        /* this method toggles marks on field depending on their current state */
        if (".".equals(field[row][column]) || "X".equals(field[row][column])) {
            field[row][column] = "*";
        } else {
            field[row][column] = ".";
        }
    }
}
