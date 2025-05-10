package org.example;

public class Opponent {
    private final String name; private final double strength;
    public Opponent(String name,double strength){this.name=name;this.strength=strength;}
    public String getName(){return name;} public double getStrength(){return strength;}
    @Override public String toString(){return name;}
}