CREATE TABLE customer
(
    id              bigint  NOT NULL,
    created_by      character varying(255),
    created_on      timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted         boolean,
    last_updated_by character varying(255),
    last_updated_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version         integer NOT NULL,
    name            character varying(255),
    no_of_rides     integer,
    rating          double precision
);
CREATE TABLE customer_lane_preference
(
    id                 bigint  NOT NULL,
    created_by         character varying(255),
    created_on         timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted            boolean,
    last_updated_by    character varying(255),
    last_updated_on    timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version            integer NOT NULL,
    customer_id        bigint,
    from_demand_center bigint,
    to_demand_center   bigint,
    weight             integer
);
CREATE TABLE demand_center
(
    id              bigint  NOT NULL,
    created_by      character varying(255),
    created_on      timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted         boolean,
    last_updated_by character varying(255),
    last_updated_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version         integer NOT NULL,
    lat             double precision,
    lng             double precision,
    name            character varying(255),
    radius          double precision
);
CREATE TABLE demand_center_prediction
(
    id               bigint  NOT NULL,
    created_by       character varying(255),
    created_on       timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted          boolean,
    last_updated_by  character varying(255),
    last_updated_on  timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version          integer NOT NULL,
    demand_center_id bigint,
    from_time        time without time zone,
    idle_wait_mins   bigint,
    to_time          time without time zone
);
CREATE TABLE demand_lane_prediction
(
    id                    bigint  NOT NULL,
    created_by            character varying(255),
    created_on            timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted               boolean,
    last_updated_by       character varying(255),
    last_updated_on       timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version               integer NOT NULL,
    estimated_demand      bigint,
    from_demand_center_id bigint,
    from_time             time without time zone,
    profit_margin         double precision,
    to_demand_center_id   bigint,
    to_time               time without time zone
);
CREATE TABLE vehicle
(
    id                 bigint  NOT NULL,
    created_by         character varying(255),
    created_on         timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted            boolean,
    last_updated_by    character varying(255),
    last_updated_on    timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version            integer NOT NULL,
    registrationnumber character varying(255)
);
CREATE TABLE vehicle_location
(
    id              bigint  NOT NULL,
    created_by      character varying(255),
    created_on      timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted         boolean,
    last_updated_by character varying(255),
    last_updated_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version         integer NOT NULL,
    idlesince       timestamp without time zone,
    lat             double precision,
    lng             double precision,
    p_timestamp     timestamp without time zone,
    vehicle_id      bigint
);
