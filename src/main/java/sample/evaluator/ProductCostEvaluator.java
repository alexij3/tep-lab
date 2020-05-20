package sample.evaluator;

public class ProductCostEvaluator {

    public double evaluatePrimeCost(double performerMainSalary, double performerAdditionalSalary, double ESV, double productESV) {
        return performerMainSalary + performerAdditionalSalary + ESV + productESV;
    }

    public double evaluateCost(double primeCost) {
        return primeCost * (1 + 0.4);
    }

}
