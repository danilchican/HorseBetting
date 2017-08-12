package com.epam.horsebetting.config;

public final class SQLFieldConfig {

    public static final String TOTAL = "total";

    public final class User {
        public static final String ID = "id";
        public static final String ROLE_ID = "role_id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String BALANCE = "balance";
        public static final String CREATED_AT = "created_at";
    }

    public final class Suit {
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public final class Horse {
        public static final String ID = "id";
        public static final String SUIT_ID = "suit_id";
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String GENDER = "gender";
        public static final String SUIT_NAME = "suit_name";
    }

    public final class Race {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String PLACE = "place";
        public static final String MIN_RATE = "min_rate";
        public static final String TRACK_LENGTH = "track_length";
        public static final String FINISHED = "is_finished";
        public static final String CREATED_AT = "created_at";
        public static final String STARTED_AT = "started_at";
    }
}
