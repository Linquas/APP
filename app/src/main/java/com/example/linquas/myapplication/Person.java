package com.example.linquas.myapplication;

/**
 * Created by Linquas on 2015/10/29.
 */
public class Person {
    private int id;
    private    String name;
    private    int age;


    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
