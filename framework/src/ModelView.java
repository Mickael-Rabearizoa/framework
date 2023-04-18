package modelView;
import java.util.HashMap;

public class ModelView {
    private String view;
    private HashMap<String, Object> data;

    public ModelView(String view){
        setView(view);
        this.data = new HashMap<String, Object>();
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
}
