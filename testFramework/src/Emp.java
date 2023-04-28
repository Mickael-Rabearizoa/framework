package model;

import java.util.Vector;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // public void setId(String id){
    //     this.setId(Integer.parseInt(id));
    // }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // public void setNom(Object nom) {
    //     this.setNom(String.valueOf(nom));
    // }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    // public void setSalaire(String salaire){
    //     this.setSalaire(Double.parseDouble(salaire));
    // }

    @Url( url = "/emp-all")
    public ModelView find_all(){
        Vector<Emp> listEmp = new Vector();
        // listEmp.add(new Emp(1 , "Rakoto" ,  2000));
        
        // listEmp.add(new Emp(2 , "Rabe" ,  2000));

        // listEmp.add(new Emp(3 , "Rasoa" ,  2000));
        ModelView mv = new ModelView("page.jsp");
        mv.addItem("listEmp" , listEmp);
        return mv;
        // System.out.println("findAll");
    }

    @Url( url = "/emp-add")
    public void add(){
        System.out.println("add");
    }

    @Url( url = "/emp-save")
    public ModelView save(){
        System.out.println(this.getId());
        System.out.println(this.getNom());
        System.out.println(this.getSalaire());
        return null;
    }
}