package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private final int rows;
    private final int columns;

    private final int mines;

    private final String[][] field;

    private List<List<Integer>> minePositions;

    public Field(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        this.field = generateField(mines);
    }

    public List<List<Integer>> getMinePositions() {
        return this.minePositions;
    }

    private String[][] generateField(int mines) {
        String[][] generatedField = new String[rows][columns];

        String gameString = generateGameString(mines);

        populateArrayFromString(gameString, generatedField);

        positionHelperDigits(generatedField);

        return generatedField;
    }

    private void populateArrayFromString(String source, String[][] generatedField) {
        List<List<Integer>> mines = new ArrayList<>();
        int stringCurrentPosition = 0;
        for (int row = 0; row < generatedField.length; row++) {
            for (int column = 0; column < generatedField[row].length; column++) {
                generatedField[row][column] = String.valueOf(source.charAt(stringCurrentPosition));
                if ("X".equals(generatedField[row][column])) {
                    List<Integer> arr = new ArrayList<>();
                    arr.add(column + 1);
                    arr.add(row + 1);
                    mines.add(arr);
                }
                stringCurrentPosition++;
            }
        }
        this.minePositions = mines;
    }

    private String generateGameString(int mines) {
        int fieldLength = rows * columns;

        double chanceToPlaceMineCoefficient = mines * 1.0 / fieldLength;
        StringBuilder sb = new StringBuilder();

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

    public void printCurrentField() {
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

    public int getRows() {
        return rows;
    }

    public String getCellData(int column, int row) {
        return this.field[row][column];
    }

    public void markToggle(int column, int row) {
        if (".".equals(field[row][column]) || "X".equals(field[row][column])) {
            field[row][column] = "*";
        } else {
            field[row][column] = ".";
        }
    }
}
