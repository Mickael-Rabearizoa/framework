package utilitaires;

public class Utils{
    public static String getPath_in_URL(String url , String projectName){
        String[] firstSplit = url.split(projectName+"/"); 
        String path_in_URL = " ";
        if(firstSplit.length < 2){
            path_in_URL = "erreur";
        } else {
            path_in_URL = firstSplit[1];
        }
        // String path_in_URL = firstSplit.split("?")[0];
        return path_in_URL;
    }
}