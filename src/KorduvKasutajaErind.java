import javafx.scene.control.Alert;

public class KorduvKasutajaErind extends RuntimeException {

    public KorduvKasutajaErind() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JiveHive");
        alert.setHeaderText("Selline kasutajanimi on juba võetud!");
        alert.showAndWait();
    }
}
