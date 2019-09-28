create database if not exists epamproject;

create table if not exists account_type
(
    account_type_ID int auto_increment
        primary key,
    acc_type_value varchar(100) null,
    constraint account_type_acc_type_value_uindex
        unique (acc_type_value),
    constraint account_type_account_type_value_uindex
        unique (acc_type_value)
);

create table if not exists user_role
(
    user_role_ID int auto_increment
        primary key,
    user_role_value varchar(50) not null,
    constraint user_role_user_role_value_uindex
        unique (user_role_value)
);

create table if not exists user
(
    user_id               int auto_increment
        primary key,
    user_firstname        varchar(100)         null,
    user_lastname         varchar(100)         null,
    user_login_email      varchar(100)         null,
    user_password         varchar(250)         null,
    user_role             varchar(50)          null,
    user_credit_acc       tinyint(1) default 0 null,
    credit_request_status tinyint(1) default 0 null,
    salt                  varchar(20)          null,
    constraint user_user_email_login_uindex
        unique (user_login_email),
    constraint user_user_login_emali_uindex
        unique (user_login_email),
    constraint user_user_role_user_role_value_fk
        foreign key (user_role) references user_role (user_role_value)
);

create table if not exists account
(
    account_ID       int auto_increment
        primary key,
    account_owner    varchar(100)   null,
    account_number   int            null,
    account_type     varchar(100)   null,
    current_balance  decimal(20, 2) null,
    interest_rate    decimal(3)     null,
    credit_limit     decimal(20, 2) null,
    account_validity timestamp      null,
    deposit          decimal(20, 2) null,
    constraint account_account_number_uindex
        unique (account_number),
    constraint account_account_type_acc_type_value_fk
        foreign key (account_type) references account_type (acc_type_value),
    constraint account_user_user_login_emali_fk
        foreign key (account_owner) references user (user_login_email)
);

create table if not exists credit_opening_request
(
    request_ID            int auto_increment
        primary key,
    user_email_login      varchar(100)   null,
    user_total_balance    decimal(20, 2) null,
    expected_credit_limit decimal(20, 2) null,
    date_of_end_credit    timestamp      null,
    constraint credit_opening_request_user_user_login_emali_fk
        foreign key (user_email_login) references user (user_login_email)
);

create table if not exists payment_history
(
    transaction_ID      int auto_increment
        primary key,
    account_number      int            null,
    transaction_amount  decimal(20, 2) null,
    current_balance     decimal(20, 2) null,
    date_of_transaction timestamp      null,
    notification        varchar(500)   null,
    constraint payment_history_account_account_number_fk
        foreign key (account_number) references account (account_number)
);


INSERT INTO `epamproject`.`user_role` (`user_role_value`)
VALUES ('user');
INSERT INTO `epamproject`.`user_role` (`user_role_value`)
VALUES ('admin');

INSERT INTO `epamproject`.`account_type` (`acc_type_value`)
VALUES ('credit');
INSERT INTO `epamproject`.`account_type` (`acc_type_value`)
VALUES ('deposit');

INSERT INTO `epamproject`.`user` (`user_firstname`, `user_lastname`, `user_login_email`,
                                  `user_password`, `user_role`, `user_credit_acc`,
                                  `credit_request_status`, `salt`)
VALUES ('MAIN', 'ADMIN', 'admin',
        '[71, 38, 96, 122, 56, -77, 103, 93, 53, 119, 85, 85, -126, -75, 117, -79]',
        'admin', DEFAULT, DEFAULT, 'somesaltsomesalt');

