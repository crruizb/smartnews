package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;

import java.util.List;

public interface Recommender {

    public List<Contribution> findRecommendations(User user);
}
