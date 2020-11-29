package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class sveujednom {
    public static class UI {

        void start() {

            Scanner scan = new Scanner(System.in);

            System.out.println("How many mines do you want on the field?");
            int broj = Integer.parseInt(scan.nextLine());
            Board board = new Board();
            String[][] field = board.createField(broj);
            Board.Coordinates[] mineCoord = board.getMines();
            int minesFound = 0;
            int fakeFound = 0;
            board.drawBoard(field);

            while (true) {
                System.out.println("Set/delete mine marks (x and y coordinates): ");
                String coord = scan.nextLine();
                int[] part = Arrays.stream(coord.split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                part[0]--;
                part[1]--;

                boolean mineNotThere = true;
                if (field[part[1]][part[0]].matches("[1-9]")) {
                    System.out.println("There is a number here!");

                } else if (field[part[1]][part[0]].equals(".")) {
                    field[part[1]][part[0]] = "*";
                    for (int i = 0; i < mineCoord.length; i++) {
                        if (mineCoord[i].getX() == part[1] && mineCoord[i].getY() == part[0]) {
                            minesFound++;
                            mineNotThere = false;
                        }
                    }
                    if(mineNotThere){
                        fakeFound++;
                    }
                    board.drawBoard(field);

                } else if((field[part[1]][part[0]].equals("*"))) {
                    field[part[1]][part[0]] = ".";
                    for (int i = 0; i < mineCoord.length; i++) {
                        if (mineCoord[i].getX() == part[1] && mineCoord[i].getY() == part[0]) {
                            minesFound--;
                            mineNotThere = false;
                        }
                    }
                    if(mineNotThere){
                        fakeFound--;
                    }
                    board.drawBoard(field);
                }
                if (minesFound == mineCoord.length && fakeFound == 0) {
                    System.out.println("Congratulations! You found all mines!");
                    break;
                }
            }
        }
    }

    public static class Board {
        class Coordinates {
            private int x;
            private int y;

            Coordinates(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public int getX() {
                return this.x;
            }

            public int getY() {
                return this.y;
            }
        }

        private Coordinates[] minePlacements;

        String[][] createField(int mines) {

            int size = 9;

            String[][] field = new String[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    field[i][j] = ".";
                }
            }
            Random rand = new Random();
            this.minePlacements = new Coordinates[mines];
            int brojac = 0;
            while (mines != 0) {
                int i = rand.nextInt(size);
                int j = rand.nextInt(size);
                if (!field[i][j].equals("X")) {
                    minePlacements[brojac] = new Coordinates(i, j);
                    field[i][j] = "X";
                    mines--;
                    brojac++;
                }
            }

            addingMarkers(field, minePlacements);

            for (Coordinates minePlacement : minePlacements) {
                field[minePlacement.getX()][minePlacement.getY()] = ".";
            }

            return field;
        }

        public Coordinates[] getMines() {
            return this.minePlacements;
        }

        private void addingMarkers(String[][] field, Coordinates[] minePlacements) {

            for (Coordinates minePlacement : minePlacements) {
                int x = minePlacement.getX();
                int y = minePlacement.getY();

                //CORNERS
                int MAX_XY = field.length - 1;
                int MIN_XY = 0;

                // IF x ISN'T CORNERED AT LEFT SIDE
                if (x != MIN_XY) {
                    field[x - 1][y] = setMarker(field[x - 1][y]);

                    // IF x ISN'T CORNERED AT LEFT DOWN SIDE
                    if (y != MIN_XY) {
                        field[x - 1][y - 1] = setMarker(field[x - 1][y - 1]);
                    }
                    // IF x ISN'T CORNERED AT LEFT UP SIDE
                    if (y != MAX_XY) {
                        field[x - 1][y + 1] = setMarker(field[x - 1][y + 1]);
                    }
                }
                // IF x ISN'T CORNERED AT RIGHT SIDE
                if (x != MAX_XY) {
                    field[x + 1][y] = setMarker(field[x + 1][y]);

                    //IF x ISN'T CORNERED AT RIGHT DOWN SIDE
                    if (y != MIN_XY) {
                        field[x + 1][y - 1] = setMarker(field[x + 1][y - 1]);
                    }
                    //IF x ISN'T CORNERED AT RIGHT UP SIDE
                    if (y != MAX_XY) {
                        field[x + 1][y + 1] = setMarker(field[x + 1][y + 1]);
                    }
                }
                //IF x ISN'T CORNERED UP
                if (y != MAX_XY) {
                    field[x][y + 1] = setMarker(field[x][y + 1]);
                }
                //IF x ISN'T CORNERED DOWN
                if (y != MIN_XY) {
                    field[x][y - 1] = setMarker(field[x][y - 1]);
                }
            }
        }

        private String setMarker(String placement) {
            if (placement.equals("X")) {
                return placement;
            }
            if (placement.equals(".")) {
                placement = "1";
            } else {
                placement = String.valueOf(Integer.parseInt(placement) + 1);
            }
            return placement;
        }

        void drawBoard(String[][] board) {
            int red = 1;
            System.out.print(" |");
            for (int i = 0; i < board.length; i++) {
                System.out.print(red++);
            }
            System.out.println("|");
            System.out.print("-|");
            for (int i = 0; i < board.length; i++) {
                System.out.print("-");
            }
            System.out.println("|");
            red = 1;
            for (String[] strings : board) {
                for (int j = 0; j < board.length; j++) {
                    if (j == 0) {
                        System.out.print(red + "|");
                        red++;
                    }
                    System.out.print(strings[j]);
                    if (j == board.length - 1) {
                        System.out.print("|");
                    }
                }
                System.out.print("\n");
            }
            System.out.print("-|");
            for (int i = 0; i < board.length; i++) {
                System.out.print("-");
            }
            System.out.println("|");


        }
    }
    public static void main(String[] args) {
        // write your code here
        UI ui = new UI();
        ui.start();
    }
}


