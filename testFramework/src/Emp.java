package model;

import java.util.HashMap;
import java.util.Vector;

import annotation.Url;
import annotation.ParameterName;
import modelView.ModelView;
import fileUpload.FileUpload;
import annotation.Scope;
import annotation.Auth;
import annotation.Session;
import annotation.RestAPI;

public class Emp{
    int id;
    String nom;
    double salaire;
    FileUpload photo;
    HashMap<String , Object> session;

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

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
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
        System.out.println("object: " + this);
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

    @Url( url = "/login.do")
    public ModelView login(){
        ModelView mv = new ModelView("accueil.jsp");
        mv.addSession("user", "Jean");
        mv.addSession("profil", "admin");
        mv.addSession("isConnected", true);
        return mv;
    }

    @Url( url = "/authentifier.do")
    @Auth(profil = "admin")
    public ModelView checkAuthentification(){
        ModelView mv = new ModelView("authentified.jsp");
        return mv;
    }

    @Session()
    @Url( url = "/test-session.do")
    public ModelView testSession(){
        HashMap<String,Object>  session = this.getSession();
        String sessionValue =  String.valueOf(session.get("user"));
        ModelView mv = new ModelView("testSession.jsp");
        mv.addItem("user", sessionValue);
        return mv;
    }   

    @RestAPI()
    @Url( url = "/emp-all-json.do")
    public Vector<Emp> find_all_json(){
        Vector<Emp> listEmp = new Vector();
        listEmp.add(new Emp(1 , "Rakoto" ,  2000));
        
        listEmp.add(new Emp(2 , "Rabe" ,  2000));

        listEmp.add(new Emp(3 , "Rasoa" ,  2000));
        
        return listEmp;
        // System.out.println("findAll");
    }

    @Url( url = "/logout.do")
    public ModelView logout(){
        ModelView mv = new ModelView("logout.jsp");
        mv.setInvalidateSession(true);
        return mv;
    }

    @Url( url = "/deleteSession.do")
    public ModelView deleteSession(){
        ModelView mv = new ModelView("logout.jsp");
        mv.addListRemoveSession("user");
        return mv;
    }

    // public ModelView login(String nom , String mdp){
    //     ModelView mv.addsession("isConnected" , true);
    //     ModelView mv.addsession("admin" , true);
    // }
}