package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class Recommender {

    private Map<User, Map<Contribution, Double>> inputData;
    private Map<Contribution, Map<Contribution, Double>> diff;
    private Map<Contribution, Map<Contribution, Integer>> freq;
    private Map<User, Map<Contribution, Double>> outputData;

    public Recommender(Map<User, Map<Contribution, Double>> inputData) {
        this.inputData = inputData;
        this.diff = new HashMap<>();
        this.freq = new HashMap<>();
        this.outputData = new HashMap<User, Map<Contribution, Double>>();
    }

    public void buildDiffFreqMatrix() {
        for (Map<Contribution, Double> userRating : inputData.values()) {
            for (Map.Entry<Contribution, Double> entry : userRating.entrySet()) {
                if (!diff.containsKey(entry.getKey())) {
                    diff.put(entry.getKey(), new HashMap<>());
                    freq.put(entry.getKey(), new HashMap<>());
                }

                for (Map.Entry<Contribution, Double> entry2 : userRating.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(entry.getKey()).containsKey(entry2.getKey())) {
                        oldCount = freq.get(entry.getKey()).get(entry2.getKey()).intValue();
                    }

                    double oldDiff = 0;
                    if (diff.get(entry.getKey()).containsKey(entry2.getKey())) {
                        oldDiff = diff.get(entry.getKey()).get(entry2.getKey()).doubleValue();
                    }

                    double observedDiff = entry.getValue() - entry2.getValue();
                    freq.get(entry.getKey()).put(entry2.getKey(), oldCount + 1);
                    diff.get(entry.getKey()).put(entry2.getKey(), oldDiff + observedDiff);
                }
            }
        }

        for (Contribution cont : diff.keySet()) {
            for (Contribution c : diff.get(cont).keySet()) {
                double oldValue = diff.get(cont).get(c).doubleValue();
                int count = freq.get(cont).get(c).intValue();
                diff.get(cont).put(c, oldValue / count);
            }
        }

        System.out.println("Hola");
    }

    public Map<User, Map<Contribution, Double>> getRecommendationMatrix() {
        buildDiffFreqMatrix();
        Map<Contribution, Double> uPred = new HashMap<>();
        Map<Contribution, Integer> uFreq = new HashMap<>();
        for (Contribution cont : freq.keySet()) {
            uPred.put(cont, 0.0);
            uFreq.put(cont, 0);
        }

        for (Map.Entry<User, Map<Contribution, Double>> entry : inputData.entrySet()) {
            for (Contribution j : entry.getValue().keySet()) {
                for (Contribution k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j) + entry.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
                    } catch (NullPointerException e) {

                    }
                }
            }

            Map<Contribution, Double> clean = new HashMap<>();
            for (Contribution c : uPred.keySet()) {
                if (uFreq.get(c) > 0) {
                    clean.put(c, uPred.get(c).doubleValue() / uFreq.get(c).intValue());
                }
            }

            for (Contribution c : DataModel.contributions) {
                if (entry.getValue().containsKey(c)) {
                    clean.put(c, entry.getValue().get(c));
                } else if (!clean.containsKey(c)) {
                    clean.put(c, -1.0);
                }
            }

            outputData.put(entry.getKey(), clean);
        }
       return outputData;
    }

    private static void printData(Map<User, Map<Contribution, Double>> data) {
        for (User user : data.keySet()) {
            System.out.println(user + ":");
            print(data.get(user));
        }
    }

    private static void print(Map<Contribution, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (Contribution j : hashMap.keySet()) {
            System.out.println(" " + j.getId() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }
}
