package modelView;
import java.util.HashMap;
import java.util.Vector;

public class ModelView {
    private String view;
    private HashMap<String, Object> data;
    private HashMap<String, Object> session;
    private boolean isJson;
    private boolean invalidateSession;
    private Vector<String> removeSession = new Vector<>();

    public ModelView(String view){
        setView(view);
        this.data = new HashMap<String, Object>();
        this.session = new HashMap<String, Object>();
    }

    public HashMap<String, Object> getData() {
        if(this.data.isEmpty() == false){
            return data;
        } else {
            return null;
        } 
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem(String cle , Object value){
        this.data.put(cle, value);
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getSession() {
        if(this.session.isEmpty() == false){
            return session;
        } else {
            return null;
        }
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public boolean getIsJson(){
        return this.isJson;
    }

    public void setIsJson(boolean isJson) {
        this.isJson = isJson;
    }

    public boolean getInvalidateSession(){
        return this.invalidateSession;
    }

    public void setInvalidateSession(boolean value){
        this.invalidateSession = value;
    }

    public void addSession(String key, Object value){
        this.session.put(key, value);
    }

    public Vector<String> getRemoveSession() {
        return removeSession;
    }

    public void setRemoveSession(Vector<String> removeSession) {
        this.removeSession = removeSession;
    }

    public void addListRemoveSession(String key){
        this.removeSession.add(key);
    }
}
