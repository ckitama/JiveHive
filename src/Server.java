import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.net.Inet4Address;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Server extends Application {

    private ServeriRakendus serveriRakendus;
    private ColorPicker colorPicker = new ColorPicker();


    @Override
    public void start(Stage primaryStage) throws Exception {

        // väline konteiner
        BorderPane borderPane = new BorderPane();

        // logo
        Image logo = new Image("jivehive_logo_small.png");
        ImageView imageView = new ImageView(logo);
        borderPane.setTop(imageView);

        // konteiner serveri staatuse, ip aadressi ja nupu jaoks
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(40,0,0,0));
        Label ühenduseLabel = new Label(" Ühendust pole. ");
        colorPicker.setValue(Color.DARKRED);
        // bindime selleks, et ülekatta css labeli stiil
        ühenduseLabel.textFillProperty().bind(colorPicker.valueProperty());

        // konteiner ip aadressi labeli ja sisestuskasti jaoks
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        Label ipLabel = new Label(" Serveri IP: ");
        TextField ip = new TextField(Inet4Address.getLocalHost().getHostAddress());
        hBox.getChildren().addAll(ipLabel, ip);

        // nupp vahetub vastavalt sellele, kas võimalik on ühendada või ühendust katkestada
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        Button ühenda = new Button("Ühenda");
        Button katkesta = new Button("Katkesta ühendus");
        stackPane.getChildren().addAll(katkesta, ühenda);
        katkesta.setVisible(false);

        // ühendamise event
        ühenda.setOnMouseClicked(event -> {
            try {
                serveriRakendus = new ServeriRakendus();
            } catch (RemoteException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            } catch (AlreadyBoundException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            }

            ühenda.setVisible(false);
            ühenduseLabel.setText(" Ühendus olemas. ");
            colorPicker.setValue(Color.DARKGREEN);
            ip.setEditable(false);
            katkesta.setVisible(true);
        });

        // ühenduse katkestamise event
        katkesta.setOnMouseClicked(event -> {
            try {
                serveriRakendus.lõpeta();
            } catch (RemoteException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            } catch (NotBoundException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            }
            katkesta.setVisible(false);
            ühenduseLabel.setText(" Ühendust pole. ");
            colorPicker.setValue(Color.DARKRED);
            ip.setEditable(true);
            ühenda.setVisible(true);
        });

        vBox.getChildren().addAll(ühenduseLabel, hBox, stackPane);

        borderPane.setCenter(vBox);

        // akna sulgemine
        primaryStage.setOnCloseRequest(event -> {
            // koristame serveri tagant vaid siis, kui ühendus on aktiivne
            if (ühenduseLabel.getText().equals(" Ühendus olemas. ")) {
            try {
                serveriRakendus.lõpeta();
            } catch (RemoteException | NotBoundException e) {
                System.out.println("Serveri maha registreerimisel tekkis viga: " + e);
            }
        }});



    Scene stseen = new Scene(borderPane, 350, 450);
        stseen.getStylesheets().add("JiveHiveStylesheet.css");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setScene(stseen);
        primaryStage.setTitle("JiveHive Server");
        primaryStage.setResizable(false);
        primaryStage.show();

}

    public static void main(String[] args) {
        launch(args);
    }
}
