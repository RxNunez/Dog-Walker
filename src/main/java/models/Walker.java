package models;

import java.util.ArrayList;
import java.util.List;

public class Walker {

    private String walkerName;
    private boolean completed;
    private int id;




    public Walker(String walkerName) {
        this.walkerName = walkerName;
        this.completed = false;


    }
    public void setWalkerName (String walkerName) {
        this.walkerName = walkerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getWalkerName() {
        return this.walkerName;
    }

    public boolean getCompleted(){
        return this.completed;
    }

    public int getId() {
        return this.id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Walker walker = (Walker) o;

        if (completed != walker.completed) return false;
        if (id != walker.id) return false;
        return walkerName.equals(walker.walkerName);
    }

    @Override
    public int hashCode() {
        int result = walkerName.hashCode() ;
        result = 31 * result + (completed ? 1 : 0);
        result = 31 * result + id;
        return result;
    }

}



