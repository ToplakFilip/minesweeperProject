package minesweeper;

import java.util.Random;
import java.util.HashSet;
import java.util.Collection;

public class Board {

    private Coordinates[] minePlacements;
    private String[][] numberedField;
    protected String[][] playingField;
    protected int emptytiles;

    protected boolean firstMove = true;
    private int mines;

    private int minesFound = 0;
    private int fakeFound = 0;

    void createField(int mines) {
        this.mines = mines;
        int size = 9;
        this.playingField = new String[size][size];
        this.numberedField = new String[size][size];
        this.minePlacements = new Coordinates[mines];
        emptytiles = size * size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.playingField[i][j] = "-";
                this.numberedField[i][j] = "/";
            }
        }
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
            playingField[y][x] = "-";
            drawBoard();
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
        if (firstMove) {
            firstMove = false;
            firstExplore(x, y);
        }

        if (numberedField[y][x].equals("X")) {
            return true;
        }
        if (playingField[y][x].matches("[1-9]")) {
            System.out.println("   [ A NUMBER IS PLACED THERE ! ]");
        } else if (playingField[y][x].equals("/")) {
            System.out.println("   [ THAT FIELD IS EMPTY ! ]");
        } else {
            openingFields(x, y);
            drawBoard();
        }
        return false;

    }

    private boolean isInteger(String a) {
        return a.matches("[1-9]");
    }

    private void openingFields(int Xc, int Yc) {
        if (playingField[Yc][Xc].equals("*")) {
            markremoval(Xc, Yc);
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

    private void cornerCasesForY(int x, int y) {
        if (y + 1 < playingField.length - 1 && playingField[y + 1][x].matches("[-]|[*]")) {
            playingField[y + 1][x] = numberedField[y + 1][x];
            emptytiles--;
            if (numberedField[y + 1][x].equals("*")) {
                markremoval(x, y);
            }
            if (numberedField[y + 1][x].equals("/")) {
                proxy(x, y + 1);
            }
        }
        if (y - 1 > 0 && playingField[y - 1][x].matches("[-]|[*]")) {
            playingField[y - 1][x] = numberedField[y - 1][x];
            emptytiles--;
            if (numberedField[y - 1][x].equals("*")) {
                markremoval(x, y);
            }
            if (numberedField[y - 1][x].equals("/")) {
                proxy(x, y - 1);
            }
        }
    }

    private void cornerCasesForX(int x, int y) {

        if (x + 1 < playingField.length - 1 && playingField[y][x + 1].matches("[-]|[*]")) {
            playingField[y][x + 1] = numberedField[y][x + 1];
            emptytiles--;
            if (numberedField[y][x + 1].equals("*")) {
                markremoval(x, y);
            }
            if (numberedField[y][x + 1].equals("/")) {
                proxy(x + 1, y);
            }
        }
        if (x - 1 > 0 && playingField[y][x - 1].matches("[-]|[*]")) {
            playingField[y][x - 1] = numberedField[y][x - 1];
            emptytiles--;
            if (numberedField[y][x - 1].equals("*")) {
                markremoval(x, y);
            }
            if (numberedField[y][x - 1].equals("/")) {
                proxy(x - 1, y);
            }
        }
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
                cornerCasesForY(x, y);
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
            if (isInteger(numberedField[y][x])) {
                cornerCasesForY(x, y);
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
                cornerCasesForX(x, y);
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
                cornerCasesForX(x, y);
                break;
            }
            proxy(x, y);
        }
    }

    private void markremoval(int x, int y) {
        if (numberedField[y][x].equals("X")) {
            minesFound--;
        } else {
            fakeFound--;
        }
    }

    protected void gameOver() {
        for (Coordinates minePlacement : minePlacements) {
            playingField[minePlacement.getX()][minePlacement.getY()] = "X";
        }
        drawBoard();
    }

    protected boolean winConditionCheck() {
        if (emptytiles == 0) return true;
        return fakeFound == 0 && minesFound == minePlacements.length;
    }

    private void firstExplore(int x, int y) {
        Random rand = new Random();
        int mines = this.mines;
        int brojac = 0;
        emptytiles -= mines;
        Collection<Coordinates> alreadyChosenCoord = new HashSet<>();
        alreadyChosenCoord.add(new Coordinates(x, y));

        while (mines != 0) {
            Coordinates cd = getNextUnusedCoord(rand, alreadyChosenCoord);

            if (!numberedField[cd.getY()][cd.getX()].equals("X")) {
                if (cd.getY() != y || cd.getX() != x) {
                    minePlacements[brojac] = new Coordinates(cd.getY(), cd.getX());
                    this.numberedField[cd.getY()][cd.getX()] = "X";

                    if (playingField[cd.getY()][cd.getX()].equals("*")) {
                        minesFound++;
                        fakeFound--;
                    }
                    mines--;
                    brojac++;

                }
            }
        }
        addingMarkers(this.numberedField, minePlacements);
    }

    private Coordinates getNextUnusedCoord(Random rand, Collection<Coordinates> alreadyChosenCoord) {
        int i = 0;
        int j = 0;

        boolean unique = false;
        while (!unique) {
            i = rand.nextInt(playingField.length);
            j = rand.nextInt(playingField.length);
            unique = !alreadyChosenCoord.contains(new Coordinates(i, j));
        }
        alreadyChosenCoord.add(new Coordinates(i, j));

        return new Coordinates(i, j);
    }

}
