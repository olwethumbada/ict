package com.example.onward.ict;

public class Category {
    private String CategoryID;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String Id;
    public String Name;
    public String Description;

    public Category(String Id, String Name, String Description){
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
    }
    public Category(){

    }
}
