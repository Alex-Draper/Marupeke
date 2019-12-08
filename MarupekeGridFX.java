import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Class to handle the GUI of the game, uses JavaFx
 */
public class MarupekeGridFX extends Application {

    //window elements
    private BorderPane root = new BorderPane();
    private MenuBar menuBar = new MenuBar();
    private Menu fileMenu = new Menu("File");
    private ArrayList<MenuItem> fileMenuItems = new ArrayList<>();
    private GridPane playArea;


    private Boolean gameInProgress = true;

    //gameInstance
    private MarupekeGrid game;

    /**
     * Load a new game, setup the scene and then display the scene
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {


        game = MarupekeGrid.randomPuzzle(3, 2, 1, 1);

        UISetUp();

        playArea = getPlayArea();
        root.setCenter(playArea);
        Scene scene = new Scene(root, 550, 550);

        primaryStage.setTitle("Marupeke");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    /**
     * Construct the elements of the display, menu items, etc
     */
    private void UISetUp() {

        //set up the main GUI and buttons
        MenuItem newGameItem = new MenuItem("New Game");
        MenuItem completeItem = new MenuItem("Complete?");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem quitItem = new MenuItem("Quit");

        newGameItem.setOnAction(e -> newGame());
        completeItem.setOnAction(e -> checkCompletion());

        helpItem.setOnAction(e -> {
            try {
                openHelpWebpage();
            } catch (IOException ex) {
                createAlert("Error", AlertType.ERROR,
                        "Unable to open webpage, please try again later!",
                        ButtonType.OK);
            }
        });



        fileMenu.getItems().addAll(newGameItem, completeItem, helpItem, quitItem);


        menuBar.getMenus().add(fileMenu);


        root.setTop(menuBar);


    }

    /**
     * Construct the visual grid of the puzzle and link click events
     * @return the constructed play area
     */
    private GridPane getPlayArea() {
        GridPane playArea = new GridPane();

        for(int row=0; row<game.getSize(); row++) {
            for(int column=0; column<game.getSize(); column++) {
                MPTile tile = game.getTile(row, column);
                Button b = new Button(tile.toString());
                b.setMinSize(50, 50);
                b.setOnAction(e->cycleSymbol(b));


                if(!tile.isEditable()) {
                    b.setDisable(true);
                }

                //b.setOnAction(value);

                playArea.setConstraints(b, row, column);
                playArea.getChildren().add(b);
            }
        }
        return playArea;

    }

    /**
     * Cycle the mark on the button that was clicked
     * @param buttonClicked the button passed through from the click event
     */
    private void cycleSymbol(Button buttonClicked) {

        if(buttonClicked.getText().equals("_")) {
            buttonClicked.setText("x");
            game.userMarkRequest(GridPane.getColumnIndex(buttonClicked), GridPane.getRowIndex(buttonClicked), Mark.CROSS);
        }
        else if(buttonClicked.getText().equals("x")) {
            buttonClicked.setText("o");
            game.userMarkRequest(GridPane.getColumnIndex(buttonClicked), GridPane.getRowIndex(buttonClicked), Mark.NOUGHT);
        }
        else if(buttonClicked.getText().equals("o")) {
            buttonClicked.setText("_");
            game.userMarkRequest(GridPane.getColumnIndex(buttonClicked), GridPane.getRowIndex(buttonClicked), Mark.BLANK);
        }

    }


    /**
     * Construct a new puzzle and display it on screen
     */
    public void newGame() {

        String difficulty;

        //check if game already in progress
        if (gameInProgress) {
            Optional<ButtonType> result = createAlert("New Game", AlertType.CONFIRMATION, "Are you sure you wish to end this game?",
                    ButtonType.YES, ButtonType.NO);

            if (result.get() != ButtonType.YES) {
                return;
            }

            createAlert("Oh No!", AlertType.INFORMATION, "Better Luck Next Time", ButtonType.OK);
        }
        //get rid of current game
        root.setCenter(null);

        //get user to choose difficulty
        Optional<ButtonType> result = chooseDifficultyAlert();

        switch (result.get().getText()) {
            case "Hard":
                difficulty = "h";
                break;
            case "Medium":
                difficulty = "m";
                break;
            default:
                difficulty = "e";
                break;
        }

        //create new game depending on difficulty selected
        Random r1 = new Random();

        //generate some random values for the number of filled tiles depending on difficulty
        if(difficulty.equals("e")) {
            createGame(r1.nextInt((4 - 3) + 1) + 3);
        }
        else if(difficulty.equals("m")) {
            createGame(r1.nextInt((7 - 5) + 1) + 5);
        }
        else if(difficulty.equals("h")) {
            createGame(r1.nextInt((10 - 8) + 1) + 8);
        }


        playArea = getPlayArea();
        root.setCenter(playArea);
    }

