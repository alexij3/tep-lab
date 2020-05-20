package sample.evaluator;

import sample.dto.StartValues;

public class SoftwareLaboriousnessAndDevelopmentTimeEvaluator {

    public double evaluateEveryDevelopmentStageTime(StartValues startValues, double codeLines, double qualificationCoefficient) {
        return startValues.getObjectiveDescriptionTime() + evaluateObjectiveDescriptionTime(codeLines, 1.35, qualificationCoefficient)
                + evaluateAlgorithmAndBlockDiagramDevelopmentTime(codeLines, qualificationCoefficient)
                + evaluateProgrammingTime(codeLines, qualificationCoefficient)
                + evaluateConfigurationAndTestingTime(codeLines, qualificationCoefficient)
                + startValues.getDocumentationTime();
    }

    public double evaluateSoftwareCodeLines(double conventionalCoefficient, double innovationAndComplexityCoefficient) {
        return Math.abs(conventionalCoefficient * innovationAndComplexityCoefficient);
    }

    public double evaluateLaboriousness(double codeLinesThousands) {
        return 3.6 * (Math.pow(codeLinesThousands, 1.2));
    }

    public double evaluateDevelopmentTime(double laboriousness) {
        return 2.5 * Math.pow(laboriousness, 0.32);
    }

    public double evaluateAveragePerformanceNumber(double laboriousness, double developmentTime) {
        return laboriousness / developmentTime;
    }

    public double evaluateProductivityOfDevelopersGroup(double laboriousness, double codeLinesThousands) {
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
