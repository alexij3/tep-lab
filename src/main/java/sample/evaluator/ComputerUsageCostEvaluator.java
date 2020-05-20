package sample.evaluator;

public class ComputerUsageCostEvaluator {

    public double evaluateComputerUptime(int workingDays, double computerWorkingHoursPerDay) {
        return workingDays * computerWorkingHoursPerDay * 0.9;
    }

    public double evaluateElectricityCosts(double hoursPerYear, double oneKvtCost, double powerInKvt) {
        return hoursPerYear * oneKvtCost * powerInKvt;
    }

    public double evaluateMainComputerUsersSalary(double monthlySalary, double fareCoefficient, int computersCount, double bonusPercent) {
        return ((monthlySalary * fareCoefficient) / computersCount ) * (1 + bonusPercent) * 12;
    }

    public double evaluateAdditionalComputerUsersSalary(double mainSalary, double additionalSalaryPercent) {
        return mainSalary * additionalSalaryPercent;
    }

    public double evaluateComputerUserESV(double mainSalary, double additionalSalary) {
        return (mainSalary + additionalSalary) * 0.347;
    }

    public double evaluateDepreciationDeduction(double balanceCost, int yearsInUse) {
        return balanceCost / yearsInUse;
    }

    public double evaluateHourlyComputerUsageCosts(double sumOfYearCosts, double computerUptime) {
        return sumOfYearCosts / computerUptime;
    }

    public double evaluateTotalComputerUsageCosts(double hourlyComputerUsageCosts, int productCreationTime) {
        return hourlyComputerUsageCosts * productCreationTime;
    }



}
