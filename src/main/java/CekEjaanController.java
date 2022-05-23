import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import spellcheck.Node;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CekEjaanController implements Initializable {

    @FXML private GridPane root;
    @FXML private TextArea masukanTextArea;
    @FXML private Button periksaButton;
    @FXML private TextFlow hasilTextFlow;
    @FXML private Label waktuEksekusiLabel;
    private ListProperty<String> daftarKataProperty;
    Node dictionary;

    public CekEjaanController() {
        ObservableList<String> observableList = FXCollections.observableArrayList(new ArrayList<>());
        this.daftarKataProperty = new SimpleListProperty<String>(observableList);
        this.dictionary = new Node(null);

        InputStream is = CekEjaan.class.getResourceAsStream("kata_dasar_kbbi_shuffled.txt");
        Scanner scanner = new Scanner(is);
        while(scanner.hasNext()){
            dictionary.addWord(scanner.nextLine().trim());
        }

        scanner.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        daftarKataProperty.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                hasilTextFlow.getChildren().clear();

                long startTime = System.nanoTime();
                Boolean[] isValid = new Boolean[daftarKataProperty.size()];
                for(int i = 0; i < isValid.length; i++){
                    isValid[i] = dictionary.checkWord(daftarKataProperty.get(i));
                }
                long duration = System.nanoTime() - startTime;
                waktuEksekusiLabel.setText("Waktu Eksekusi: " + duration/1000 + " mikrosekon");

                for(int i = 0; i < daftarKataProperty.size(); i++){
                    Label l = new Label(daftarKataProperty.get(i) + " ");
                    l.setFont(Font.font(18));
                    if(!isValid[i]){
                        l.setTextFill(Color.RED);
                    }
                    hasilTextFlow.getChildren().add(l);
                }
            }
        });

    }

    public void periksaTeks(ActionEvent event){
        this.daftarKataProperty.clear();
        this.daftarKataProperty.addAll(masukanTextArea.getText().trim().split("\\s+"));
    }


}
