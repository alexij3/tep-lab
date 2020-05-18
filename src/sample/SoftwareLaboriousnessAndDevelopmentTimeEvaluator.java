package sample;

public class SoftwareLaboriousnessAndDevelopmentTimeEvaluator {



    private double evaluateSoftwareCodeLines(double conventionalCoefficient, double innovationAndComplexityCoefficient) {
        return Math.abs(conventionalCoefficient * innovationAndComplexityCoefficient);
    }

    private double evaluateLaboriousness(double codeLinesThousands) {
        return 3.6 * (Math.pow(codeLinesThousands, 1.2));
    }

    private double evaluateDevelopmentTime(double laboriousness) {
        return 2.5 * Math.pow(laboriousness, 0.32);
    }

    private double evaluateAveragePerformanceNumber(double laboriousness, double developmentTime) {
        return laboriousness / developmentTime;
    }

    private double evaluateProductivityOfDevelopersGroup(double laboriousness, double codeLinesThousands) {
        return Math.abs((1000 * codeLinesThousands) / laboriousness);
    }

    private double evaluateObjectiveDescriptionTime(double codeLines, double objectiveChangesCoefficient, double qualificationCoefficient) {
        return (codeLines * objectiveChangesCoefficient) / (50 * qualificationCoefficient);
    }

    private double evaluateAlgorithmAndBlockDiagramDevelopmentTime(double codeLines, double qualificationCoefficient) {
        return codeLines / (50 * qualificationCoefficient);
    }

    private double evaluateProgrammingTime(double codeLines, double qualificationCoefficient) {
        return (codeLines * 1.5) / (50 * qualificationCoefficient);
    }

    private double evaluateConfigurationAndTestingTime(double codeLines, double qualificationCoefficient) {
        return (codeLines * 4.2) / (50 * qualificationCoefficient);
    }

    

}
