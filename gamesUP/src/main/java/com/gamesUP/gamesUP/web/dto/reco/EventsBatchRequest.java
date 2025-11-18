package com.gamesUP.gamesUP.web.dto.reco;

import java.util.List;

public class EventsBatchRequest {
    public List<EventItem> events;

    public static class EventItem {
        public String type; // PURCHASE | RATING | VIEW
        public long user_id;
        public int game_id;
        public Double value; // optional
        public String ts;    // ISO timestamp
    }
}
