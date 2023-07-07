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

// @Scope( value = "singleton")
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

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

    // pour tester data de ModelView
    @Url( url = "/emp-all.do")
    public ModelView find_all(){
        Vector<Emp> listEmp = new Vector();
        listEmp.add(new Emp(1 , "Rakoto" ,  2000));
        
        listEmp.add(new Emp(2 , "Rabe" ,  2000));

        listEmp.add(new Emp(3 , "Rasoa" ,  2000));
        ModelView mv = new ModelView("page.jsp");
        mv.addItem("listEmp" , listEmp);
        return mv;
    }

    // pour tester Url
    @Url( url = "/emp-add.do")
    public void add(){
        System.out.println("add");
    }

    // pour tester le passage des données de la vue vers les model
    @Url( url = "/emp-save.do")
    public ModelView save(){
        System.out.println("object: " + this);
        System.out.println(this.getId());
        System.out.println(this.getNom());
        System.out.println(this.getSalaire());
        System.out.println(this.getPhoto().getName());
        System.out.println(this.getPhoto().getBytes()[0]);
        System.out.println(this.getPhoto().getBytes()[1]);
        ModelView mv = new ModelView("save.jsp");
        mv.addItem("employe" , this);
        return mv;
    }

    // pour tester l'appel d'un fonction avec parametre
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

    // verifier l'authentification
    @Url( url = "/authentifier.do")
    @Auth(profil = "admin")
    public ModelView checkAuthentification(){
        ModelView mv = new ModelView("authentified.jsp");
        return mv;
    }

    // test d'utilisation des attributs de session
    @Session()
    @Url( url = "/test-session.do")
    public ModelView testSession(){
        HashMap<String,Object>  session = this.getSession();
        String sessionValue =  String.valueOf(session.get("user"));
        ModelView mv = new ModelView("testSession.jsp");
        mv.addItem("user", sessionValue);
        return mv;
    }   

    // test json
    @RestAPI()
    @Url( url = "/emp-all-json.do")
    public Vector<Emp> find_all_json(){
        Vector<Emp> listEmp = new Vector();
        listEmp.add(new Emp(1 , "Rakoto" ,  2000));
        
        listEmp.add(new Emp(2 , "Rabe" ,  2000));

        listEmp.add(new Emp(3 , "Rasoa" ,  2000));
        
        return listEmp;
    }

    // supprimer tous les session
    @Url( url = "/logout.do")
    public ModelView logout(){
        ModelView mv = new ModelView("logout.jsp");
        mv.setInvalidateSession(true);
        return mv;
    }

    // supprimer un attribut de session
    @Url( url = "/deleteSession.do")
    public ModelView deleteSession(){
        ModelView mv = new ModelView("logout.jsp");
        mv.addListRemoveSession("user");
        return mv;
    }

    // test singleton
    @Url( url = "/singleton.do")
    public ModelView singleton(){
        ModelView mv = new ModelView("singleton.jsp");
        System.out.println("object: " + this);
        mv.addItem("singleton" , this);
        return mv;
    }

}