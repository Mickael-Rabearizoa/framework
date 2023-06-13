package model;

import java.util.Vector;

import annotation.Url;
import annotation.ParameterName;
import modelView.ModelView;
import fileUpload.FileUpload;

public class Emp{
    int id;
    String nom;
    double salaire;
    FileUpload photo;

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

    public FileUpload getPhoto() {
        return photo;
    }

    public void setPhoto(FileUpload photo) {
        this.photo = photo;
    }

    // public void setSalaire(String salaire){
    //     this.setSalaire(Double.parseDouble(salaire));
    // }

    @Url( url = "/emp-all.do")
    public ModelView find_all(){
        Vector<Emp> listEmp = new Vector();
        listEmp.add(new Emp(1 , "Rakoto" ,  2000));
        
        listEmp.add(new Emp(2 , "Rabe" ,  2000));

        listEmp.add(new Emp(3 , "Rasoa" ,  2000));
        ModelView mv = new ModelView("page.jsp");
        mv.addItem("listEmp" , listEmp);
        return mv;
        // System.out.println("findAll");
    }

    @Url( url = "/emp-add.do")
    public void add(){
        System.out.println("add");
    }

    @Url( url = "/emp-save.do")
    public ModelView save(){
        System.out.println(this.getId());
        System.out.println(this.getNom());
        System.out.println(this.getSalaire());
        System.out.println(this.getPhoto().getName());
        System.out.println(this.getPhoto().getBytes()[0]);
        System.out.println(this.getPhoto().getBytes()[1]);
        return null;
    }

    @Url( url = "/detail-emp.do")
    public ModelView detailEmp(@ParameterName( parameterName = "id" ) int id){
        Emp employe = this.findById(id);
        ModelView mv = new ModelView("details.jsp");
        mv.addItem("employe" , employe);
        return mv;
    }

    public Emp findById(int id){
        return new Emp(id , "Anarana" , 6780);
    }

    // public ModelView login(String nom , String mdp){
    //     ModelView mv.addsession("isConnected" , true);
    //     ModelView mv.addsession("admin" , true);
    // }
}