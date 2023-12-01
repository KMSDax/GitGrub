module com.example.gitgrub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.json;
    requires java.desktop;
    requires jsoup;
    requires javafx.web;


    opens com.example.gitgrub to javafx.fxml;
    exports com.example.gitgrub;
}