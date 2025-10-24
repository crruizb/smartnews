package com.github.cristianrb.smartnews.service.ai;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;
import java.util.List;

public interface RecommendationsService {
    List<Contribution> recommendations(User user);
}