CREATE TABLE users1 (
	user_id int not null auto_increment,
	email_address varchar(45) not null,
	password varchar(45) not null,
    full_name varchar(45) not null,
	PRIMARY KEY(user_id)
);

CREATE TABLE emails (
	email_id int not null auto_increment,
    sender varchar(45) not null,
	receiver varchar(45) not null,
    subject varchar(45),
    email_text text(1000000000),
    date date not null,
    time time not null,
    PRIMARY KEY(email_id)
);

CREATE TABLE contacts (
	contact_id int not null auto_increment,
	full_name varchar(45) not null,
	email_address varchar(45) not null,
	PRIMARY KEY(contact_id)
);

SELECT * FROM users1;

SELECT * FROM emails;

SELECT * FROM contacts;



