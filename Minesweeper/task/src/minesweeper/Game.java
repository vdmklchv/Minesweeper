package minesweeper;

import java.util.*;

public class Game {

    private final List<List<Integer>> markedFields = new ArrayList<>();

    public void run() {
        // set up the field with mines
        int numberOfMines = getNumberOfMines();
        Field field = new Field(9, 9, numberOfMines);
        field.printCurrentField();

        do {
            playerMove(field);
            System.out.println(field.getMinePositions());
            System.out.println(markedFields);
        } while (!isGameWon(markedFields, field.getMinePositions()));

        System.out.println("Congratulations! You found all the mines!");
    }

    private int getNumberOfMines() {
        Scanner sc = new Scanner(System.in);
        int numberOfMines = 0;
        while (numberOfMines == 0) {
            try {
                System.out.println("How many mines do you want on the field?");
                numberOfMines = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Strange input. Please input number");
            }
        }
        return numberOfMines;
    }

    private void playerMove(Field field) {
        // check if moveCoordinates are in markedFields
            List<Integer> moveCoordinates = getPlayerMoveCoordinates(field);
            if (".".equals(field.getCellData(moveCoordinates.get(0), moveCoordinates.get(1))) ||
            "*".equals(field.getCellData(moveCoordinates.get(0), moveCoordinates.get(1))) ||
                    "X".equals(field.getCellData(moveCoordinates.get(0), moveCoordinates.get(1)))) {
                field.markToggle(moveCoordinates.get(0), moveCoordinates.get(1));
                // remove from coordinates if exists and add if not exist
                toggleMoveCoordinates(moveCoordinates);
                field.printCurrentField();
                return;
            }

            String fieldData = field.getCellData(moveCoordinates.get(0), moveCoordinates.get(1));
            try {
                Integer.parseInt(fieldData);
                System.out.println("There is a number here!");
            } catch (NumberFormatException e) {
                System.out.println("Strange content of the cell");
            }
    }


    private List<Integer> getPlayerMoveCoordinates(Field field) {
        List<Integer> moveCoordinates = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        int coordinateA = -1;
        int coordinateB = -1;

        while (coordinateA < 0 || coordinateB < 0 || coordinateA > field.getRows() - 1 || coordinateB > field.getRows() - 1) {
            try {
                System.out.println("Set/delete mine marks (x and y coordinates): ");
                coordinateA = sc.nextInt();
                coordinateB = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Sorry, wrong input: " + e.getClass().getSimpleName());
            }
        }

        moveCoordinates.add(coordinateA);
        moveCoordinates.add(coordinateB);

        return moveCoordinates;
    }

    private void toggleMoveCoordinates(List<Integer> coordinates) {
        if (markedFields.contains(coordinates)) {
            markedFields.remove(coordinates);
        } else {
            markedFields.add(coordinates);
        }
    }

    private boolean isGameWon(List<List<Integer>> playerMoveList, List<List<Integer>> mineList) {
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
