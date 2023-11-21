package com.drawingtool;

import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;

public class MainController extends App implements Initializable {
    @FXML
    private ColorPicker colorpickerPrimary;
    @FXML
    private ColorPicker colorpickerSecondary;
    @FXML
    private Slider bsizeSlider;
    @FXML
    private TextField bsize;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Rectangle canvasBG;
    
    // New canvas
    @FXML
    private TextField canvasWidth;
    @FXML
    private TextField canvasHeight;
    @FXML
    private ColorPicker canvasColorpicker;
    @FXML
    private Rectangle canvasPreview;
    @FXML
    private AnchorPane canvasPreviewPane;
    @FXML 
    private Text canvasErrorMsg;
    @FXML
    private AnchorPane newCanvasPane;
    
    GraphicsContext brushTool;

    private String selectedTool = "";
    private int selectedBrush = 1;
    private double mouseAnchorX;
    private double mouseAnchorY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();

        //  Brush tool
        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(bsize.getText());
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;

            if (selectedTool == "brush" && !bsize.getText().isEmpty()) {
                brushTool.setFill(colorpickerPrimary.getValue());
                //if (selectedBrush == 1) {
                brushTool.fillRoundRect(x, y, size, size, size, size);
                //}
            }
        });

        //  Change brush size
        bsizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            bsize.setText(Integer.toString(newValue.intValue()));
        });
        bsize.textProperty().addListener((observable) -> {
            if (isValid(bsize.getText())) {
                if (Integer.parseInt(bsize.getText()) > 100)
                    bsize.setText("100");
                else if (Integer.parseInt(bsize.getText()) < 1)
                    bsize.setText("1");
                bsizeSlider.setValue((double)Double.parseDouble(bsize.getText()));
            }
            else {
                bsize.setText("1");
                bsizeSlider.setValue(1.0);
            }
        });

        //  Hand tool
        canvasPane.setOnMousePressed(mouseEvent -> {
            if (selectedTool == "hand") {
                mouseAnchorX = mouseEvent.getX();
                mouseAnchorY = mouseEvent.getY();
                scene.setCursor(Cursor.CLOSED_HAND);
            }
        });
        canvasPane.setOnMouseDragged(mouseEvent -> {
            if (selectedTool == "hand") {
                canvasPane.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
                canvasPane.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
                scene.setCursor(Cursor.CLOSED_HAND);
            }
        });
        canvasPane.setOnMouseReleased(mouseEvent -> {
            if (selectedTool == "hand") {
                scene.setCursor(Cursor.OPEN_HAND);
            }
        });

        //  Create new canvas
        canvasWidth.textProperty().addListener((observable) -> {
            changeDim();
        });
        canvasHeight.textProperty().addListener((observable) -> {
            changeDim();
        });
        canvasColorpicker.valueProperty().addListener((observable) -> {
            changeDim();
        });
        canvasWidth.textProperty().addListener((observable) -> {
            if (isValid(canvasWidth.getText())) {
                if (Integer.parseInt(canvasWidth.getText()) > 1440)
                    canvasWidth.setText("1440");
                else if (Integer.parseInt(canvasWidth.getText()) < 1)
                    canvasWidth.setText("1");
            }
            else
                canvasWidth.setText("1");
        });
        canvasHeight.textProperty().addListener((observable) -> {
            if (isValid(canvasHeight.getText())) {
                if (Integer.parseInt(canvasHeight.getText()) > 1440)
                    canvasHeight.setText("1440");
                else if (Integer.parseInt(canvasHeight.getText()) < 1)
                    canvasHeight.setText("1");
            }
            else
                canvasHeight.setText("1");
        });
    }

    @FXML
    public void newCanvas(ActionEvent e) throws IOException {
        canvasPane.setVisible(false);
        canvasPane.setDisable(true);
        newCanvasPane.setVisible(true);
        newCanvasPane.setDisable(false);
        scene.setCursor(Cursor.DEFAULT);
        selectedTool = "";
    }

    private void changeDim() {
        if (isValid(canvasWidth.getText()) && isValid(canvasHeight.getText())) {
            int width = Integer.parseInt(canvasWidth.getText());
            int height = Integer.parseInt(canvasHeight.getText());
            int maxDim = 200;

            double ratio = (double)width/(double)height;
            if (ratio > 1) {
                width = maxDim;
                height = (int) (width * 1/ratio);
            }
            else {
                height = maxDim;
                width = (int) (height * ratio);
            }

            double paneMidpointX = canvasPreviewPane.getWidth()/2;
            double paneMidpointY = canvasPreviewPane.getHeight()/2;
            canvasPreview.setX(paneMidpointX - width/2);
            canvasPreview.setY(paneMidpointY - height/2);

            canvasPreview.setWidth(width);
            canvasPreview.setHeight(height);
            canvasPreview.setFill(canvasColorpicker.getValue());
        }
    }

    @FXML
    private void createNewCanvas(ActionEvent e) throws IOException {
        if (isValid(canvasWidth.getText()) && isValid(canvasHeight.getText())) {
            canvasErrorMsg.setVisible(false);

            canvasPane.setVisible(true);
            canvasPane.setDisable(false);
            newCanvasPane.setVisible(false);
            newCanvasPane.setDisable(true);

            int width = Integer.parseInt(canvasWidth.getText());
            int height = Integer.parseInt(canvasHeight.getText());

            //  Change dimensions
            canvas.setWidth(width);
            canvas.setHeight(height);
            canvasBG.setWidth(width);
            canvasBG.setHeight(height);
            canvasBG.setVisible(false);

            //  Change canvas location
            double paneMidpointX = canvasPane.getWidth()/2;
            double paneMidpointY = canvasPane.getHeight()/2;
            canvas.setLayoutX(paneMidpointX - width/2);
            canvas.setLayoutY(paneMidpointY - height/2);
            canvasBG.setX(paneMidpointX - width/2);
            canvasBG.setY(paneMidpointY - height/2);
            
            //  Clear canvas
            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
            brushTool.setFill(canvasColorpicker.getValue());
            brushTool.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), 1, 1);

            System.out.println("Canvas size: " + canvas.getWidth() + " x " + canvas.getHeight());
        }
        else
            canvasErrorMsg.setVisible(true);
    }

    @FXML
    private void cancelNewCanvas(ActionEvent e) {
        canvasPane.setVisible(true);
        canvasPane.setDisable(false);
        newCanvasPane.setVisible(false);
        newCanvasPane.setDisable(true);
    }

    private boolean isValid(String strNum) {
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if (Integer.parseInt(strNum) <= 0)
            return false;
        return true;
    }

    //  TOOLBAR METHODS
    @FXML
    private void brushSelected(ActionEvent e) {
        selectedTool = "brush";
        scene.setCursor(Cursor.CROSSHAIR);
    }

    @FXML
    private void handSelected(ActionEvent e) {
        selectedTool = "hand";
        scene.setCursor(Cursor.OPEN_HAND);
    }

    @FXML
    private void switchColor(ActionEvent e) {
        Color temp = colorpickerPrimary.getValue();
        colorpickerPrimary.setValue(colorpickerSecondary.getValue());
        colorpickerSecondary.setValue(temp);
    }


    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
