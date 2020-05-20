package sample.evaluator;

public class SalaryEvaluator {

    public double evaluateMainSalary(double monthlySalary, double fareCoefficient, double productCreationTime, int workingDays, double workingHoursPerDay, double bonusPercent) {
        return ((monthlySalary * fareCoefficient * productCreationTime) / (workingDays * workingHoursPerDay)) * (1 + bonusPercent);
    }

    public double evaluateAdditionalSalary(double mainSalary, double additionalSalaryPercent) {
        return mainSalary * (additionalSalaryPercent);
    }

    public double evaluateESV(double mainSalary) {
        return (mainSalary * 0.347);
    }

}
