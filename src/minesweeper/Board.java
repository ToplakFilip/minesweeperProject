package minesweeper;

import java.util.Random;

public class Board {

    private Coordinates[] minePlacements;
    private String[][] numberedField;
    protected String[][] playingField;
    protected int emptytiles;

    private int minesFound = 0;
    private int fakeFound = 0;

    void createField(int mines) {
        int size = 9;
        this.playingField = new String[size][size];
        this.numberedField = new String[size][size];
        emptytiles = size * size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.playingField[i][j] = "-";
                this.numberedField[i][j] = "/";
            }
        }

        Random rand = new Random();
        this.minePlacements = new Coordinates[mines];
        int brojac = 0;
        emptytiles -= mines;
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
        if (placement.equals("/")) {
            placement = "1";
        } else {
            placement = String.valueOf(Integer.parseInt(placement) + 1);
        }
        return placement;
    }

    void drawBoard() {
        int red = 1;
        //ROOF
        System.out.print(" [MINEFINDERS] ");

        //TOP LAYER
        System.out.print("\n");
        System.out.print("/| ");
        for (int i = 0; i < playingField.length; i++) {
            System.out.print(red++);
        }
        System.out.println(" |\\");
        red = 1;
        //GRID
        for (String[] strings : playingField) {
            for (int j = 0; j < playingField.length; j++) {
                if (j == 0) {
                    System.out.print(red + "| ");
                    red++;
                }
                System.out.print(strings[j]);
                if (j == playingField.length - 1) {
                    System.out.print(" ||");
                }
            }
            System.out.print("\n");
        }
        //BOTTOM LAYER
        System.out.print("||=");
        for (int i = 0; i < playingField.length; i++) {
            System.out.print("=");
        }
        System.out.print("=||\n\n");
    }

    protected void mark(int x, int y) {
        if (playingField[y][x].matches("[1-9]")) {
            System.out.println("   [ A NUMBER IS PLACED THERE ! ]");
        } else if (playingField[y][x].equals("/")) {
            System.out.println("   [ THAT FIELD IS EMPTY ! ]");
        } else if (playingField[y][x].equals("*")) {
            markremoval(x, y);
        } else if (playingField[y][x].equals("-")) {
            playingField[y][x] = "*";

            if (numberedField[y][x].equals("X")) {
                minesFound++;
            } else {
                fakeFound++;
            }

            drawBoard();
        }
    }

    protected boolean explore(int x, int y) {
        if (binarySearch(minePlacements, x, y)) {
            return true;
        }
        if (playingField[y][x].matches("[1-9]")) {
            System.out.println("   [ A NUMBER IS PLACED THERE ! ]");
        } else if (playingField[y][x].equals("/")) {
            System.out.println("   [ THAT FIELD IS EMPTY ! ]");
        } else {
            openingFields(x, y);
            System.out.println("BROJ PRAZNIH POLJA " + emptytiles);
            System.out.println("BROJ KRIVIH POLJA " + fakeFound);
            System.out.println("BROJ DOBRIH POLJA " + minesFound);
            drawBoard();
        }
        return false;

    }

    private boolean isInteger(String a) {
        return a.matches("[1-9]");
    }

    private void openingFields(int x, int y) {
        int Xc = x;
        int Yc = y;
        if (playingField[y][x].equals("*")) {
            markremoval(x, y);
        }
        if (isInteger(numberedField[Yc][Xc])) {
            playingField[Yc][Xc] = numberedField[Yc][Xc];
            emptytiles--;
        } else {
            playingField[Yc][Xc] = "/";
            emptytiles--;
            proxy(Xc, Yc);
        }
    }

    private void proxy(int Xc, int Yc) {
        goingUp(Xc, Yc);
        goingDown(Xc, Yc);
        goingLeft(Xc, Yc);
        goingRight(Xc, Yc);
    }

    private void goingRight(int x, int y) {
        while (x < numberedField.length - 1) {
            x++;
            if (playingField[y][x].equals("/")) break;
            if (playingField[y][x].equals("*")) {
                markremoval(x, y);
            }
            if (isInteger(playingField[y][x])) emptytiles++;
            playingField[y][x] = numberedField[y][x];
            emptytiles--;
            if (isInteger(numberedField[y][x])) {
                break;
            }

            proxy(x, y);
        }
    }

    private void goingLeft(int x, int y) {
        while (x > 0) {
            x--;
            if (playingField[y][x].equals("/")) break;
            if (playingField[y][x].equals("*")) {
                markremoval(x, y);
            }
            if (isInteger(playingField[y][x])) emptytiles++;
            playingField[y][x] = numberedField[y][x];
            emptytiles--;
            if (playingField[y][x].equals("*")) {
                markremoval(x, y);
            }
            if (isInteger(numberedField[y][x])) {
                break;
            }
            proxy(x, y);
        }
    }

    private void goingUp(int x, int y) {
        while (y > 0) {
            y--;
            if (playingField[y][x].equals("/")) break;
            if (playingField[y][x].equals("*")) {
                markremoval(x, y);
            }
            if (isInteger(playingField[y][x])) emptytiles++;
            playingField[y][x] = numberedField[y][x];
            emptytiles--;
            if (isInteger(numberedField[y][x])) {
                break;
            }
            proxy(x, y);
        }
    }

    private void goingDown(int x, int y) {
        while (y < numberedField.length - 1) {
            y++;
            if (playingField[y][x].equals("/")) break;
            if (playingField[y][x].equals("*")) {
                markremoval(x, y);
            }
            if (isInteger(playingField[y][x])) emptytiles++;
            playingField[y][x] = numberedField[y][x];
            emptytiles--;
            if (isInteger(numberedField[y][x])) {
                break;
            }
            proxy(x, y);
        }
    }

    public boolean binarySearch(Coordinates arr[], int x, int y) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m].getX() == y) {
                if (arr[m].getY() == x) {
                    return true;
                }
            }
            if (arr[m].getX() < y) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return false;
    }

    private void markremoval(int x, int y) {
        if (numberedField[y][x].equals("X")) {
            minesFound--;
        } else {
            fakeFound--;
        }
    }

    protected void gameOver() {
        for (int i = 0; i < minePlacements.length; i++) {
            playingField[minePlacements[i].getX()][minePlacements[i].getY()] = "X";
        }
        drawBoard();
    }

    protected boolean winConditionCheck(){
        if(emptytiles == 0)return true;

        if(fakeFound == 0 && minesFound == minePlacements.length) return true;

        return false;
    }
}
