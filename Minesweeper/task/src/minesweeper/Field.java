package minesweeper;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field {

    // FIELDS
    private final int rows;
    private final int columns;

    private final int mines;

    private final String[][] baseField;
    private final String[][] currentField;

    private List<List<Integer>> minePositions;
    private List<List<Integer>> freePositions;

    private final List<List<Integer>> openFields = new ArrayList<>();

    // CONSTRUCTOR
    public Field(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.baseField = generateBaseField(mines);
        this.currentField = generateCurrentField();
    }

    private String[][] generateCurrentField() {
        String[][] currentField = new String[9][9];
        for (String[] row: currentField) {
            Arrays.fill(row, ".");
        }
        return currentField;
    }

    // GETTERS
    public List<List<Integer>> getMinePositions() {
        return this.minePositions;
    }

    public int getRows() {
        return rows;
    }

    public List<List<Integer>> getFreePositions() {
        return freePositions;
    }

    public String getCellData(int column, int row) {
        return this.baseField[row][column];
    }


    private String[][] generateBaseField(int mines) {
        /* method takes in number of mines, generates field and returns it */
        String[][] generatedBaseField = new String[rows][columns];

        // build a string representation of game field
        String gameString = generateGameString(mines);

        // convert string representation to array representation
        populateArrayAndStoreMinesAndFreeCoordinates(gameString, generatedBaseField);

        // place hint digits about nearby mines
        positionHelperDigits(generatedBaseField);

        return generatedBaseField;
    }

    private void populateArrayAndStoreMinesAndFreeCoordinates(String source, String[][] generatedField) {
        /* this method takes source string, generates field and remembers and sets mine positions */
        List<List<Integer>> mines = new ArrayList<>(); // array to store all mines coordinates
        List<List<Integer>> free = new ArrayList<>(); // array to store all free positions
        int stringCurrentPosition = 0;
        for (int row = 0; row < generatedField.length; row++) {
            for (int column = 0; column < generatedField[row].length; column++) {
                generatedField[row][column] = String.valueOf(source.charAt(stringCurrentPosition));
                // check if data is a mine, create array and add to array of mines
                List<Integer> arr = new ArrayList<>();
                arr.add(row);
                arr.add(column);
                if ("X".equals(generatedField[row][column])) {
                    mines.add(arr);
                } else {
                    free.add(arr);
                }
                stringCurrentPosition++;
            }
        }
        this.minePositions = mines; // set field with mines
        this.freePositions = free;
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
                    sb.append("/");
                }
            }
        }
        return sb.toString();
    }

    public void printCurrentField() {
        /* prints field in a formatted way */
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int row = 0; row < rows; row++) {
            System.out.print(row + 1 + "|");
            for (int column = 0; column < columns; column++) {
                System.out.print(this.currentField[row][column]);
            }
            System.out.print("|\n");
        }
        System.out.println("-|---------|");
    }

    private void positionHelperDigits(String[][] field) {
        /* helper method to calculate and position digits hinting about nearby mines */
        for (int row = 0; row < field.length; row++) {
            for (int column = 0; column < field.length; column++) {
                if ("/".equals(field[row][column])) {
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


    public void markMineOnCurrentFieldToggle(int row, int column) {
        /* this method toggles marks on field depending on their current state */
        if ("*".equals(currentField[row][column])) {
            currentField[row][column] = ".";
        } else if (".".equals(currentField[row][column])){
            currentField[row][column] = "*";
        }
    }

    public void openBaseCellInCurrentField(int column, int row) {
        int fieldRow = row - 1;
        int fieldColumn = column - 1;
        currentField[fieldRow][fieldColumn] = baseField[fieldRow][fieldColumn];
        addToListOfOpenCells(fieldRow, fieldColumn);
        if (calculateNearbyMinesFor(fieldRow, fieldColumn, baseField) == 0 && hasClosedCellsAround(fieldRow, fieldColumn) &&
        currentField[fieldRow][fieldColumn].equals("/")) {
            openNearbyCells(fieldRow, fieldColumn);
        }
    }

    public boolean hasNoMoreFreeCells(List<List<Integer>> freePositions, List<List<Integer>> openFields) {
        if (freePositions.size() != openFields.size()) {
            return false;
        }

        for (List<Integer> coordinates: freePositions) {
            if (!openFields.contains(coordinates)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCellClosed(int row, int column) {
        if (!isInBounds(row, column)) {
            return false;
        }
        return ".".equals(currentField[row][column]) || "*".equals(currentField[row][column]);
    }

    private boolean isInBounds(int row, int column) {
        return row >= 0 && row < baseField.length && column >= 0 && column < baseField.length;
    }

    private boolean hasClosedCellsAround(int row, int column) {
        return isCellClosed(row - 1, column) || isCellClosed(row - 1, column + 1) ||
                isCellClosed(row, column + 1) || isCellClosed(row + 1, column + 1) ||
                isCellClosed(row + 1, column) || isCellClosed(row + 1, column - 1) ||
                isCellClosed(row, column - 1) || isCellClosed(row - 1, column - 1);
    }

    private void openNearbyCells(int row, int column) {
        if (calculateNearbyMinesFor(row, column, baseField) > 0 || !hasClosedCellsAround(row, column)) {
            return;
        }
        if (row - 1 >= 0 && isCellClosed(row - 1, column)) {
            currentField[row - 1][column] = baseField[row - 1][column];
            addToListOfOpenCells(row - 1, column);
            if (calculateNearbyMinesFor(row - 1, column, baseField) == 0 && hasClosedCellsAround(row - 1, column) && currentField[row - 1][column].equals("/")) {
                openNearbyCells(row - 1, column);
            }
        }

        if (row - 1 >= 0 && column + 1 < baseField.length && isCellClosed(row - 1, column + 1)) {
            currentField[row - 1][column + 1] = baseField[row - 1][column + 1];
            addToListOfOpenCells(row - 1, column + 1);
            if (calculateNearbyMinesFor(row - 1, column + 1, baseField) == 0 && hasClosedCellsAround(row - 1, column + 1) && currentField[row - 1][column + 1].equals("/")) {
                openNearbyCells(row - 1, column + 1);
            }
        }

        if (row + 1 < baseField.length && isCellClosed(row + 1, column)) {
            currentField[row + 1][column] = baseField[row + 1][column];
            addToListOfOpenCells(row + 1, column);
            if (calculateNearbyMinesFor(row + 1, column, baseField) == 0 && hasClosedCellsAround(row + 1, column) && currentField[row + 1][column].equals("/")) {
                openNearbyCells(row + 1, column);
            }
        }

        if (row + 1 < baseField.length && column + 1 < baseField.length && isCellClosed(row + 1, column + 1)) {
            currentField[row + 1][column + 1] = baseField[row + 1][column + 1];
            addToListOfOpenCells(row + 1, column + 1);
            if (calculateNearbyMinesFor(row + 1, column + 1, baseField) == 0 && hasClosedCellsAround(row + 1, column + 1) && currentField[row + 1][column + 1].equals("/")) {
                openNearbyCells(row + 1, column + 1);
            }
        }
        if (column + 1 < baseField.length && isCellClosed(row, column + 1)) {
            currentField[row][column + 1] = baseField[row][column + 1];
            addToListOfOpenCells(row, column + 1);
            if (calculateNearbyMinesFor(row, column + 1, baseField) == 0 && hasClosedCellsAround(row, column + 1) && currentField[row][column + 1].equals("/")) {
                openNearbyCells(row, column + 1);
            }
        }
        if (column - 1 >= 0 && row - 1 >= 0 && isCellClosed(row - 1, column - 1)) {
            currentField[row - 1][column - 1] = baseField[row - 1][column - 1];
            addToListOfOpenCells(row - 1, column - 1);
            if (calculateNearbyMinesFor(row - 1, column - 1, baseField) == 0 && hasClosedCellsAround(row - 1, column - 1) && currentField[row - 1][column - 1].equals("/")) {
                openNearbyCells(row - 1, column - 1);
            }
        }
        if (column - 1 >= 0 && row + 1 < baseField.length && isCellClosed(row + 1, column - 1)) {
            currentField[row + 1][column - 1] = baseField[row + 1][column - 1];
            addToListOfOpenCells(row + 1, column - 1);
            if (calculateNearbyMinesFor(row + 1, column - 1, baseField) == 0 && hasClosedCellsAround(row + 1, column - 1) && currentField[row + 1][column - 1].equals("/")) {
                openNearbyCells(row + 1, column - 1);
            }
        }
        if (column - 1 >= 0 && isCellClosed(row, column - 1)) {
            currentField[row][column - 1] = baseField[row][column - 1];
            addToListOfOpenCells(row, column - 1);
            if (calculateNearbyMinesFor(row, column - 1, baseField) == 0 && hasClosedCellsAround(row, column - 1) && currentField[row][column - 1].equals("/")) {
                openNearbyCells(row, column - 1);
            }
        }
    }

    private void addToListOfOpenCells(int row, int column) {
        List<Integer> tempList = new ArrayList<>();
        tempList.add(row);
        tempList.add(column);
        openFields.add(tempList);
    }

    public List<List<Integer>> getOpenFields() {
        return openFields;
    }
}
