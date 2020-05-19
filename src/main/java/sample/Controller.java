package sample;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sample.dto.OutputValues;
import sample.dto.StartValues;
import sample.evaluator.SoftwareLaboriousnessAndDevelopmentTimeEvaluator;

public class Controller {

    public static HashMap innovationComplexityCoefficients;
    public static HashMap softwareClassification;
    public static HashMap employeeQualificationCoefficients;
    private SoftwareLaboriousnessAndDevelopmentTimeEvaluator softwareLaboriousnessAndDevelopmentTimeEvaluator = new SoftwareLaboriousnessAndDevelopmentTimeEvaluator();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem loadStartValuesOption;

    @FXML
    private TextField codeLines;

    @FXML
    private TextField peopleTextField;

    @FXML
    private TextField producitivityTextField;

    @FXML
    private TextField performersTextField;

    @FXML
    private TextField timeTextField;

    @FXML
    private TextField mainSalaryTextField;

    @FXML
    private TextField additionalSalaryTextField;

    @FXML
    private TextField totalComputerCostsTextField;

    @FXML
    private TextField primeCostTextField;

    @FXML
    private TextField productCostTextField;

    @FXML
    void openInputValuesFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".json", "*.json"));
        File file = fileChooser.showOpenDialog(Main.primaryStage);
        if (file != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                StartValues startValues = objectMapper.readValue(file, StartValues.class);
                evaluateOutputValues(startValues);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void evaluateOutputValues(StartValues startValues) throws Throwable {
        OutputValues outputValues = new OutputValues();
        double codeLinesCount = evaluateCodeLines(startValues);
        outputValues.setCodeLinesCount(codeLinesCount);
        double labourisness = evaluatePeopleNumber(codeLinesCount);
        outputValues.setLaboriousness(labourisness);
        double productivity = evaluateProductivity(labourisness, codeLinesCount);
        outputValues.setProductivity(productivity);
        double developmentTime = softwareLaboriousnessAndDevelopmentTimeEvaluator.evaluateDevelopmentTime(labourisness);
        double performersNumber = softwareLaboriousnessAndDevelopmentTimeEvaluator.evaluateAveragePerformanceNumber(labourisness, developmentTime);
        outputValues.setPerformersCount(performersNumber);

    }

    private double evaluateCodeLines(StartValues startValues) throws Throwable {
        double conventionalCoefficient = Double.parseDouble(String.valueOf(softwareClassification.get(startValues.getTaskType())));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(Paths.get(ClassLoader.getSystemResource("innov_complexity_coef.json").toURI()).toFile());
        double complexityCoefficient = rootNode.get("languageLevel")
                .get(startValues.getLanguageLevel())
                .get("complexityGroup")
                .get(startValues.getComplexityLevel())
                .get("innovationLevel")
                .get(startValues.getInnovationLevel())
                .doubleValue();
        return softwareLaboriousnessAndDevelopmentTimeEvaluator.evaluateSoftwareCodeLines(conventionalCoefficient, complexityCoefficient);
    }

    private double evaluatePeopleNumber(double codeLines) {
        return softwareLaboriousnessAndDevelopmentTimeEvaluator.evaluateLaboriousness(codeLines / 1000);
    }

    private double evaluateProductivity(double laboriousness, double codeLines) {
        return softwareLaboriousnessAndDevelopmentTimeEvaluator.evaluateProductivityOfDevelopersGroup(laboriousness, codeLines / 1000);
    }

    @FXML
    void initialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            innovationComplexityCoefficients = objectMapper.readValue(Paths.get(ClassLoader.getSystemResource("innov_complexity_coef.json").toURI()).toFile(), LinkedHashMap.class);
            softwareClassification = objectMapper.readValue(Paths.get(ClassLoader.getSystemResource("sw_classification.json").toURI()).toFile(), LinkedHashMap.class);
            employeeQualificationCoefficients = objectMapper.readValue(Paths.get(ClassLoader.getSystemResource("emp_qualif_coef.json").toURI()).toFile(), LinkedHashMap.class);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }
}