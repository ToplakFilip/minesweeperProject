package minesweeper;

public class Coordinates {
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


    public boolean equals(Object compared) {

        if (compared == this) {
            return true;
        }

        if (!(compared instanceof Coordinates)) {
            return false;
        }

        Coordinates c = (Coordinates) compared;

        return x == c.x
                && y == c.y;
    }
}
