package com.nullprogram.chess;

/**
 * Represents a position on a Chess board.
 */
public class Position implements Comparable<Position> {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position pos, int deltax, int deltay) {
        this(pos.x + deltax, pos.y + deltay);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Check if Position objects are equal.
     *
     * @param pos position to be compared
     * @return    true if the positions are equal
     */
    public boolean equals(Position pos) {
        return (pos != null) && (pos.x == x) && (pos.y == y);
    }

    /**
     * Check if object is equal to this Position.
     *
     * @param pos position to be compared
     * @return    true if the positions are equal
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return equals((Position)o);
    }

    /**
     * Compare two Position objects.
     *
     * @param pos position to be compared
     * @return    positive if more, negative if less, zero if equal
     */
    public int compareTo(Position pos) {
        if (pos.y == y) {
            return x - pos.x;
        } else {
            return y - pos.y;
        }
    }
}
