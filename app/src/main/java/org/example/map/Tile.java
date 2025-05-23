package org.example.map;

public class Tile {
    private boolean isUsable;
    private char deployedObjectChar = '.'; 

    public Tile() {
        this.isUsable = true;
        this.deployedObjectChar = '.'; 
    }

    public void setUsable(boolean usable) { isUsable = usable; }
    public char getDeployedObjectChar() { return deployedObjectChar; }

    public void setDeployedObjectChar(char deployedObjectChar) {
        this.deployedObjectChar = deployedObjectChar;
        this.isUsable = isTillable() || isTilled() || isPlanted() || isHarvestable();
    }

    public boolean isUsable() { return isUsable; }
    public boolean isTillable() { return deployedObjectChar == '.'; }
    public boolean isTilled() { return deployedObjectChar == 't'; }
    public boolean isPlanted() { return deployedObjectChar == 'l'; }
    public boolean isHarvestable() { return deployedObjectChar == 'r'; }

    @Override
    public String toString() {
        return String.valueOf(deployedObjectChar);
    }
}
