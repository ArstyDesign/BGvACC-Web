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

CREATE TABLE roles(
	rolename varchar(70) not null primary key,
	description varchar(200)
);

INSERT INTO roles VALUES ('SYS_ADMIN', 'System administrator');
INSERT INTO roles VALUES ('BGVACC_STAFF', 'BGvACC staff');
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
