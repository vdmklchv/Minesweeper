package minesweeper;

import java.util.*;

public class Game {
    // VARS
    // used to store coordinates of all marked fields by player
    private final List<List<Integer>> markedFields = new ArrayList<>();

    public void run() {
        /* this method contains game logic and runs the game until player correctly guesses all mines */
        // set up and print field with mines
        int numberOfMines = getNumberOfMines();
        Field field = new Field(9, 9, numberOfMines);
        field.printField();

        // play until game is won
        do {
            playerMove(field);
        } while (!isGameWon(markedFields, field.getMinePositions()));

        System.out.println("Congratulations! You found all the mines!");
    }

    private int getNumberOfMines() {
        /* this method asks user for number of mines he randomly wants to place on field and returns
        this value */
        Scanner sc = new Scanner(System.in);
        int numberOfMines = 0;
        while (numberOfMines == 0) {
            try {
                System.out.println("How many mines do you want on the field?");
                numberOfMines = sc.nextInt();
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Strange input. Please input number");
            }
        }
        return numberOfMines;
    }

    private void playerMove(Field field) {
        // ask user for coordinates to mark as mine
        List<Integer> moveCoordinates = getPlayerMoveCoordinates(field);

        // check if coordinates are ".", "*" or "X"
        if (".".equals(field.getCellData(moveCoordinates.get(0) - 1, moveCoordinates.get(1) - 1)) ||
                "*".equals(field.getCellData(moveCoordinates.get(0) - 1, moveCoordinates.get(1) - 1)) ||
                "X".equals(field.getCellData(moveCoordinates.get(0) - 1, moveCoordinates.get(1) - 1))) {
            // toggle asterisk in the field
            field.markToggle(moveCoordinates.get(0) - 1, moveCoordinates.get(1) - 1);
            // remove from coordinates if exists and add if it does not exist
            toggleMoveCoordinates(moveCoordinates);
            field.printField();
            return; // early return if this clause has been activated
        }

        // get cell data and check if it is a number or some other erroneous data
        String fieldData = field.getCellData(moveCoordinates.get(0) - 1, moveCoordinates.get(1) - 1);
        try {
            Integer.parseInt(fieldData);
            System.out.println("There is a number here!");
        } catch (NumberFormatException e) {
            System.out.println("Strange content of the cell");
        }
    }


    private List<Integer> getPlayerMoveCoordinates(Field field) {
        /* this method takes in field object, asks user to enter coordinates within the field coordinates
        and returns list with picked coordinates
         */
        List<Integer> moveCoordinates = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        int column = -1;
        int row = -1;

        while (column <= 0 || row <= 0 || column > field.getRows() || row > field.getRows()) {
            try {
                System.out.println("Set/delete mine marks (x and y coordinates): ");
                column = sc.nextInt();
                row = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Sorry, wrong input: " + e.getClass().getSimpleName());
            }
        }

        moveCoordinates.add(column);
        moveCoordinates.add(row);

        return moveCoordinates;
    }

    private void toggleMoveCoordinates(List<Integer> coordinates) {
        /* helper method that takes in list of coordinates and adds/removes them from user coordinate list */
        if (markedFields.contains(coordinates)) {
            markedFields.remove(coordinates);
        } else {
            markedFields.add(coordinates);
        }
    }

    private boolean isGameWon(List<List<Integer>> playerMoveList, List<List<Integer>> mineList) {
        /* This method compares mine list and player move list and returns true if game is won */
        if (playerMoveList.size() != mineList.size()) {
            return false;
        }
        for (List<Integer> list: playerMoveList) {
            if (!mineList.contains(list)) {
                return false;
            }
        }
        return true;
    }

}