    /**
     * Generate the new puzzle
     * @param size
     */
    public void createGame(int size) {
        int spread = (int) Math.floor((((size*size)/2)-1)/3);

        game = MarupekeGrid.randomPuzzle(size, spread, spread, spread);
    }


    //methods to create generic alerts with different amounts of buttons

    /**
     * Create an alert to display to the user
     * @param title The title of the alert
     * @param type The type of alert
     * @param alertText The text the alert should contain
     * @param button The button to display to the user
     * @return The constructed alert object
     */
    public Optional<ButtonType> createAlert(String title, AlertType type, String alertText,
                                            ButtonType button) {

        Alert alert = new Alert(type, alertText, button);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * Create an alert to display to the user
     * @param title The title of the alert
     * @param type The type of alert
     * @param alertText The text the alert should contain
     * @param button The first button to display to the user
     * @param button2 The second button to display to the user
     * @return The constructed alert object
     */
    public Optional<ButtonType> createAlert(String title, AlertType type, String alertText,
                                            ButtonType button, ButtonType button2) {

        Alert alert = new Alert(type, alertText, button, button2);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * Create the difficulty alert to display to the user
     * @return The constructed alert object
     */
    public Optional<ButtonType> chooseDifficultyAlert() {

        ButtonType easy = new ButtonType("Easy");
        ButtonType medium = new ButtonType("Medium");
        ButtonType hard = new ButtonType("Hard");


        Alert alert = new Alert(AlertType.CONFIRMATION, "Please select a difficulty",
                easy, medium, hard);
        alert.setTitle("Difficulty");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * Check if the puzzle is complete, if so then display well done alert, if not highlight all problem tiles
     */
    private void checkCompletion() {

        clearGridStyling();

        if(game.isPuzzleComplete()) {
            //disable everything since the puzzle is complete
            for(Node n : playArea.getChildren()) {
                n.setDisable(true);
            }
            gameInProgress = false;
            createAlert("Finshed!", AlertType.INFORMATION, "Well Done, you finished the puzzle! Press New in the File menu to try another one.", new ButtonType(":)"));
        }
        else if(!game.isLegal()) {
            //create an 2d array of all the buttons because javafx doesn't seem to allow direct indexing...
            Node[][] nodeGrid = new Node[game.getSize()][game.getSize()];
            for(Node n : playArea.getChildren()) {
                Integer column = GridPane.getColumnIndex(n);
                Integer row = GridPane.getRowIndex(n);
                nodeGrid[column][row] = n;
            }

            //colour all illegal tiles
            ArrayList<Tuple> illegalTiles = game.illegalities();

            for(Tuple problemTileTuple : illegalTiles) {

                int rowIndex = (int)problemTileTuple.first;
                int columnIndex = (int)problemTileTuple.second;

                Node n = nodeGrid[rowIndex][columnIndex];
                n.setStyle("-fx-background-image: url('illegal.png')");

            }
        }
        showUnmarked();


    }

    /**
     * remove all colours from the grid indicating problem tiles
     */
    public void clearGridStyling() {
        for(Node n : playArea.getChildren()) {
            n.setStyle(null);
        }
    }

    /**
     * Colour all unmarked tiles to indicate to the user that they must be filled
     */
    public void showUnmarked() {
        for(Node n : playArea.getChildren()) {
            Button b = (Button)n;

            if(b.getText().equals("_")) {
                b.setStyle("-fx-background-image: url('unmarked.png')");
            }
        }
    }

    /**
     * Open webpage to help on how to play
     * @throws IOException exception if for what ever reason the weboage cannot be opened, not much to handle
     * let the game keep running
     */
    private void openHelpWebpage() throws IOException {
        try {
            Desktop desktop = Desktop.getDesktop();
            URI oURL = new URI("https://www.theguardian.com/technology/2017/oct/10/puzzle-masters-japan-sudoku-tokyo");
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
            createAlert("Error", AlertType.ERROR,
                    "Unable to open webpage, please try again later!",
                    ButtonType.OK);
        }
    }













}


