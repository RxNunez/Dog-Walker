package models;

public class Dog {

    private String dogName;
    private String breed;
    private String color;
    private int id;
    private int walkerId;

    public Dog(String dogName, String breed, String color, int walkerId) {
        this.dogName = dogName;
        this.breed = breed;
        this.color = color;
        this.walkerId = walkerId;
    }
}
