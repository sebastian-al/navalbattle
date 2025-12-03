module com.example.navalbattle1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.navalbattle1.Controller to javafx.fxml;
    exports com.example.navalbattle1;
    exports com.example.navalbattle1.Controller;
}