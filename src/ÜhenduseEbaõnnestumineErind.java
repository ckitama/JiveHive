import javafx.scene.control.Alert;

public class ÜhenduseEbaõnnestumineErind extends RuntimeException {

    public ÜhenduseEbaõnnestumineErind() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("JiveHive");
        alert.setHeaderText("Ühendamisel tekkis mingi viga! \nProovi varsti uuesti!");
        alert.showAndWait();
    }
}
