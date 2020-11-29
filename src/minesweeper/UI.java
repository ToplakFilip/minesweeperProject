package minesweeper;

import java.util.Arrays;
import java.util.Scanner;

public class UI {

    static int minesFound;
    static int fakeFound;

    public UI(){
    }

   static void start() {

        Scanner scan = new Scanner(System.in);

        System.out.println("[ HOW MANY MINES ON THE FIELD ? ]");
        int broj = Integer.parseInt(scan.nextLine());
        Board board = new Board();
        String[][] field = board.createField(broj);
        Board.Coordinates[] mineCoord = board.getMines();
        int minesFound = 0;
        int fakeFound = 0;
        board.drawBoard(field);

        while (true) {
            int[] part = coordinates(scan);
            boolean mineNotThere = true;
            if (field[part[1]][part[0]].matches("[1-9]")) {
                System.out.println("[ A NUMBER IS PLACED THERE ! ]");
            } else if (field[part[1]][part[0]].equals("-")) {
                field[part[1]][part[0]] = "*";
                int i = 0;
                while (i < mineCoord.length) {
                    if (mineCoord[i].getX() == part[1] && mineCoord[i].getY() == part[0]) {
                        minesFound++;
                        mineNotThere = false;
                        break;
                    }
                    i++;
                }
                if (mineNotThere) {
                    fakeFound++;
                }
                board.drawBoard(field);
            } else if (field[part[1]][part[0]].equals("*")) {
                field[part[1]][part[0]] = "-";

                int i = 0;
                while (i < mineCoord.length) {
                    if (mineCoord[i].getX() == part[1] && mineCoord[i].getY() == part[0]) {
                        minesFound--;
                        mineNotThere = false;
                        break;
                    }
                    i++;
                }
                if (mineNotThere) {
                    fakeFound--;
                }
                board.drawBoard(field);
            }
            if (minesFound == mineCoord.length && fakeFound == 0) {
                System.out.println("[ V I C T O R Y ]");
                break;
            }
        }
    }

    static private int[] coordinates(Scanner scan){
        System.out.println("[ SET OR DELETE MINE MARKS COORDINATES (X Y) ]");
        String coord = scan.nextLine();
        int[] part = Arrays.stream(coord.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        part[0]--;
        part[1]--;
        return part;
    }

}
