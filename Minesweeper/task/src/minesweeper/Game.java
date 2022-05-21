package minesweeper;

import java.util.*;

public class Game {
    static GAME_STATE state = GAME_STATE.IN_PROGRESS;

    public void run() {
        /* this method contains game logic and runs the game until player correctly guesses all mines */
        // set up and print field with mines
        int numberOfMines = getNumberOfMines();
        Field field = new Field(9, 9, numberOfMines);

        field.printCurrentField();

        // play until game is won
        do {
            playerMove(field);
        } while (!field.hasNoMoreFreeCells(field.getFreePositions(), field.getOpenFields()) && !allMinesMarked(field.getMarkedFields(), field.getMinePositions()) && state == GAME_STATE.IN_PROGRESS);
        if (state != GAME_STATE.LOST) {
            System.out.println("Congratulations! You found all the mines!");
        }
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
         /* this method takes in field object, asks user to enter coordinates within the field coordinates
        and returns list with picked coordinates
         */
        Scanner sc = new Scanner(System.in);

        int column = -1;
        int row = -1;
        String command;


        while (column <= 0 || row <= 0 || column > field.getRows() || row > field.getRows()) {
            try {
                System.out.println("Set/unset mine marks or claim cell as free: ");
                column = sc.nextInt();
                row = sc.nextInt();
                command = sc.next();
                if (column <= 0 || row <= 0 || column > field.getRows() || row > field.getRows()) {
                    System.out.println("Coordinates out of range");
                    continue;
                }
            } catch (InputMismatchException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Sorry, wrong input");
                sc.nextLine();
                continue;
            }

            switch (command) {
                case "mine":
                    if (field.isCellClosed(row - 1, column - 1)) {
                        field.addMarkMineCoordinate(row - 1, column - 1);
                        field.markMineOnCurrentFieldToggle(row - 1, column - 1);
                    }

                    field.printCurrentField();
                    break;
                case "free":
                    if (isLandedOnMine(column, row, field)) {
                        field.openBaseCellInCurrentField(column, row);
                        field.printCurrentField();
                        System.out.println("You stepped on a mine and failed!");
                        state = GAME_STATE.LOST;
                        return;
                    }
                    field.openBaseCellInCurrentField(column, row);
                    field.printCurrentField();
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }

    }

    private boolean isLandedOnMine(int column, int row, Field field) {
        /* returns true if specified coordinates contain mine */
        return "X".equals(field.getCellData(column - 1, row - 1));
    }


    private boolean allMinesMarked(List<List<Integer>> playerMoveList, List<List<Integer>> mineList) {
        /* This method compares mine list and player move list and returns true if game is won */


        if (playerMoveList.size() != mineList.size()) {
            return false;
        }
        for (List<Integer> list: playerMoveList) {
            if (!mineList.contains(list)) {
                return false;
            }
        }

        state = GAME_STATE.WON;
        return true;
    }

    enum GAME_STATE {
        IN_PROGRESS, LOST, WON
    }
}
