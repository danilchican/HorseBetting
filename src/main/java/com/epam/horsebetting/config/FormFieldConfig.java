package com.epam.horsebetting.config;

public final class FormFieldConfig {

    public final class User {
        private static final String PREFIX = "user-";
        public static final String NAME_FIELD = PREFIX + "name";
        public static final String EMAIL_FIELD = "email";
        public static final String PASSWORD_FIELD = "password";
        public static final String CONFIRMATION_FIELD = "confirmation";
        public static final String PAYMENT_AMOUNT_FIELD = "payment-amount";
    }

    public final class Suit {
        public static final String ID_FIELD = "id";
        public static final String NAME_FIELD = "name";
    }

    public final class Horse {
        private static final String PREFIX = "horse-";
        public static final String ID_FIELD = PREFIX + "id";
        public static final String NAME_FIELD = PREFIX + "name";
        public static final String GENDER_FIELD = PREFIX + "gender";
        public static final String AGE_FIELD = PREFIX + "age";
        public static final String SUIT_FIELD = PREFIX + "suit";
    }

    public final class Race {
        private static final String PREFIX = "race-";
        public static final String ID_FIELD = PREFIX + "id";
        public static final String TITLE_FIELD = PREFIX + "title";
        public static final String PLACE_FIELD = PREFIX + "place";
        public static final String MIN_RATE_FIELD = PREFIX + "min-rate";
        public static final String TRACK_LENGTH_FIELD = PREFIX + "track-length";
        public static final String FINISHED_FIELD = PREFIX + "is-finished";
        public static final String BET_END_DATE_FIELD = PREFIX + "bet-end-date";
        public static final String STARTED_AT_FIELD = PREFIX + "started-at";
        public static final String SELECTED_HORSES_FIELD = "selected-horses";
        public static final String HORSE_COEFFICIENTS_FIELD = "horse-coefficients";
    }

    public final class Bet {
        public static final String ID = "id";
        public static final String PARTICIPANT_ID = "participant_id";
        public static final String AMOUNT = "amount";
    }

    public final class Locale {
        public static final String LANG_FIELD = "lang";
    }

    public final class Pagination {
        public static final String PAGE_FIELD = "page";
    }
}
