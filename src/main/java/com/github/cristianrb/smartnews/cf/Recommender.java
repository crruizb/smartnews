package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;

import java.util.List;
import java.util.Map;

public interface Recommender {

    public List<Contribution> findRecommendations(User user);
}
