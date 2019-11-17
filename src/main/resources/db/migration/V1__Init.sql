create table hibernate_sequence
(
	next_val bigint null
);

create table roles
(
	id bigint not null
		primary key,
	name varchar(255) null
);

create table status_
(
	id bigint not null
		primary key,
	name varchar(255) null
);

create table users
(
	id bigint not null
		primary key,
	created_at datetime not null,
	updated_at datetime not null,
	name varchar(255) null,
	password varchar(255) null,
	username varchar(255) null,
	constraint UKr43af9ap4edm43mmtq01oddj6
		unique (username)
);

create table debts
(
	id bigint not null
		primary key,
	created_at datetime not null,
	updated_at datetime not null,
	balance float null,
	borrower_id bigint null,
	creditor_id bigint null,
	constraint FKnhip82fx3rfk8driwnyhto277
		foreign key (borrower_id) references users (id),
	constraint FKsr7ggg84v3isgrrjx076opj78
		foreign key (creditor_id) references users (id)
);

create table groups_
(
	id bigint not null
		primary key,
	title varchar(255) null,
	owner_id bigint null,
	constraint FKmm3rm9xc0qu00qr4iq6mf2wqp
		foreign key (owner_id) references users (id)
);

create table groups_members
(
	group_id bigint not null,
	user_id bigint not null,
	constraint UK_pynnby7lrxo096mdm1t0okmdo
		unique (user_id),
	constraint FK4cc1uaype9m8649rrnlxrnqoc
		foreign key (user_id) references users (id),
	constraint FK5de7dqov8agkuq98cmwg811e0
		foreign key (group_id) references groups_ (id)
);

create table orders_
(
	id bigint not null
		primary key,
	amount float null,
	receiver_id bigint null,
	status_id bigint null,
	constraint FK7u3wre46942b7ytyrpjsxoxk2
		foreign key (status_id) references status_ (id),
	constraint FKi5u9gerkjrj40yqna80wdapfp
		foreign key (receiver_id) references users (id)
);

create table requests_debt
(
	id bigint not null
		primary key,
	created_at datetime not null,
	updated_at datetime not null,
	comment varchar(255) null,
	sender_id bigint null,
	status_id bigint null,
	constraint FKk7qp014evvnhn5th5ypfqokim
		foreign key (sender_id) references users (id),
	constraint FKohosbf6anjhgpbvilbml552fs
		foreign key (status_id) references status_ (id)
);

create table requests_debt_orders
(
	request_id bigint not null,
	order_id bigint not null,
	constraint UK_5jgt5yupdauprr69nm5lssxsc
		unique (order_id),
	constraint FK3e39a0rill1njpll7768xr2l9
		foreign key (request_id) references requests_debt (id),
	constraint FK8jlkooq4nvdmof36kpllshmi1
		foreign key (order_id) references orders_ (id)
);

create table requests_friend
(
	id bigint not null
		primary key,
	created_at datetime not null,
	updated_at datetime not null,
	comment varchar(255) null,
	sender_id bigint null,
	status_id bigint null,
	receiver_id bigint null,
	constraint FK4rn7u93t5yfq7w2pynq928jxv
		foreign key (sender_id) references users (id),
	constraint FKhqqsud20b9bbhvjptxlef8vqc
		foreign key (status_id) references status_ (id),
	constraint FKllxnm2r5ibhy070kg94vdxf29
		foreign key (receiver_id) references users (id)
);

create table requests_repayment
(
	id bigint not null
		primary key,
	created_at datetime not null,
	updated_at datetime not null,
	comment varchar(255) null,
	sender_id bigint null,
	status_id bigint null,
	order_id bigint null,
	constraint FK7aatyklhugrar639b06ld8y9h
		foreign key (order_id) references orders_ (id),
	constraint FKbgfmledm3sgcexli9djepfdw2
		foreign key (sender_id) references users (id),
	constraint FKqijbv5p2eomf7msnbb3tfncsl
		foreign key (status_id) references status_ (id)
);

create table user_blacklist
(
	user_id bigint not null,
	user_black_id bigint not null,
	constraint FKc718a3p0qxcr3k7xakl2ifj9c
		foreign key (user_id) references users (id),
	constraint FKtlefjifyhg0ykrrye6lq85s4b
		foreign key (user_black_id) references users (id)
);

create table user_friends
(
	user_id bigint not null,
	user_friend_id bigint not null,
	constraint FKbkxv9u4m4o1mw3hy77b9p5o4u
		foreign key (user_friend_id) references users (id),
	constraint FKk08ugelrh9cea1oew3hgxryw2
		foreign key (user_id) references users (id)
);

create table user_roles
(
	user_id bigint not null,
	role_id bigint not null,
	primary key (user_id, role_id),
	constraint FKh8ciramu9cc9q3qcqiv4ue8a6
		foreign key (role_id) references roles (id),
	constraint FKhfh9dx7w3ubf1co1vdev94g3f
		foreign key (user_id) references users (id)
);

