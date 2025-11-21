package com.gamesUP.gamesUP.web.dto.reco;

import java.util.List;

public class RecommendationsResponse {
    public long user_id;
    public List<RecommendationItem> recommendations;
    public String model_version;
}
