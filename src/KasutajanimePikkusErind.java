import javafx.scene.control.Alert;

public class KasutajanimePikkusErind extends RuntimeException {

    public KasutajanimePikkusErind() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JiveHive");
        alert.setHeaderText("Kasutajanime pikkus peab olema vahemikus 1-15 tähemarki!");
        alert.showAndWait();
    }
}
