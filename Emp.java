package model;

import annotation.Url;

public class Emp{
    int id;
    String nom;
    double salaire;

    public Emp(int id, String nom , double salaire){
        this.id = id;
        this.nom = nom;
        this.salaire = salaire;
    }

    @Url( url = "/emp-all")
    public void find_all(){
        System.out.println("findAll");
    }

    @Url( url = "/emp-add")
    public void add(){
        System.out.println("add");
    }
}