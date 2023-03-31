package model;

import annotation.Url;
import modelView.ModelView;

public class Emp{
    int id;
    String nom;
    double salaire;

    public Emp(){
        
    }

    public Emp(int id, String nom , double salaire){
        this.id = id;
        this.nom = nom;
        this.salaire = salaire;
    }

    @Url( url = "/emp-all")
    public ModelView find_all(){
        return new ModelView("page.jsp");
        // System.out.println("findAll");
    }

    @Url( url = "/emp-add")
    public void add(){
        System.out.println("add");
    }
}