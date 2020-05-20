package sample;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
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
import sample.evaluator.ComputerUsageCostEvaluator;
import sample.evaluator.ProductCostEvaluator;
import sample.evaluator.SalaryEvaluator;
import sample.evaluator.SoftwareLaboriousnessAndDevelopmentTimeEvaluator;

public class Controller {

    public static HashMap innovationComplexityCoefficients;
    public static HashMap softwareClassification;
    public static HashMap employeeQualificationCoefficients;
    private SoftwareLaboriousnessAndDevelopmentTimeEvaluator softwareLaboriousnessAndDevelopmentTimeEvaluator = new SoftwareLaboriousnessAndDevelopmentTimeEvaluator();
    private SalaryEvaluator salaryEvaluator = new SalaryEvaluator();
    private ComputerUsageCostEvaluator computerUsageCostEvaluator = new ComputerUsageCostEvaluator();
    private ProductCostEvaluator productCostEvaluator = new ProductCostEvaluator();

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
        double qualificationCoefficient = evaluateQualificationCoefficient(startValues.getEmployeeExperience());
        double productCreationTime = softwareLaboriousnessAndDevelopmentTimeEvaluator.evaluateEveryDevelopmentStageTime(startValues, codeLinesCount, qualificationCoefficient);
        outputValues.setProductCreationNeededTime(productCreationTime);

        int workingDays = 251;
        double workingHoursPerDay = startValues.getAvgWorkingDayLength();
        double mainSalarySum = salaryEvaluator.evaluateMainSalary(startValues.getFirstClassEngineerSalary(), startValues.getEngineerFareCoefficient(), productCreationTime, workingDays, workingHoursPerDay, startValues.getAvgBonusPercent()) +
                salaryEvaluator.evaluateMainSalary(startValues.getOperatorSalary(), startValues.getOperatorFareCoefficient(), productCreationTime, workingDays, workingHoursPerDay, startValues.getAvgBonusPercent())
                + salaryEvaluator.evaluateMainSalary(startValues.getSystemAdminSalary(), startValues.getSystemAdminFareCoefficient(), productCreationTime, workingDays, workingHoursPerDay, startValues.getAvgBonusPercent());

        double additionalSalarySum = salaryEvaluator.evaluateAdditionalSalary(mainSalarySum, startValues.getAvgAdditionalSalaryPercent());

        double performerESV = salaryEvaluator.evaluateESV(mainSalarySum);
        outputValues.setMainPerformerSalary(mainSalarySum);
        outputValues.setAdditionalPerformerSalary(additionalSalarySum);


        double computerUptime = computerUsageCostEvaluator.evaluateComputerUptime(workingDays, workingHoursPerDay);
        double electricityCosts = computerUsageCostEvaluator.evaluateElectricityCosts(startValues.getAvgWorkingDayLength() * workingDays, startValues.getOneKvtCost(), startValues.getSumComputerPower());
        double mainComputerUsersSalary = computerUsageCostEvaluator.evaluateMainComputerUsersSalary(startValues.getFirstClassEngineerSalary(), startValues.getEngineerFareCoefficient(), 6, startValues.getAvgBonusPercent())
                + computerUsageCostEvaluator.evaluateMainComputerUsersSalary(startValues.getOperatorSalary(), startValues.getOperatorFareCoefficient(), 6, startValues.getAvgBonusPercent())
                + computerUsageCostEvaluator.evaluateMainComputerUsersSalary(startValues.getSystemAdminSalary(), startValues.getSystemAdminFareCoefficient(), 6, startValues.getAvgBonusPercent());

        double additionalComputerUsersSalary = computerUsageCostEvaluator.evaluateAdditionalComputerUsersSalary(mainComputerUsersSalary, startValues.getAvgAdditionalSalaryPercent());
        double compUsersESV = computerUsageCostEvaluator.evaluateComputerUserESV(mainComputerUsersSalary, additionalComputerUsersSalary);
        double materialsCosts = startValues.getBalanceCost() * 0.02;
        double profCosts = startValues.getBalanceCost() * 0.03;
        double amorCosts = startValues.getBalanceCost() / startValues.getComputerUsageYears();

        double sumOfYearlyCosts = electricityCosts + materialsCosts + profCosts + amorCosts + mainComputerUsersSalary + additionalComputerUsersSalary + compUsersESV;
        double computerUsageCosts = sumOfYearlyCosts / computerUptime;
        outputValues.setTotalComputerUsageCosts(computerUsageCosts);

        double primeProductCost = productCostEvaluator.evaluatePrimeCost(mainSalarySum, additionalSalarySum, performerESV, computerUsageCosts);
        double productCost = productCostEvaluator.evaluateCost(primeProductCost);
        outputValues.setProductPrimeCost(primeProductCost);
        outputValues.setProductCost(productCost);
        setTextFieldsText(outputValues);
        writeOutputValuesToFile(outputValues);
    }

    private void setTextFieldsText(OutputValues outputValues) {
        codeLines.setText(String.format("%.2f", outputValues.getCodeLinesCount()));
        peopleTextField.setText(String.format("%.2f", outputValues.getLaboriousness()));
        producitivityTextField.setText(String.format("%.2f", outputValues.getProductivity()));
        performersTextField.setText(String.format("%.2f", outputValues.getPerformersCount()));
        timeTextField.setText(String.format("%.2f", outputValues.getProductCreationNeededTime()));
        mainSalaryTextField.setText(String.format("%.2f", outputValues.getMainPerformerSalary()));
        additionalSalaryTextField.setText(String.format("%.2f", outputValues.getAdditionalPerformerSalary()));
        totalComputerCostsTextField.setText(String.format("%.2f", outputValues.getTotalComputerUsageCosts()));
        primeCostTextField.setText(String.format("%.2f", outputValues.getProductPrimeCost()));
        productCostTextField.setText(String.format("%.2f", outputValues.getProductCost()));
    }

    private void writeOutputValuesToFile(OutputValues outputValues) throws URISyntaxException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(Files.createFile(Paths.get("output.json")).toFile(), outputValues);
        } catch (FileAlreadyExistsException e) {
            File file = Paths.get("output.json").toFile();
            objectMapper.writeValue(file, outputValues);
        }

    }

    private double evaluateQualificationCoefficient(double employeeExperience) throws URISyntaxException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(Paths.get(ClassLoader.getSystemResource("emp_qualif_coef.json").toURI()).toFile());
        if (employeeExperience <= 2) {
            return rootNode.get("<= 2 years").doubleValue();
        } else if (employeeExperience >= 2 && employeeExperience <= 3) {
            return rootNode.get("2-3 years").doubleValue();
        } else if (employeeExperience >= 3 && employeeExperience <= 5) {
            return rootNode.get("3-5 years").doubleValue();
        } else if (employeeExperience >= 5 && employeeExperience <= 10) {
            return rootNode.get("5-10 years").doubleValue();
        }
        return rootNode.get("> 10 years").doubleValue();
    }

    private double evaluateCodeLines(StartValues startValues) throws Throwable {
        double conventionalCoefficient = Double.parseDouble(String.valueOf(softwareClassification.get(startValues.getTaskType())));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(Paths.get(ClassLoader.getSystemResource("innov_complexity_coef.json").toURI()).toFile());
        double complexityCoefficient = rootNode.get("languageLevel")
                .get(startValues.getLanguageLevel())
                .get("complexityGroup")
                .get(String.valueOf(startValues.getComplexityLevel()))
                .get("innovationLevel")
                .get(String.valueOf(startValues.getInnovationLevel()))
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