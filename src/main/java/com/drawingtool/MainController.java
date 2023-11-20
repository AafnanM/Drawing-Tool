package com.drawingtool;

import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;

public class MainController implements Initializable {
    @FXML
    private ColorPicker colorpicker;
    @FXML
    private TextField bsize;
    @FXML
    private Canvas canvas;
    
    GraphicsContext brushTool;

    private String selectedTool = "";
    private int selectedBrush = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();


        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(bsize.getText());
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;

            if (selectedBrush != 0 && !bsize.getText().isEmpty()) {
                brushTool.setFill(colorpicker.getValue());
                brushTool.fillRoundRect(x, y, size, size, size, size);
            }
        });
    }

    @FXML
    public void newCanvas(ActionEvent e) throws IOException {
        App.startSeparateWindow("NewCanvas", "Create New Canvas");
    }

    @FXML
    private void brushSelected(ActionEvent e) {
        selectedBrush = 1;
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
