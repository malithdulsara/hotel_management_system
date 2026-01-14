package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dto.RoomInfoDto;


import java.net.URL;
import java.util.ResourceBundle;


public class RoomInfoFormController implements Initializable{
    RoomInfoController roomInfoController = new RoomInfoController();
    ObservableList<RoomInfoDto> roomInfoDtos = FXCollections.observableArrayList();

    @FXML
    private TableColumn<?, ?> colAvailability;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colFloor;

    @FXML
    private TableColumn<?, ?> colGuest;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colRoomId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<RoomInfoDto> tblRoomInfo;

    @FXML
    private ComboBox<Integer> cmbMaxGuest;

    @FXML
    private ComboBox<Integer> cmbFloor;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private RadioButton radioAvailable;

    @FXML
    private RadioButton radioUnavailble;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtRoomId;

    private void loadTable(){

        tblRoomInfo.setItems(roomInfoController.getAllRooms());

        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colGuest.setCellValueFactory(new PropertyValueFactory<>("maxGuest"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("floor"));

    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();

        ObservableList<String >types = FXCollections.observableArrayList("Single","Double","Suite","Delux");
        cmbType.setItems(types);

        ObservableList<Integer> floors = FXCollections.observableArrayList(1,2,3,4,5,6);
        cmbFloor.setItems(floors);

        ObservableList<Integer> maxGuests = FXCollections.observableArrayList(1,2,3,4,5);
        cmbMaxGuest.setItems(maxGuests);

        ToggleGroup toggleGroup = new ToggleGroup();
        radioAvailable.setToggleGroup(toggleGroup);
        radioUnavailble.setToggleGroup(toggleGroup);

        tblRoomInfo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    private void fillFields(RoomInfoDto room) {
        txtRoomId.setText(room.getRoomId());
        txtPrice.setText(String.valueOf(room.getPrice()));
        txtDescription.setText(room.getDescription());

        cmbType.setValue(room.getType());
        cmbMaxGuest.setValue(room.getMaxGuest());

        try {
            cmbFloor.setValue(Integer.parseInt(room.getFloor()));
        } catch (NumberFormatException e) {
            System.out.println("Could not parse floor: " + room.getFloor());
        }

        if (room.isAvailability()) {
            radioAvailable.setSelected(true);
        } else {
            radioUnavailble.setSelected(true);
        }
    }
    @FXML
    void btnAddOnAction(ActionEvent event) {
        String roomId = txtRoomId.getText();
        String type = cmbType.getValue();
        double pricePerNight = Double.parseDouble(txtPrice.getText());
        int maxGuest = cmbMaxGuest.getValue();
        boolean availability = checkAvailability();
        String description = txtDescription.getText();
        int floor = cmbFloor.getValue();

        roomInfoController.addRoomDetails(roomId,type,pricePerNight,maxGuest,availability,description,floor);
        loadTable();
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String roomId = txtRoomId.getText();
        if (roomId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID to delete!").show();
        }

        int rowsAffected = roomInfoController.deleteRoom(roomId);

        if (rowsAffected > 0) {
            new Alert(Alert.AlertType.INFORMATION, "room Deleted Successfully!").show();

            loadTable();
            clearFields();
        } else {
            new Alert(Alert.AlertType.ERROR, "Delete Failed. ID not found!").show();
        }


    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String roomId = txtRoomId.getText();
        String type = cmbType.getValue();
        double pricePerNight = Double.parseDouble(txtPrice.getText());
        int maxGuest = cmbMaxGuest.getValue();
        boolean availability = checkAvailability();
        String description = txtDescription.getText();
        int floor = cmbFloor.getValue();

        int rows = roomInfoController.updateRoomDetails(roomId,type,pricePerNight,maxGuest,availability,description,floor);

        if (rows > 0) {
            new Alert(Alert.AlertType.INFORMATION, "Room Updated!").show();
            loadTable();
            clearFields();
        } else {
            new Alert(Alert.AlertType.ERROR, "Update Failed").show();
        }
    }
    private boolean checkAvailability() {
        if (radioAvailable.isSelected()) {
            return true;
        } else {
            return false;
        }
    }
    private void clearFields() {
        txtRoomId.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        cmbType.setValue(null);
        cmbMaxGuest.setValue(null);
        cmbFloor.setValue(null);
        radioAvailable.setSelected(false);
        radioUnavailble.setSelected(false);
    }

}
