/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstcompilor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lexical.LexicalAnalyzer;
import token.token;

/**
 *
 * @author FORSAKEN MYSTERY
 */
public class FXMLDocumentController implements Initializable {

    String txt = "";
    String text = "";
    @FXML
    private Button saving;
    @FXML
    private Button refreshing;
    @FXML
    private Button selectFile;
    @FXML
    private Button compiling;
    @FXML
    private Button exit;
    @FXML
    private TextArea textArea;
    @FXML
    private BorderPane panel;
    List<File> files;
    @FXML
    private TextArea area;

    private List<File> returnMultipleFileChooser() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select C files");
        File f[] = File.listRoots();
        fileChooser.setInitialDirectory(new File(f[0].getPath()));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("C Files", "*.C", "*.c"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                LexicalAnalyzer lexim = new LexicalAnalyzer(file.getPath());
                txt += lexim.getContent();
                txt += "\n================================\n\n";
                area.setText(txt);
            }
            return selectedFiles;
        }
        return null;
    }

    @FXML
    private void select(MouseEvent event) {
        files = returnMultipleFileChooser();
    }

    @FXML
    private void save(MouseEvent event) throws FileNotFoundException, UnsupportedEncodingException {
        if (!text.equals("")) {
            try (PrintWriter writer = new PrintWriter("Tokens.txt", "UTF-8")) {
                if (files != null) {
                    files.stream().map((file) -> new LexicalAnalyzer(file.getPath())).map((lexim) -> {
                        lexim.tokenize();
                        return lexim;
                    }).map((lexim) -> {
                        lexim.getToken().stream().forEach((token) -> {
                            writer.println(token.toString());
                        });
                        return lexim;
                    }).map((_item) -> {
                        writer.println();
                        return _item;
                    }).map((_item) -> {
                        writer.println("===================================");
                        return _item;
                    }).map((_item) -> {
                        writer.println();
                        return _item;
                    }).forEach((_item) -> {
                        writer.println();
                    });
                }
            }
            new Alert(Alert.AlertType.INFORMATION, "save done !", ButtonType.FINISH).showAndWait();
        } else {
            new Alert(Alert.AlertType.WARNING, "there is no data !", ButtonType.FINISH).showAndWait();
        }
    }

    @FXML
    private void compile(MouseEvent event) {
        if (files != null) {
            for (File file : files) {
                LexicalAnalyzer lexim = new LexicalAnalyzer(file.getPath());
                lexim.tokenize();
                for (token token : lexim.getToken()) {
                    text += token.toString();
                }
                text += "\n=================================\n\n";
                textArea.setText(text);
            }
        }
    }

    @FXML
    private void exit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void refresh(MouseEvent event) {
        text = "";
        textArea.setText(text);
        txt = "";
        area.setText(txt);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setEditable(false);
        area.setEditable(false);
        files = null;
    }

}
