package sample.evaluator;

public class SalaryEvaluator {

    private double evaluateMainSalary(double fareCoefficient, double productCreationTime, int workingDays, int workingHoursPerDay, double bonusPercent) {
        return ((1600 * fareCoefficient * productCreationTime) / (workingDays * workingHoursPerDay)) * (1 + bonusPercent);
    }

    private double evaluateAdditionalSalary(double mainSalary, double additionalSalaryPercent) {
        return mainSalary * (additionalSalaryPercent);
    }

    private double evaluateESV(double mainSalary) {
        return (mainSalary * 0.347);
    }

}
