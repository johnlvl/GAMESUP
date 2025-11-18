package com.gamesUP.gamesUP.web.dto.reco;

import java.util.List;

public class RecommendationsRequest {
    public long user_id;
    public Integer k;
    public List<Integer> candidates;
}
