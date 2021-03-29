package com.github.cristianrb.smartnews;

import com.github.cristianrb.smartnews.cf.DataModel;
import com.github.cristianrb.smartnews.cf.SlopeOneImpl;
import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SlopeOneTest {

    @Mock
    DataModel dataModel;

    @InjectMocks
    SlopeOneImpl algorithm = new SlopeOneImpl();

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
