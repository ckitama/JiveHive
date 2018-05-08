import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServeriRakendus implements ServeriLiides {

    private static Registry registry;
    private HashMap<String, StringBuilder> kasutajateLogid = new HashMap<>();
    private List<String> kasutajateList = new ArrayList<>();


    public ServeriRakendus() throws RemoteException, AlreadyBoundException {
        ServeriLiides stub = (ServeriLiides) UnicastRemoteObject.exportObject(this, 0);
        registry = LocateRegistry.getRegistry();
        registry.bind("ServeriRakendus", stub);
        System.out.println("Ühendus olemas.");
    }

    public void lõpeta() throws RemoteException, NotBoundException {
        UnicastRemoteObject.unexportObject(this, true);
        registry.unbind("ServeriRakendus");
        System.out.println("ServeriRakendus pandi kinni.");
    }

    @Override
    public String getKasutajaLogi(String kasutajanimi) throws RemoteException {
        return kasutajateLogid.get(kasutajanimi).toString();
    }

    @Override
    public String getKasutajateListSõnena() throws RemoteException {
        StringBuilder kasutajadSõnena = new StringBuilder();
        for (String kasutaja : kasutajateLogid.keySet()) {
            kasutajadSõnena.append(kasutaja + "\n");
        }
        return kasutajadSõnena.toString();
    }

    @Override
    public List<String> getKasutajateList() throws RemoteException {
        return kasutajateList;
    }

    @Override
    public void sisene(String kasutajanimi) throws RemoteException {
        kirjutaTeade("\\*Kasutaja " + kasutajanimi + " sisenes tuppa*/");
        StringBuilder tervitus = new StringBuilder("\\*Tere tulemast JiveHive jututuppa, " + kasutajanimi + "!*/\n\n");
        kasutajateLogid.put(kasutajanimi, tervitus);
        kasutajateList.add(kasutajanimi);
    }

    @Override
    public void siseneUuesti(String kasutajanimi, StringBuilder vestlus) throws RemoteException {
        kasutajateLogid.put(kasutajanimi, vestlus);
        kasutajateList.add(kasutajanimi);
    }

    @Override
    public void kirjutaTeade(String sõnum) throws RemoteException {
        for (String kasutaja : kasutajateLogid.keySet()) {
            kasutajateLogid.get(kasutaja).append(sõnum + "\n");
        }
    }

    @Override
    public void kirjutaKasutajalt(String kasutajanimi, String sõnum) throws RemoteException {
        for (String kasutaja : kasutajateLogid.keySet()) {
            kasutajateLogid.get(kasutaja).append("[" + kasutajanimi + "]: " + sõnum + "\n");
        }
    }

    @Override
    public void lahku(String kasutajanimi) throws RemoteException {
        kasutajateLogid.remove(kasutajanimi);
        kasutajateList.remove(kasutajanimi);
        kirjutaTeade(kasutajanimi + " lahkus toast.");
    }

    @Override
    public void lahkuAjutiselt(String kasutajanimi) throws RemoteException {
        kasutajateLogid.remove(kasutajanimi);
        kasutajateList.remove(kasutajanimi);
    }

}