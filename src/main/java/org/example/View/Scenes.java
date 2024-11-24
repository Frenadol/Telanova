package org.example.View;

public enum Scenes {
    ROOT("/org/example/View/Layout.fxml"),
    PRIMARY("/org/example/View/primary.fxml"),
    SECONDARY("/org/example/View/secondary.fxml");



    private String url;
    Scenes(String url){
        this.url=url;
    }
    public String getURL(){
        return url;
    }
}
