package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlopeOneImpl implements Recommender{

    @Autowired
    private DataModel dm;
    private Map<User, Map<Contribution, Double>> inputData;
    private final Map<Contribution, Map<Contribution, Double>> diff;
    private final Map<Contribution, Map<Contribution, Integer>> freq;
    private final Map<User, Map<Contribution, Double>> outputData;

    public SlopeOneImpl() {
        this.diff = new HashMap<>();
        this.freq = new HashMap<>();
        this.outputData = new HashMap<>();
    }

    private void buildDiffFreqMatrix() {
        if (this.dm == null) this.dm = new DataModel();
        this.inputData = dm.createDataModel();
        for (Map<Contribution, Double> userRating : inputData.values()) {
            for (Map.Entry<Contribution, Double> entry : userRating.entrySet()) {
                if (!diff.containsKey(entry.getKey())) {
                    diff.put(entry.getKey(), new HashMap<>());
                    freq.put(entry.getKey(), new HashMap<>());
                }

                for (Map.Entry<Contribution, Double> entry2 : userRating.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(entry.getKey()).containsKey(entry2.getKey())) {
                        oldCount = freq.get(entry.getKey()).get(entry2.getKey());
                    }

                    double oldDiff = 0.0;
                    if (diff.get(entry.getKey()).containsKey(entry2.getKey())) {
                        oldDiff = diff.get(entry.getKey()).get(entry2.getKey());
                    }

                    double observedDiff = entry.getValue() - entry2.getValue();
                    freq.get(entry.getKey()).put(entry2.getKey(), oldCount + 1);
                    diff.get(entry.getKey()).put(entry2.getKey(), oldDiff + observedDiff);
                }
            }
        }

        for (Contribution cont : diff.keySet()) {
            for (Contribution c : diff.get(cont).keySet()) {
                double oldValue = diff.get(cont).get(c);
                int count = freq.get(cont).get(c);
                diff.get(cont).put(c, oldValue / count);
            }
        }
    }

    public Map<User, Map<Contribution, Double>> predictRecommendations() {
        buildDiffFreqMatrix();
        Map<Contribution, Double> uPred = new HashMap<>();
        Map<Contribution, Integer> uFreq = new HashMap<>();
        for (Contribution cont : diff.keySet()) {
            uPred.put(cont, 0.0);
            uFreq.put(cont, 0);
        }

        for (Map.Entry<User, Map<Contribution, Double>> entry : inputData.entrySet()) {
            for (Contribution j : entry.getValue().keySet()) {
                for (Contribution k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j) + entry.getValue().get(j);
                        double finalValue = predictedValue * freq.get(k).get(j);
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
                    } catch (NullPointerException ignored) {

                    }
                }
            }

            HashMap<Contribution, Double> clean = new HashMap<>();
            for (Contribution c : uPred.keySet()) {
                if (uFreq.get(c) > 0) {
                    clean.put(c, uPred.get(c) / uFreq.get(c));
                }
            }

            for (Contribution c : dm.getContributions()) {
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

    @Override
    public List<Contribution> findRecommendations(User user) {
        Map<Contribution, Double> recommendationMatrixOfUser = predictRecommendations().get(user);
        Map<Contribution, Double> ratingsOfTheUser = dm.getData().get(user);
        recommendationMatrixOfUser.values().removeIf(val -> val <= 2.5);

        for (Map.Entry<Contribution, Double> entry : ratingsOfTheUser.entrySet()) {
            recommendationMatrixOfUser.remove(entry.getKey());
        }

        List<Contribution> recommendationList = new ArrayList<>(recommendationMatrixOfUser.keySet());
        recommendationList.sort(Contribution.contributionComparator);
        return recommendationList;
    }
}
