package sample.evaluator;

public class ProductCostEvaluator {

    private double evaluatePrimeCost(double performerMainSalary, double performerAdditionalSalary, double ESV, double productESV) {
        return performerMainSalary + performerAdditionalSalary + ESV + productESV;
    }

    private double evaluateCost(double primeCost) {
        return primeCost * (1 + 0.4);
    }

}
