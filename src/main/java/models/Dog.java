package models;

public class Dog {

    private String dogName;
    private String breed;
    private String color;
    private int walkerId;
    private int id;



    public Dog(String dogName, String breed, String color, int walkerId) {
        this.dogName = dogName;
        this.breed = breed;
        this.color = color;
        this.walkerId = walkerId;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWalkerId() {
        return walkerId;
    }

    public void setWalkerId(int walkerId) {
        this.walkerId = walkerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dog dog = (Dog) o;

        if (id != dog.id) return false;
        if (breed != dog.breed) return false;
        if (color != dog.color) return false;
        if (walkerId != dog.walkerId) return false;
        return dogName.equals(dog.dogName);
    }

    @Override
    public int hashCode() {
        int result = dogName.hashCode();
        result = 31 * result + breed.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + walkerId;
        result = 31 * result + id;
        return result;
    }




}
