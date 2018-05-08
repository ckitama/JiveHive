import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Optional;

import static java.rmi.registry.LocateRegistry.getRegistry;

public class JiveHive extends Application {

    private Registry registry;
    private ServeriLiides stub;
    private String kasutajanimi;


    @Override
    public void start(Stage stage) {


        Stage sisselogimine = new Stage();
        BorderPane sisselogimiseBp = new BorderPane();

        Image logo = new Image("jivehive_logo_small.png");
        ImageView imageView = new ImageView(logo);
        sisselogimiseBp.setTop(imageView);


        VBox välineVBox = new VBox();

        HBox konteinerVBoxidele = new HBox();
        VBox labeliteVBox = new VBox();
        Label serverLabel = new Label(" Serveri IP: ");
        Label kasutajanimeLabel = new Label(" Kasutajanimi: ");
        labeliteVBox.getChildren().addAll(serverLabel, kasutajanimeLabel);
        labeliteVBox.setSpacing(16);
        labeliteVBox.setAlignment(Pos.CENTER_LEFT);

        VBox sisestusteVBox = new VBox();
        TextField server = new TextField();
        TextField kasutaja = new TextField();
        sisestusteVBox.getChildren().addAll(server, kasutaja);
        sisestusteVBox.setSpacing(10);

        konteinerVBoxidele.getChildren().addAll(labeliteVBox, sisestusteVBox);
        konteinerVBoxidele.setAlignment(Pos.CENTER);
        konteinerVBoxidele.setSpacing(10);

        Button ühenda = new Button("Ühenda");
        välineVBox.getChildren().addAll(konteinerVBoxidele, ühenda);
        välineVBox.setAlignment(Pos.CENTER);
        välineVBox.setSpacing(40);
        ühenda.setDefaultButton(true);

        sisselogimiseBp.setCenter(välineVBox);


        // edukal ühendamisel avame jututoa akna
        ühenda.setOnAction(jututoaEvent -> {

            String host = server.getText();

            // ühendame serveriga
            try {
                registry = getRegistry(host);
            } catch (RemoteException e) {
                // juhul kui sisestada mingi vale ip aadress, siis programm mõtleb päris kaua enne, kui errori viskab
                throw new ÜhenduseEbaõnnestumineErind();
            }

            try {
                stub = (ServeriLiides) registry.lookup("ServeriRakendus");
            } catch (RemoteException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            } catch (NotBoundException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            }


            kasutajanimi = kasutaja.getText();
            while (true) {
                if (kasutajanimi.length() < 1 || kasutajanimi.length() > 15) {
                    kasutaja.requestFocus();
                    kasutaja.selectAll();
                    throw new KasutajanimePikkusErind();
                } else break;
            }
            try {
                if (stub.getKasutajateList().contains(kasutajanimi)) {
                    kasutaja.requestFocus();
                    kasutaja.selectAll();
                    throw new KorduvKasutajaErind();
                }
            } catch (RemoteException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            }


            try {
                stub.sisene(kasutajanimi);
            } catch (RemoteException e) {
                throw new ÜhenduseEbaõnnestumineErind();
            }


            // juur
            BorderPane borderPane = new BorderPane();

            // konteiner horisontaalsete elementide hoidmiseks
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(10, 20, 30, 20));

            // logo
            borderPane.setTop(imageView);


            // vestlus kast
            TextArea vestlus = new TextArea();
            vestlus.setEditable(false);
            vestlus.setWrapText(true);
            // vestluse kast moodustab alati 70% akna kõrgusest
            vestlus.prefHeightProperty().bind(stage.heightProperty().multiply(0.70));


            // kasutaja sisestuse ala
            TextArea sisend = new TextArea();


            // vasakpoolne vertikaalne elementide konteiner
            VBox vasakVBox = new VBox();
            vasakVBox.setSpacing(10);
            // vasakpoolne vBox moodustab alati 75% akna laiusest
            vasakVBox.prefWidthProperty().bind(stage.widthProperty().multiply(0.75));


            vasakVBox.getChildren().addAll(vestlus, sisend);


            // ühendunud kasutajate kuvamine
            TextArea kasutajad = new TextArea();

            kasutajad.setEditable(false);
            // kasutajate kasti kõrgus sõltub vestlus kasti kõrgusest
            kasutajad.prefHeightProperty().bind(vestlus.heightProperty());

            // sõnumi saatmise nupp
            Button saada = new Button("Saada");
            saada.setPadding(new Insets(20, 30, 20, 30));

            // saada nupu event
            saada.setOnMouseClicked(event -> {
                try {
                    stub.kirjutaKasutajalt(kasutajanimi, sisend.getText());
                    sisend.clear();
                } catch (RemoteException e) {
                    throw new ÜhenduseEbaõnnestumineErind();
                }
            });

            // sõnumi saatmine ka enteriga
            sisend.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        stub.kirjutaKasutajalt(kasutajanimi, sisend.getText().trim());
                        sisend.clear();
                    } catch (RemoteException e) {
                        throw new ÜhenduseEbaõnnestumineErind();
                    }
                }
            });


            // borderpane nupu paigutamiseks
            BorderPane saadaBorderPane = new BorderPane();
            // saada nupu asukoht sõltub sisendkasti kõrgusest ja kasutajate laiusest
            saadaBorderPane.prefHeightProperty().bind(sisend.heightProperty());
            saadaBorderPane.prefWidthProperty().bind(kasutajad.widthProperty());
            saadaBorderPane.setCenter(saada);


            // parempoolne vertikaalne elementide konteiner
            VBox paremVBOX = new VBox();
            paremVBOX.setSpacing(10);
            paremVBOX.setMaxWidth(200);

            paremVBOX.getChildren().addAll(kasutajad, saadaBorderPane);

            hBox.getChildren().addAll(vasakVBox, paremVBOX);

            borderPane.setCenter(hBox);


            // uuendab automaatselt vestlust ja kasutajate listi
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    try {
                        if (!vestlus.getText().equals(stub.getKasutajaLogi(kasutajanimi))) {
                            vestlus.setText(stub.getKasutajaLogi(kasutajanimi));
                            vestlus.selectPositionCaret(vestlus.getLength());
                            vestlus.deselect();
                            kasutajad.setText(stub.getKasutajateListSõnena());
                        }
                    } catch (RemoteException e) {
                        // kui server läheb offline, siis "jookseb rakendus kokku"
                        // seega EELDAME, et katkestus on lühiajaline
                        // pärast katkestust on rakendus võimeline jätkama
                        while (true) {
                            try {
                                registry = getRegistry(host);
                            } catch (RemoteException e1) {
                                throw new RuntimeException("Serveriga ühenduse loomine ebaõnnestus");
                            }
                            try {
                                StringBuilder vestlusSiiamaani = new StringBuilder();
                                vestlusSiiamaani.append(vestlus.getText());
                                vestlusSiiamaani.append("\n\\*ühenduse katkestus*/\n\n");
                                stub = (ServeriLiides) registry.lookup("ServeriRakendus");
                                stub.lahkuAjutiselt(kasutajanimi);
                                stub.siseneUuesti(kasutajanimi,vestlusSiiamaani);
                                break;
                            } catch (RemoteException e1) {
                                throw new RuntimeException("Serveriga ühenduse loomine ebaõnnestus");
                            } catch (NotBoundException e1) {
                                throw new RuntimeException("Serveriga ühenduse loomine ebaõnnestus");
                            }
                        }
                    }
                }
            }.start();


            // küsime kasutaja kinnitust enne jututoa ristist kinni panemist
            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("JiveHive");
                alert.setHeaderText("Kas soovid tõesti jututoast väljuda?");

                ButtonType välju = new ButtonType("Välju");
                ButtonType loobu = new ButtonType("Loobu");

                alert.getButtonTypes().setAll(välju, loobu);

                // selekteerime automaatselt loobu nupu
                Button loobuSelekteeritud = (Button) alert.getDialogPane().lookupButton(loobu);
                loobuSelekteeritud.setDefaultButton(true);

                Optional<ButtonType> vastus = alert.showAndWait();
                if (vastus.get() == välju) {
                    try {
                        event.consume();
                        stub.lahku(kasutajanimi);
                        System.exit(0);
                    } catch (RemoteException e) {
                        throw new ÜhenduseEbaõnnestumineErind();
                    }
                } else {
                    event.consume();
                }
            });


            Scene stseen = new Scene(borderPane, 600, 500);


            stseen.getStylesheets().add("JiveHiveStylesheet.css");
            stage.getIcons().add(new Image("icon.png"));
            stage.setScene(stseen);
            stage.setTitle("JiveHive");
            stage.show();

            sisselogimine.hide();


        });


        Scene sisselogimiseStseen = new Scene(sisselogimiseBp, 350, 450);
        sisselogimiseStseen.getStylesheets().add("JiveHiveStylesheet.css");
        sisselogimine.getIcons().add(new Image("icon.png"));
        sisselogimine.setScene(sisselogimiseStseen);
        sisselogimine.setTitle("JiveHive");
        sisselogimine.setResizable(false);
        sisselogimine.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
