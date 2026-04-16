package dev.java10x.elifoot.entity;

public enum Position {

    GOALKEEPER("Goalkeeper"),
    DEFENDER("Defender"),
    FULLBACK("Fullback"),
    MIDFIELDER("MidFilder"),
    FORWARD("Forward");

    private final String label;

    Position(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
