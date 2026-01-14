package controller;

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

public class CustomerInfoController implements Initializable {
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");

            String sql = "INSERT INTO customers (customer_id,first_name,last_name,email,phone,address,city,registered_date)" + "VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, txtCustomerId.getText());
            statement.setString(2, txtFirstName.getText());
            statement.setString(3, txtLastName.getText());
            statement.setString(4, txtEmail.getText());
            statement.setString(5, txtPhone.getText());
            statement.setString(6, txtAddress.getText());
            statement.setString(7, txtCity.getText());
            statement.setString(8, java.time.LocalDate.now().toString());

            int rowAffected = statement.executeUpdate();

            if (rowAffected > 0) {
                System.out.println("Successfully Added !");
                customerInfoDtos.clear();
                loadCustomerData();
                clearFields();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void loadCustomerData() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");
            String sql = "SELECT * FROM customers";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CustomerInfoDto customerInfoDto = new CustomerInfoDto(
                        resultSet.getString("customer_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("registered_date")
                );
                customerInfoDtos.add(customerInfoDto);
            }


            tblCustomerInfo.setItems(customerInfoDtos);


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
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (customer.getCustomerId().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches Customer ID
                    } else if (customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches First Name
                    } else if (customer.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches Last Name
                    } else if (customer.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches Phone
                    } else if (customer.getCity().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches City
                    }

                    return false; // Does not match.
                });
            });


            SortedList<CustomerInfoDto> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(tblCustomerInfo.comparatorProperty());

            tblCustomerInfo.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CustomerInfoDto selectCustomer = tblCustomerInfo.getSelectionModel().getSelectedItem();

        if(selectCustomer == null){
            System.out.println("please select a customer first !");
            return;
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");
            String sql = "DELETE FROM customers WHERE customer_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);


            statement.setString(1, selectCustomer.getCustomerId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully !");


                customerInfoDtos.remove(selectCustomer);
            } else {
                System.out.println("Delete failed. Customer not found in DB.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");

            String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone=?, address=?, city=? WHERE customer_id=?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, txtFirstName.getText());
            statement.setString(2, txtLastName.getText());
            statement.setString(3, txtEmail.getText());
            statement.setInt(4, Integer.parseInt(txtPhone.getText()));
            statement.setString(5, txtAddress.getText());
            statement.setString(6, txtCity.getText());

            statement.setString(7, txtCustomerId.getText());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer Updated Successfully!");

                customerInfoDtos.clear();
                loadCustomerData();
                clearFields();
            } else {
                System.out.println("Update Failed. Customer ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error: Phone number is invalid.");
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
