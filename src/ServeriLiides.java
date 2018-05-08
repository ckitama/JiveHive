import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServeriLiides extends Remote {


    void sisene(String kasutajanimi) throws RemoteException;

    void siseneUuesti(String kasutajanimi, StringBuilder vestlus) throws RemoteException;

    void kirjutaTeade(String sõnum) throws RemoteException;

    void kirjutaKasutajalt(String kasutajanimi, String sõnum) throws RemoteException;

    void lahku(String kasutajanimi) throws RemoteException;

    void lahkuAjutiselt(String kasutajanimi) throws RemoteException;

    String getKasutajaLogi(String kasutajanimi) throws RemoteException;

    String getKasutajateListSõnena() throws RemoteException;

    List<String> getKasutajateList() throws RemoteException;
}