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
	edited_on timestamp
);

INSERT INTO users VALUES ('1720051', 'aarshinkov9705@gmail.com', 'aarshinkov9705@gmail.com', '$2a$12$NtFfJNxLCgGtuopIsyt0g.AHjSD0lcBnMMMqBExtXmqUm4YwEK9sO', 'Atanas', 'Arshinkov', true, null, NOW(), null);

INSERT INTO users VALUES ('1773453', 'kristiyan.hristov@gmail.com', 'kristiyan.hristov@gmail.com', '$2a$12$NtFfJNxLCgGtuopIsyt0g.AHjSD0lcBnMMMqBExtXmqUm4YwEK9sO', 'Kristiyan', 'Hristov', true, null, NOW(), null);

INSERT INTO users VALUES ('1008143', 'svetlin.nikolov@gmail.com', 'svetlin.nikolov@gmail.com', '$2a$12$NtFfJNxLCgGtuopIsyt0g.AHjSD0lcBnMMMqBExtXmqUm4YwEK9sO', 'Svetlin', 'Nikolov', true, null, NOW(), null);

CREATE TABLE roles(
	rolename varchar(70) not null primary key,
	description varchar(200)
);

INSERT INTO roles VALUES ('SYS_ADMIN', 'System administrator');
INSERT INTO roles VALUES ('STAFF', 'Staff role');
INSERT INTO roles VALUES ('STAFF_DIRECTOR', 'Staff director'); 
INSERT INTO roles VALUES ('STAFF_EVENTS', 'Staff events'); 
INSERT INTO roles VALUES ('STAFF_TRAINING', 'Staff training');
INSERT INTO roles VALUES ('ATC', 'ATC');
INSERT INTO roles VALUES ('ATC_S1', 'ATC rating S1');
INSERT INTO roles VALUES ('ATC_S2', 'ATC rating S2');
INSERT INTO roles VALUES ('ATC_S3', 'ATC rating S3');
INSERT INTO roles VALUES ('ATC_C1', 'ATC rating C1');
INSERT INTO roles VALUES ('ATC_C3', 'ATC rating C3');
INSERT INTO roles VALUES ('ATC_I1', 'ATC rating I1');
INSERT INTO roles VALUES ('ATC_I3', 'ATC rating I3');

CREATE TABLE user_roles(
	user_role_id varchar(100) not null primary key default gen_random_uuid(),
	cid varchar(30) not null references users(cid) ON DELETE CASCADE,
	rolename varchar(50) not null references roles(rolename) ON DELETE CASCADE,
	created_on timestamp not null default NOW(),
	UNIQUE (cid, rolename)
);

INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'SYS_ADMIN');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'STAFF');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'STAFF_EVENTS');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'STAFF_TRAINING');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'ATC');
INSERT INTO user_roles (cid, rolename) VALUES ('1720051', 'ATC_S3');
INSERT INTO user_roles (cid, rolename) VALUES ('1773453', 'ATC');
INSERT INTO user_roles (cid, rolename) VALUES ('1773453', 'ATC_S2');
INSERT INTO user_roles (cid, rolename) VALUES ('1008143', 'STAFF');
INSERT INTO user_roles (cid, rolename) VALUES ('1008143', 'STAFF_EVENTS');
INSERT INTO user_roles (cid, rolename) VALUES ('1008143', 'STAFF_TRAINING');
INSERT INTO user_roles (cid, rolename) VALUES ('1008143', 'ATC');
INSERT INTO user_roles (cid, rolename) VALUES ('1008143', 'ATC_C1');

CREATE TABLE event_types (
    type varchar(10) primary key
);

INSERT INTO event_types (type) VALUES ('event'), ('cpt'), ('vasops');

CREATE TABLE events (
    event_id int primary key not null,
    name varchar(255) not null,
    type varchar(10) not null references event_types(type),
    description text,
    short_description text,
    image_url varchar(255),
    start_at timestamptz not null,
    end_at timestamptz not null,
    created_at timestamptz not null,
    updated_at timestamptz
);