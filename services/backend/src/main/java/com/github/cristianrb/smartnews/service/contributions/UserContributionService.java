package com.github.cristianrb.smartnews.service.contributions;

import java.security.Principal;

public interface UserContributionService {

    public void voteContribution(int vote, int contributionId, Principal principal);
}
