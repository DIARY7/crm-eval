package site.easy.exception;

public class MyCsvException extends Exception {
    int numLigne;
    String nomFichier;
    String message;
    public MyCsvException(){
        
    }
    public MyCsvException(int numLigne,String nomFichier,String message){
        this.setNumLigne(numLigne);
        this.setNomFichier(nomFichier);
        this.setMessage(message);
    }


    public int getNumLigne() {
        return numLigne;
    }
    public void setNumLigne(int numLigne) {
        this.numLigne = numLigne;
    }
    public String getNomFichier() {
        return nomFichier;
    }
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
