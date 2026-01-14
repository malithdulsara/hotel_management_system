package controller.customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.dto.CustomerInfoDto;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CustomerInfoFormController implements Initializable {
    CustomerInfoService customerInfoService = new CustomerInfoController();
    ObservableList<CustomerInfoDto> customerInfoDtos = FXCollections.observableArrayList();

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCity;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private TableColumn<?, ?> colRegDate;

    @FXML
    private TableView<CustomerInfoDto> tblCustomerInfo;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String customerId = txtCustomerId.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String city = txtCity.getText();
        String date = java.time.LocalDate.now().toString();

        int rowAffected = customerInfoService.addCustomerDetails(customerId,firstName,lastName,email,phone,address,city,date);
        if (rowAffected > 0) {
            System.out.println("Successfully Added !");
            customerInfoDtos.clear();
            loadCustomerData();
            clearFields();

        }


    }

    private void loadCustomerData() {

        customerInfoDtos = customerInfoService.getAllCustomers();

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("registeredDate"));

        FilteredList<CustomerInfoDto> filteredData = new FilteredList<>(customerInfoDtos, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomerId() != null && customer.getCustomerId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getLastName() != null && customer.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getPhone() != null && customer.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getCity() != null && customer.getCity().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<CustomerInfoDto> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblCustomerInfo.comparatorProperty());

        tblCustomerInfo.setItems(sortedData);
    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CustomerInfoDto selectCustomer = tblCustomerInfo.getSelectionModel().getSelectedItem();

        if(selectCustomer == null){
            System.out.println("please select a customer first !");
            return;
        }
int rowsAffected = customerInfoService.deleteCustomer(selectCustomer);
        if (rowsAffected > 0) {
            System.out.println("Customer deleted successfully !");

            customerInfoDtos.remove(selectCustomer);
        } else {
            System.out.println("Delete failed. Customer not found in DB.");
        }

    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String customerId = txtCustomerId.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        String city = txtCity.getText();
        String date = java.time.LocalDate.now().toString();

         int rowsAffected = customerInfoService.updateCustomerDetails(customerId,firstName,lastName,email,phone,address,city,date);

        if (rowsAffected > 0) {
            System.out.println("Customer Updated Successfully!");

            customerInfoDtos.clear();
            loadCustomerData();
            clearFields();
        } else {
            System.out.println("Update Failed. Customer ID not found.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCustomerData();

        tblCustomerInfo.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if(newValue != null){
                addValueToTextField(newValue);
            }
        } );

    }

    private void addValueToTextField(CustomerInfoDto customer) {
        txtCustomerId.setText(customer.getCustomerId());
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());
        txtCity.setText(customer.getCity());
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtCity.setText("");
    }
}
