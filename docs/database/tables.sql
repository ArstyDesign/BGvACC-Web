CREATE TABLE roles(
	rolename varchar(70) not null primary key,
	description varchar(200)
);

INSERT INTO roles VALUES ('SYS_ADMIN', 'System administrator');