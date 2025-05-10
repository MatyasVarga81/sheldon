package org.example;

import java.util.*;
public enum Move {
    KO("kő", "/images/rock.png"),
    PAPIR("papír", "/images/paper.png"),
    OLLO("olló", "/images/scissors.png"),
    GYIK("gyík", "/images/lizard.png"),
    SPOCK("spock", "/images/spock.png");
    private final String name;
    private final String imagePath;
    Move(String name, String imagePath) { this.name=name; this.imagePath=imagePath; }
    public String getName(){return name;} public String getImagePath(){return imagePath;}
    public List<Move> beatsList(){ switch(this){
        case KO:    return Arrays.asList(OLLO, GYIK);
        case PAPIR: return Arrays.asList(KO, SPOCK);
        case OLLO:  return Arrays.asList(PAPIR, GYIK);
        case GYIK:  return Arrays.asList(PAPIR, SPOCK);
        case SPOCK: return Arrays.asList(KO, OLLO);
        default:    return Collections.emptyList();
    }}
    public boolean beats(Move other){return beatsList().contains(other);}
}