CREATE TABLE users(
	cid varchar(30) not null primary key,
	email varchar(200) not null unique,
	email_vatsim varchar(200) not null unique,
	password varchar(100) not null,
	first_name varchar(100) not null,
	last_name varchar(100),
    is_active boolean not null default false,
	last_login timestamp,
	created_on timestamp not null default NOW(),
	edited_on timestamp,
	activated_on timestamp,
	highest_controller_rating int not null default 1,
	password_reset_token varchar(100)
);

INSERT INTO users VALUES ('1720051', 'aarshinkov9705@gmail.com', 'aarshinkov9705@gmail.com', '$2a$12$NtFfJNxLCgGtuopIsyt0g.AHjSD0lcBnMMMqBExtXmqUm4YwEK9sO', 'Atanas', 'Arshinkov', true, null, NOW(), null);

CREATE TABLE roles(
	rolename varchar(70) not null primary key,
	description varchar(200)
);

INSERT INTO roles VALUES ('SYS_ADMIN', 'System administrator');
INSERT INTO roles VALUES ('STAFF_DIRECTOR', 'Staff director'); 
INSERT INTO roles VALUES ('STAFF_EVENTS', 'Staff events'); 
INSERT INTO roles VALUES ('STAFF_TRAINING', 'Staff training');
INSERT INTO roles VALUES ('ATC_S1', 'ATC rating S1');
INSERT INTO roles VALUES ('ATC_S2', 'ATC rating S2');
INSERT INTO roles VALUES ('ATC_S3', 'ATC rating S3');
INSERT INTO roles VALUES ('ATC_C1', 'ATC rating C1');
INSERT INTO roles VALUES ('ATC_C3', 'ATC rating C3');
INSERT INTO roles VALUES ('ATC_I1', 'ATC rating I1');
INSERT INTO roles VALUES ('ATC_I3', 'ATC rating I3');
INSERT INTO roles VALUES ('ATC_TRAINING', 'ATC training');
INSERT INTO roles VALUES ('EXAMINER', 'Examiner');
INSERT INTO roles VALUES ('USER', 'Regular user');
INSERT INTO roles VALUES ('BLOG_MANAGER', 'Manages blog events');

CREATE TABLE user_roles(
	user_role_id varchar(100) not null primary key default gen_random_uuid(),
	cid varchar(30) not null references users(cid) on delete cascade,
	rolename varchar(50) not null references roles(rolename) on delete cascade,
	created_on timestamp not null default NOW(),
	unique (cid, rolename)
);

INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'USER');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'SYS_ADMIN');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'STAFF_EVENTS');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'STAFF_TRAINING');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'ATC_S3');

CREATE TABLE mailbox(
	mail_id varchar(100) not null primary key default gen_random_uuid(),
	sender varchar(250) not null,
	receivers varchar(2000) not null,
	subject varchar(300) not null,
	content text not null,
	is_sent boolean not null default false,
	created_on timestamp not null default NOW(),
	sent_on timestamp
);

CREATE TABLE event_types (
    type varchar(10) primary key
);

INSERT INTO event_types (type) VALUES ('event'), ('cpt'), ('vasops');

CREATE TABLE events (
    event_id int not null primary key,
    name varchar(255) not null,
    type varchar(10) not null references event_types(type),
	priority int not null default 1,
	cpt_rating_number int,
	cpt_rating_symbol varchar(10),
	cpt_examinee varchar(100),
    description text,
    short_description text,
	vatsim_event_url varchar(500),
	vateud_event_url varchar(500),
    image_url varchar(255),
    start_at timestamp not null,
    end_at timestamp not null,
    created_at timestamptz not null default NOW(),
    updated_at timestamptz
);

CREATE TABLE event_icaos (
	event_icao_id varchar(100) not null primary key default gen_random_uuid(),
	event_id int not null references events(event_id) on delete cascade,
	icao varchar(10) not null,
	CONSTRAINT unique_icao_event_id UNIQUE (event_id, icao)
);

