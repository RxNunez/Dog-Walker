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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dog dog = (Dog) o;

        if (id != dog.id) return false;
        if (walkerId != dog.walkerId) return false;
        if (dogName != null ? !dogName.equals(dog.dogName) : dog.dogName != null) return false;
        if (breed != null ? !breed.equals(dog.breed) : dog.breed != null) return false;
        return color != null ? color.equals(dog.color) : dog.color == null;
    }

    @Override
    public int hashCode() {
        int result = dogName != null ? dogName.hashCode() : 0;
        result = 31 * result + (breed != null ? breed.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + walkerId;
        return result;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalkerId() {
        return walkerId;
    }

    public void setWalkerId(int walkerId) {
        this.walkerId = walkerId;
    }


}
