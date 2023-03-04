module toysocialnetwork.toysocialnetworkfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens toysocialnetwork.toysocialnetworkfx to javafx.fxml;
    exports toysocialnetwork.toysocialnetworkfx;
    exports toysocialnetwork.toysocialnetworkfx.utils;
    exports toysocialnetwork.toysocialnetworkfx.utils.observer;
    exports toysocialnetwork.toysocialnetworkfx.domain;
    exports toysocialnetwork.toysocialnetworkfx.domain.dto;
    exports toysocialnetwork.toysocialnetworkfx.domain.validators;
    exports toysocialnetwork.toysocialnetworkfx.repository.database;
    exports toysocialnetwork.toysocialnetworkfx.controller;
    exports toysocialnetwork.toysocialnetworkfx.service;
}