CREATE TABLE event_connections (
    event_connection_id varchar(100) not null primary key default gen_random_uuid(),
    event_one_id int not null references events(event_id) on delete cascade,
    event_two_id int not null references events(event_id) on delete cascade,
    created_at timestamp not null default NOW(),
    event_one_least int GENERATED ALWAYS AS (LEAST(event_one_id, event_two_id)) STORED,
    event_one_greatest int GENERATED ALWAYS AS (GREATEST(event_one_id, event_two_id)) STORED,
    CONSTRAINT unique_event_connection UNIQUE (event_one_least, event_one_greatest)
);

CREATE TABLE sections (
	id varchar(50) not null primary key,
	name varchar(100) not null
);

CREATE TABLE tags (
    name VARCHAR(100) not null primary key
);

CREATE TABLE blog_posts (
	id varchar(100) not null primary key default gen_random_uuid(),
	title varchar(100) not null,
	content text not null,
	is_visible boolean not null default false,
	section_id varchar(50) references sections(id) on delete set null,
	cid varchar(30) references users(cid) on delete set null,
	created_at timestamptz not null default NOW(),
	updated_at timestamptz
);

CREATE TABLE blog_post_tags (
    blog_post_id varchar(100) not null references blog_posts(id) on delete cascade,
    tag_name varchar(100) not null references tags(name) on delete cascade, 
    primary key (blog_post_id, tag_name)
);

CREATE TABLE controllers_online_log (
	controller_online_log_id varchar(100) not null primary key default gen_random_uuid(),
	session_id bigint not null default -1,
	cid varchar(30),
	rating int not null,
	server varchar(50),
	position varchar(30) not null,
	session_started timestamptz not null default NOW(),
	session_ended timestamptz
);

CREATE TABLE user_events (
	user_cid varchar(30) not null references users(cid) on delete cascade,
	event_id int not null references events(event_id) on delete cascade,
	position varchar(50) not null
);

CREATE TABLE positions (
    position_id varchar(30) not null primary key,
    name VARCHAR(100) not null,
	order_priority int not null
);

INSERT INTO positions (position_id, name, order_priority) VALUES ('LBSR_CTR', 'Sofia Control', 1);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBSF_APP', 'Sofia Approach', 3);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBSF_TWR', 'Sofia Tower', 4);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBWN_APP', 'Varna Approach', 5);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBWN_TWR', 'Varna Tower', 6);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBBG_APP', 'Burgas Approach', 7);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBBG_TWR', 'Burgas Tower', 8);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBPD_TWR', 'Plovdiv Tower', 9);
INSERT INTO positions (position_id, name, order_priority) VALUES ('LBGO_TWR', 'Gorna Tower', 10);

CREATE TABLE event_positions (
    event_position_id varchar(100) not null primary key default gen_random_uuid(),
    event_id int not null references events(event_id) on delete cascade,
    position_id varchar(30) not null references positions(position_id),
	minimum_rating int,
	can_trainees_apply boolean not null default false,
    is_approved boolean not null default false,
    unique (event_id, position_id)
);

CREATE TABLE slots (
    slot_id varchar(100) not null primary key default gen_random_uuid(),
    event_position_id varchar(100) references event_positions(event_position_id) on delete cascade,
    start_time timestamp not null,
    end_time timestamp not null,
    user_cid varchar(30) references users(cid) on delete set null,
    is_approved boolean default false,
    unique (event_position_id, start_time, end_time)
);

CREATE TABLE user_event_applications (
    application_id varchar(100) not null primary key default gen_random_uuid(),
    user_cid varchar(30) not null references users(cid) on delete cascade,
	slot_id varchar(100) not null references slots(slot_id) on delete cascade,
    status boolean,
	applied_at timestamp not null default NOW(),
	is_added_by_staff boolean not null default false,
	added_by_staff_cid varchar(30) references users(cid) on delete set null,
	unique (user_cid, slot_id)
);

CREATE TABLE user_atc_authorized_positions (
	id varchar(100) not null primary key default gen_random_uuid(),
	user_cid varchar(30) not null references users(cid) on delete cascade,
	position_id varchar(30) not null references positions(position_id),
	is_position_manually_added boolean default false,
	expires_on timestamp,
	created_at timestamp not null default NOW()
);

CREATE TABLE atc_reservation_types (
	reservation_type varchar(40) not null primary key
);

INSERT INTO atc_reservation_types VALUES ('Normal');
INSERT INTO atc_reservation_types VALUES ('Training');

