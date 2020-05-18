package sample;

public class ComputerUsageCostEvaluator {

    private double evaluateComputerUptime(int workingDays, int computerWorkingOursPerDay) {
        return workingDays * computerWorkingOursPerDay * 0.9;
    }

    private double evaluateElectricityCosts(int hoursPerYear, double oneKvtCost, double powerInKvt) {
        return hoursPerYear * oneKvtCost * powerInKvt;
    }

    private double evaluateMainComputerUsersSalary(double fareCoefficient, int computersCount, double bonusPercent) {
        return ((1600 * fareCoefficient) / computersCount ) * (1 + bonusPercent) * 12;
    }

    private double evaluateAdditionalComputerUsersSalary(double mainSalary, double additionalSalaryPercent) {
        return mainSalary * additionalSalaryPercent;
    }

    private double evaluateComputerUserESV(double mainSalary, double additionalSalary) {
        return (mainSalary + additionalSalary) * 0.347;
    }

    private double evaluateDepreciationDeduction(double balanceCost, int yearsInUse) {
        return balanceCost / yearsInUse;
    }

    private double evaluateHourlyComputerUsageCosts(double sumOfYearCosts, double computerUptime) {
        return sumOfYearCosts / computerUptime;
    }



}
