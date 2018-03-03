/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasaxparser;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

/**
 *
 * @author gambi
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextArea textArea;
    
    private XMLNode root;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void handleOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(textArea.getScene().getWindow());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files (*.xml", "*.xml"));
        if (file != null) {
            try{
                root = XMLStackParser.parse(file);
                
                textArea.setText(XMLStackParser.createText());
            } catch (Exception ex) {
                displayExceptionAlert("Exception parsing XML file.", ex);
            }
        }
    }
    
    private void displayExceptionAlert(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
}
