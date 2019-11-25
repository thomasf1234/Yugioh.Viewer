package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.entities.Artwork;
import sample.entities.CardSpec;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    public Parent root;
    @FXML
    public TextField cardNameTextField;
    @FXML
    public ImageView selectedCardArtworkImageView;

    @FXML
    public TextField searchTextField;

    @FXML
    public ListView cardListView;

    @FXML
    public ComboBox cardAttributesComboBox;

    @FXML
    public TextField cardPasscodeTextField;

    @FXML
    public TextField cardATKTextField;

    @FXML
    public TextField cardDEFTextField;

    @FXML
    public TextArea cardDescriptionTextArea;

    @FXML
    public TilePane cardArtworksTilePane;

    private Logger _logger;

    private Repository _repository;

    private List<CardSpec> _cardSpecs;

    private enum State {INITIALIZED, DATABASE_LOADED}

    protected volatile State state;

    @FXML
    public void initialize() {
        this.state = State.INITIALIZED;
        this._logger = Logger.getLogger(String.valueOf(Controller.class));
        this._logger.info("Initialised logger for Controller");

        this.cardListView.setCellFactory(tree -> {
            ListCell<CardSpec> cell = new ListCell<CardSpec>() {
//                private ImageView imageView = new ImageView("https://vignette.wikia.nocookie.net/yugioh/images/b/b6/DarkMagician-DUPO-EN-UR-LE.png/revision/latest?cb=20190506182525");

                @Override
                public void updateItem(CardSpec item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getName());

//                        imageView.setFitHeight(50);
//                        imageView.setPreserveRatio(true);
//                        setGraphic(imageView);
                    }
                }
            };

//            cell.setOnMouseClicked(event -> {
//                if (!cell.isEmpty()) {
//                    CardSpec cardSpec = cell.getItem();
//                    this.cardNameLabel.setText(cardSpec.getName());
//                }
//            });

            return cell;
        });


        this.cardListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CardSpec>() {

            @Override
            public void changed(ObservableValue<? extends CardSpec> observable, CardSpec oldCardSpec, CardSpec newCardSpec) {
                try {
                    List<Artwork> artworks = _repository.getCardSpecArtworks(newCardSpec.getCardId());

                    List allowedCardAttributes = new ArrayList<String>();
                    allowedCardAttributes.add("FIRE");
                    allowedCardAttributes.add("WATER");
                    allowedCardAttributes.add("SPELL");
                    allowedCardAttributes.add("TRAP");
                    allowedCardAttributes.add("DIVINE");
                    allowedCardAttributes.add("EARTH");
                    allowedCardAttributes.add("LIGHT");
                    allowedCardAttributes.add("DARK");
                    allowedCardAttributes.add("WIND");
                    cardAttributesComboBox.setItems(FXCollections.observableArrayList(allowedCardAttributes));
                    cardAttributesComboBox.getSelectionModel().select(allowedCardAttributes.indexOf(newCardSpec.getCardAttribute()));
                    cardNameTextField.setText(newCardSpec.getName());
                    cardPasscodeTextField.setText(newCardSpec.getPasscode());
                    cardATKTextField.setText(newCardSpec.getAttack());
                    cardDEFTextField.setText(newCardSpec.getDefense());
                    cardDescriptionTextArea.setText(newCardSpec.getDescription());

                    cardArtworksTilePane.getChildren().clear();

                    for (Artwork artwork : artworks) {
                        Image cardArtworkImage = new Image(new ByteArrayInputStream(artwork.getImage()), 100, 100, true, true);
                        ImageView cardArtworkImageView = new ImageView(cardArtworkImage);
                        cardArtworkImageView.setOnMouseClicked(event -> {
                            Image selectedCardArtworkImage = new Image(new ByteArrayInputStream(artwork.getImage()) ,408, 408, true, true);
                            selectedCardArtworkImageView.setImage(selectedCardArtworkImage);
                        });

                        cardArtworkImageView.getStyleClass().add("brightness");
                        cardArtworksTilePane.getChildren().add(cardArtworkImageView);

                        if (!artwork.isAlternate()) {
                            Image artworkImage = new Image(new ByteArrayInputStream(artwork.getImage()), 408, 408, true, true);

                            selectedCardArtworkImageView.setImage(artworkImage);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.searchTextField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldText, String newText) {
                // Your action here
                cardListView.getItems().clear();

                if (hasDatabaseLoaded()) {
                    populateListView(newText);
                }
            }
        });
    }

//    public void updateImage(ActionEvent actionEvent) {
//        String imagePath = searchTextField.getText().trim();
//
//        Image newImage = new Image(imagePath);
//        cardArtworkImageView.setImage(newImage);
//
//        actionEvent.consume();
//    }

    public void onOpenDatabase(ActionEvent actionEvent) {
        File choice = openDatabase(this.root.getScene().getWindow());

        if (choice != null) {
            String sqliteConnectionString = String.format("jdbc:sqlite:%s", choice.getAbsolutePath());

            try {
                // create a connection to the database
                Connection connection = DriverManager.getConnection(sqliteConnectionString);
                this._repository = new Repository(connection);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(String.format("Successfully connected using connection string %s", sqliteConnectionString));
                alert.showAndWait();

                setState(State.DATABASE_LOADED);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(String.format("Failed to connect using connection string %s. %s :: %s", sqliteConnectionString, e.getClass(), e.getMessage()));
                alert.showAndWait();
            }
        }

        actionEvent.consume();
    }

    public boolean hasDatabaseLoaded() {
        return this.state == State.DATABASE_LOADED;
    }

    private void setState(State state) throws SQLException {
        switch (state) {
            case DATABASE_LOADED:
                this.state = State.DATABASE_LOADED;
                onDatabaseLoaded();
                break;
            default:
                String errorMessage = String.format("Unsupported state transition to %s", state);
                throw new UnsupportedOperationException(errorMessage);
        }
    }

    private File openDatabase(Window window) {
        File returnValue = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("SQLite files (*.sqlite)", "*.sqlite");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        File choice = fileChooser.showOpenDialog(window);


        if (choice == null) {
            //User cancelled dialog
        } else {
            if (choice.exists()) {
                if (choice.isDirectory()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Could not open file");
                    alert.setContentText(String.format("%s is a directory.", choice));
                    alert.showAndWait();
                } else {
                    //valid choice
                    returnValue = choice;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Could not open file");
                alert.setContentText(String.format("%s no longer exists", choice));
                alert.showAndWait();
            }
        }
        return returnValue;
    }

    private void onDatabaseLoaded() throws SQLException {
        this._cardSpecs = this._repository.getAllCardSpec();
        populateListView();
    }

    private void populateListView() {
        for (CardSpec cardSpec :
                this._cardSpecs) {
            this.cardListView.getItems().add(cardSpec);
        }
    }

    private void populateListView(String cardNamePartial) {
        String regex = String.format("^.*%s.*$", cardNamePartial.trim());
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        for (CardSpec cardSpec : _cardSpecs) {
            Matcher matcher = pattern.matcher(cardSpec.getName());

            if (matcher.matches()) {
                cardListView.getItems().add(cardSpec);
            }
        }
    }
}

//https://examples.javacodegeeks.com/core-java/javafx-layout-example/
//https://stackoverflow.com/questions/1525444/how-to-connect-sqlite-with-java
//https://stackoverflow.com/questions/24933164/javafx-adding-image-in-tableview
//http://tutorials.jenkov.com/javafx/tableview.html
//https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
//https://gist.github.com/jewelsea/5470095
//SELECT CardId, COUNT(Number) FROM CardPrint GROUP BY CardId HAVING COUNT(Number) > 1