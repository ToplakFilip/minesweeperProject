package minesweeper;

import java.util.Random;

public class Board {
    static class Coordinates {
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
    private String[][] numberedField;
    protected String[][] playingField;

    String[][] createField(int mines) {

        int size = 9;

        String[][] field = new String[size][size];
        this.numberedField = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = "-";
                this.numberedField[i][j] = "-";
            }
        }

        Random rand = new Random();
        this.minePlacements = new Coordinates[mines];
        int brojac = 0;
        while (mines != 0) {
            int i = rand.nextInt(size);
            int j = rand.nextInt(size);
            if (!numberedField[i][j].equals("X")) {
                minePlacements[brojac] = new Coordinates(i, j);
                this.numberedField[i][j] = "X";
                mines--;
                brojac++;
            }
        }
        addingMarkers(this.numberedField, minePlacements);
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
        if (placement.equals("-")) {
            placement = "1";
        } else {
            placement = String.valueOf(Integer.parseInt(placement) + 1);
        }
        return placement;
    }

    void drawBoard(String[][] board) {
        int red = 1;
        //ROOF
        System.out.print(" [MINEFINDERS] ");

        //TOP LAYER
        System.out.print("\n");
        System.out.print("/| ");
        for (int i = 0; i < board.length; i++) {
            System.out.print(red++);
        }
        System.out.println(" |\\");
        red = 1;
        //GRID
        for (String[] strings : board) {
            for (int j = 0; j < board.length; j++) {
                if (j == 0) {
                    System.out.print(red + "| ");
                    red++;
                }
                System.out.print(strings[j]);
                if (j == board.length - 1) {
                    System.out.print(" ||");
                }
            }
            System.out.print("\n");
        }
        //BOTTOM LAYER
        System.out.print("||=");
        for (int i = 0; i < board.length; i++) {
            System.out.print("=");
        }
        System.out.print("=||\n\n");
    }

}