CREATE TABLE atc_reservations (
	reservation_id varchar(100) not null primary key default gen_random_uuid(),
	reservation_type varchar(40) not null references atc_reservation_types(reservation_type) on delete restrict,
	position_id varchar(30) not null references positions(position_id),
	user_cid varchar(30) not null references users(cid) on delete cascade,
	trainee_cid varchar(30) references users(cid) on delete cascade,
	is_canceled boolean not null default false,
	from_time timestamp not null,
	to_time timestamp not null,
	created_at timestamp not null default NOW()
);

CREATE TABLE mentor_trainees (
	mentor_trainee_id varchar(100) not null primary key default gen_random_uuid(),
	mentor_cid varchar(30) not null references users(cid) on delete cascade,
	trainee_cid varchar(30) references users(cid) on delete set null,
	position_id varchar(30) not null references positions(position_id),
	assigned_at timestamp not null default NOW()
);

CREATE OR REPLACE FUNCTION set_unique_status_per_user_slot()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status IS TRUE THEN
        UPDATE user_event_applications
        SET status = FALSE
        WHERE slot_id = NEW.slot_id
		AND application_id <> NEW.application_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_enforce_unique_status_per_user_slot
AFTER INSERT OR UPDATE ON user_event_applications
FOR EACH ROW
EXECUTE FUNCTION set_unique_status_per_user_slot();

CREATE TABLE saved_user_searches (
	id varchar(100) not null primary key default gen_random_uuid(),
	user_cid varchar(30) not null references users(cid) on delete cascade,
	searched_user_cid varchar(30) not null references users(cid) on delete cascade,
	added_at timestamp not null default NOW()
);

CREATE OR REPLACE FUNCTION enforce_single_atc_role()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.rolename LIKE 'ATC_%' AND NEW.rolename != 'ATC_TRAINING' THEN
        DELETE FROM user_roles
        WHERE cid = NEW.cid
        AND rolename LIKE 'ATC_%'
		AND rolename != 'ATC_TRAINING';
		
		UPDATE users
        SET highest_controller_rating = CASE
            WHEN NEW.rolename = 'ATC_S1' THEN 2
            WHEN NEW.rolename = 'ATC_S2' THEN 3
            WHEN NEW.rolename = 'ATC_S3' THEN 4
            WHEN NEW.rolename = 'ATC_C1' THEN 5
            WHEN NEW.rolename = 'ATC_C3' THEN 7
            WHEN NEW.rolename = 'ATC_I1' THEN 8
            WHEN NEW.rolename = 'ATC_I3' THEN 10
            ELSE highest_controller_rating
        END
        WHERE cid = NEW.cid;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_enforce_single_atc_role
BEFORE INSERT ON user_roles
FOR EACH ROW
EXECUTE FUNCTION enforce_single_atc_role();

CREATE OR REPLACE VIEW get_users_event_applications
AS
SELECT uea.application_id AS uea_application_id, uea.user_cid AS uea_user_cid, u.first_name AS u_first_name, u.last_name AS u_last_name,
ep.position_id AS ep_position_id, uea.status AS uea_status, uea.applied_at AS uea_applied_at, uea.is_added_by_staff AS uea_is_added_by_staff,
uea.added_by_staff_cid AS uea_added_by_staff_cid, ua.first_name AS ua_first_name, ua.last_name AS ua_last_name,
e.event_id AS e_event_id, e.name AS e_name, e.type AS e_type, e.image_url AS e_image_url, e.start_at AS e_start_at, e.end_at AS e_end_at, e.created_at AS e_created_at
FROM user_event_applications uea
JOIN users u ON uea.user_cid = u.cid
JOIN slots s ON uea.slot_id = s.slot_id
JOIN event_positions ep ON s.event_position_id = ep.event_position_id
JOIN events e ON ep.event_id = e.event_id
LEFT JOIN users ua ON uea.added_by_staff_cid = ua.cid;

