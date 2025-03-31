package com.github.cristianrb.smartnews;

import com.github.cristianrb.smartnews.cf.DataModel;
import com.github.cristianrb.smartnews.cf.SlopeOneImpl;
import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SlopeOneTest {

    @Mock
    DataModel dataModel;

    @InjectMocks
    SlopeOneImpl algorithm = new SlopeOneImpl();

    @Test
    public void testpredictRecommendationsSimple() {
        List<Contribution> constList = new ArrayList<>();
        constList.add(new Contribution(1, "Cont1", "1"));
        constList.add(new Contribution(2, "Cont2", "2"));
        constList.add(new Contribution(3, "Cont3", "3"));

        Map<User, Map<Contribution, Double>> inputData = new HashMap<>();
        Map<Contribution, Double> ratingsUser100 = new HashMap<>();
        ratingsUser100.put(constList.get(0), 3.0);
        ratingsUser100.put(constList.get(1), 5.0);
        inputData.put(new User("100"), ratingsUser100);

        Map<Contribution, Double> ratingsUser200 = new HashMap<>();
        ratingsUser200.put(constList.get(0), 4.0);
        ratingsUser200.put(constList.get(1), 3.0);
        ratingsUser200.put(constList.get(2), 3.0);
        inputData.put(new User("200"), ratingsUser200);

        when(dataModel.createDataModel()).thenReturn(inputData);
        when(dataModel.getData()).thenReturn(inputData);
        when(dataModel.getContributions()).thenReturn(new HashSet<>(constList));

        Map<User, Map<Contribution, Double>> predictions = algorithm.predictRecommendations();

        Map<Contribution, Double> predictionsUser100 = new HashMap<>();
        predictionsUser100.put(constList.get(0), 3.0);
        predictionsUser100.put(constList.get(1), 5.0);
        predictionsUser100.put(constList.get(2), 3.5);
        Map<Contribution, Double> predictionsUser200 = new HashMap<>();
        predictionsUser200.put(constList.get(0), 4.0);
        predictionsUser200.put(constList.get(1), 3.0);
        predictionsUser200.put(constList.get(2), 3.0);

        Map<User, Map<Contribution, Double>> expectedPredictions = new HashMap<>();
        expectedPredictions.put(new User("100"), predictionsUser100);
        expectedPredictions.put(new User("200"), predictionsUser200);

        Assertions.assertEquals(predictions, expectedPredictions);
    }

    @Test
    public void testFindRecommendationOneExpected() {
        List<Contribution> constList = new ArrayList<>();
        constList.add(new Contribution(1, "Cont1", "1"));
        constList.add(new Contribution(2, "Cont2", "2"));
        constList.add(new Contribution(3, "Cont3", "3"));

        Map<User, Map<Contribution, Double>> inputData = new HashMap<>();
        Map<Contribution, Double> ratingsUser100 = new HashMap<>();
        ratingsUser100.put(constList.get(0), 3.0);
        ratingsUser100.put(constList.get(1), 5.0);
        inputData.put(new User("100"), ratingsUser100);

        Map<Contribution, Double> ratingsUser200 = new HashMap<>();
        ratingsUser200.put(constList.get(0), 4.0);
        ratingsUser200.put(constList.get(1), 3.0);
        ratingsUser200.put(constList.get(2), 3.0);
        inputData.put(new User("200"), ratingsUser200);

        when(dataModel.createDataModel()).thenReturn(inputData);
        when(dataModel.getData()).thenReturn(inputData);
        when(dataModel.getContributions()).thenReturn(new HashSet<>(constList));

        List<Contribution> conts = algorithm.findRecommendations(new User("100"));
        List<Contribution> contsExpected = new ArrayList<>();
        contsExpected.add(constList.get(2));
        Assertions.assertEquals(conts, contsExpected);
    }

    @Test
    public void testFindRecommendationZeroExpected() {
        List<Contribution> constList = new ArrayList<>();
        constList.add(new Contribution(1, "Cont1", "1"));
        constList.add(new Contribution(2, "Cont2", "2"));
        constList.add(new Contribution(3, "Cont3", "3"));

        Map<User, Map<Contribution, Double>> inputData = new HashMap<>();
        Map<Contribution, Double> ratingsUser100 = new HashMap<>();
        ratingsUser100.put(constList.get(0), 3.0);
        ratingsUser100.put(constList.get(1), 5.0);
        inputData.put(new User("100"), ratingsUser100);

        Map<Contribution, Double> ratingsUser200 = new HashMap<>();
        ratingsUser200.put(constList.get(0), 4.0);
        ratingsUser200.put(constList.get(1), 3.0);
        ratingsUser200.put(constList.get(2), 0.0);
        inputData.put(new User("200"), ratingsUser200);

        when(dataModel.createDataModel()).thenReturn(inputData);
        when(dataModel.getData()).thenReturn(inputData);
        when(dataModel.getContributions()).thenReturn(new HashSet<>(constList));

        List<Contribution> conts = algorithm.findRecommendations(new User("100"));
        List<Contribution> contsExpected = new ArrayList<>();
        Assertions.assertEquals(conts, contsExpected);
    }

    @Test
    public void testpredictRecommendationsExtended() {
        List<Contribution> constList = new ArrayList<>();
        constList.add(new Contribution(1, "Cont1", "1"));
        constList.add(new Contribution(2, "Cont2", "2"));
        constList.add(new Contribution(3, "Cont3", "3"));

        Map<User, Map<Contribution, Double>> inputData = new HashMap<>();
        Map<Contribution, Double> ratingsUser100 = new HashMap<>();
        ratingsUser100.put(constList.get(0), 1.0);
        ratingsUser100.put(constList.get(1), 3.0);
        ratingsUser100.put(constList.get(2), 3.0);
        inputData.put(new User("100"), ratingsUser100);

        Map<Contribution, Double> ratingsUser200 = new HashMap<>();
        ratingsUser200.put(constList.get(0), 3.0);
        ratingsUser200.put(constList.get(1), 4.0);
        inputData.put(new User("200"), ratingsUser200);

        Map<Contribution, Double> ratingsUser300 = new HashMap<>();
        ratingsUser300.put(constList.get(1), 2.0);
        ratingsUser300.put(constList.get(2), 5.0);
        inputData.put(new User("300"), ratingsUser300);

        when(dataModel.createDataModel()).thenReturn(inputData);
        when(dataModel.getData()).thenReturn(inputData);
        when(dataModel.getContributions()).thenReturn(new HashSet<>(constList));

        Map<User, Map<Contribution, Double>> predictions = algorithm.predictRecommendations();

        Map<Contribution, Double> predictionsUser100 = new HashMap<>();
        predictionsUser100.put(constList.get(0), 1.0);
        predictionsUser100.put(constList.get(1), 3.0);
        predictionsUser100.put(constList.get(2), 3.0);
        Map<Contribution, Double> predictionsUser200 = new HashMap<>();
        predictionsUser200.put(constList.get(0), 3.0);
        predictionsUser200.put(constList.get(1), 4.0);
        predictionsUser200.put(constList.get(2), 4.25);
        Map<Contribution, Double> predictionsUser300 = new HashMap<>();
        predictionsUser300.put(constList.get(0), 1.75);
        predictionsUser300.put(constList.get(1), 2.0);
        predictionsUser300.put(constList.get(2), 5.0);

        Map<User, Map<Contribution, Double>> expectedPredictions = new HashMap<>();
        expectedPredictions.put(new User("100"), predictionsUser100);
        expectedPredictions.put(new User("200"), predictionsUser200);
        expectedPredictions.put(new User("300"), predictionsUser300);

        Assertions.assertEquals(predictions, expectedPredictions);
    }

    @Test
    public void testFindRecommendationWithThreeUsers() {
        List<Contribution> constList = new ArrayList<>();
        constList.add(new Contribution(1, "Cont1", "1"));
        constList.add(new Contribution(2, "Cont2", "2"));
        constList.add(new Contribution(3, "Cont3", "3"));

        Map<User, Map<Contribution, Double>> inputData = new HashMap<>();
        Map<Contribution, Double> ratingsUser100 = new HashMap<>();
        ratingsUser100.put(constList.get(0), 5.0);
        ratingsUser100.put(constList.get(1), 3.0);
        ratingsUser100.put(constList.get(2), 2.0);
        inputData.put(new User("100"), ratingsUser100);

        Map<Contribution, Double> ratingsUser200 = new HashMap<>();
        ratingsUser200.put(constList.get(0), 3.0);
        ratingsUser200.put(constList.get(1), 4.0);
        inputData.put(new User("200"), ratingsUser200);

        Map<Contribution, Double> ratingsUser300 = new HashMap<>();
        ratingsUser300.put(constList.get(1), 2.0);
        ratingsUser300.put(constList.get(2), 5.0);
        inputData.put(new User("300"), ratingsUser300);

        when(dataModel.createDataModel()).thenReturn(inputData);
        when(dataModel.getData()).thenReturn(inputData);
        when(dataModel.getContributions()).thenReturn(new HashSet<>(constList));

        List<Contribution> conts = algorithm.findRecommendations(new User("300"));
        List<Contribution> contsExpected = new ArrayList<>();
        contsExpected.add(constList.get(0));
        Assertions.assertEquals(conts, contsExpected);
    }

}