CREATE MATERIALIZED VIEW weekly_controller_report AS
WITH weekly_sessions AS (
    SELECT
        cid,
        position,
        session_started,
        session_ended,
        COALESCE(ROUND(EXTRACT(EPOCH FROM (session_ended - session_started))), 0) AS session_duration_seconds
    FROM
        controllers_online_log
    WHERE
        session_started >= date_trunc('week', NOW()) - INTERVAL '1 week'
        AND session_started < date_trunc('week', NOW()) + INTERVAL '4 hour'
),
aggregated_data AS (
    SELECT
        cid,
        position,
        SUM(session_duration_seconds)::BIGINT AS total_duration_seconds
    FROM
        weekly_sessions
    GROUP BY
        cid, position
),
formatted_data AS (
    SELECT
        a.cid AS cid,
        a.position AS position,
        a.total_duration_seconds AS time_in_seconds
    FROM
        aggregated_data a
    LEFT JOIN users u ON a.cid = u.cid
    ORDER BY
        a.total_duration_seconds DESC
)
SELECT
    cid,
    position,
    time_in_seconds
FROM
    formatted_data;
	
----------------------------------------------------------------------------------------------------------------

CREATE TABLE airports (
    airport_id varchar(10) not null primary key,
    name varchar(255) not null,
    location varchar(255),
	image_path text not null
);

CREATE TABLE airport_details (
	airport_detail_id varchar(100) not null primary key default gen_random_uuid(),
	airport_id varchar(10) not null references airports(airport_id) on delete cascade,
	icao varchar(10),
	iata varchar(10),
	is_major boolean not null default false,
	elevation int,
	transition_altitude int,
	transition_level int,
	msa int,
	latitude numeric,
	longitude numeric
);

CREATE OR REPLACE FUNCTION capitalize_airport_id()
RETURNS TRIGGER AS $$
BEGIN
    NEW.airport_id := UPPER(NEW.airport_id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_capitalize_airport_id
BEFORE INSERT OR UPDATE ON airports
FOR EACH ROW
EXECUTE FUNCTION capitalize_airport_id();

CREATE TABLE runways (
    runway_id varchar(100) not null primary key default gen_random_uuid(),
    airport_id varchar(10) not null references airports(airport_id) on delete cascade,
    name varchar(50) not null,
    description TEXT,
	is_automatically_created boolean not null default false
);

CREATE TABLE map_categories (
    map_category_id varchar(100) not null primary key default gen_random_uuid(),
    name varchar(100) not null,
    description text
);

CREATE TABLE map_files (
    map_file_id varchar(100) not null primary key default gen_random_uuid(),
    airport_id varchar(10) not null references airports(airport_id) on delete cascade,
    runway_id varchar(100) references runways(runway_id) on delete set null,
    category_id varchar(100) references map_categories(map_category_id) on delete set null,
    name varchar(255) not null,
    file_path text not null,
	description text,
	order_number int not null,
	created_at timestamp not null default NOW(),
    updated_at timestamp not null default NOW()
);

CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_updated_at
BEFORE UPDATE ON map_files
FOR EACH ROW
EXECUTE FUNCTION update_updated_at();

CREATE OR REPLACE FUNCTION create_opposite_runway()
RETURNS TRIGGER AS $$
DECLARE
    opposite_name varchar(50);
BEGIN
    IF NEW.is_automatically_created THEN
        RETURN NEW;
    END IF;
	
    opposite_name := LPAD(((CAST(SUBSTRING(NEW.name, 1, 2) AS INT) + 18) % 36)::TEXT, 2, '0');
	
    INSERT INTO runways (airport_id, name, description, is_automatically_created)
    VALUES (NEW.airport_id, opposite_name, 'Automatically created opposite runway', TRUE);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_create_opposite_runway
AFTER INSERT ON runways
FOR EACH ROW
EXECUTE FUNCTION create_opposite_runway();

CREATE OR REPLACE FUNCTION delete_opposite_runway()
RETURNS TRIGGER AS $$
DECLARE
    opposite_name VARCHAR(50);
BEGIN
    IF OLD.name ~ '^[0-9]{2}$' THEN
        opposite_name := LPAD((CAST(SUBSTRING(OLD.name, 1, 2) AS INT) + 18) % 36, 2, '0');

        DELETE FROM runways
        WHERE airport_id = OLD.airport_id AND name = opposite_name;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_delete_opposite_runway
AFTER DELETE ON runways
FOR EACH ROW
EXECUTE FUNCTION delete_opposite_runway();
