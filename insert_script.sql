-- truncate table friendships restart identity cascade;
-- truncate table messages restart identity cascade;
truncate table users restart identity cascade;

insert into users(username, first_name, last_name, password) values 
	('cristi', 'Cristi', 'Ioan', 'cristi'),
	('decebal', 'Decebal', 'Popescu', 'decebal'),
	('maria', 'Maria', 'Ionescu', 'maria'),
	('robert', 'Robert', 'Zamfirescu', 'robert'),
	('daniel', 'Daniel', 'Stefanescu', 'daniel'),
	('bianca', 'Bianca', 'Tatar', 'bianca'),
	('alex', 'Alex', 'Pop', 'alex'),
	('ioana', 'Ioana', 'Enescu', 'ioana');
	
insert into friendships(id_first_user, id_second_user, friends_from, status, sent_by) values
	(1, 2, timestamp '2022-06-01 18:20', 'ACCEPTED', 'FIRST'),
	(2, 3, timestamp '2020-04-20 15:00', 'ACCEPTED', 'SECOND'),
	(3, 4, timestamp '2021-05-11 14:20', 'ACCEPTED', 'FIRST'),
	(3, 5, timestamp '2019-06-15 19:50', 'ACCEPTED', 'FIRST'),
	(4, 5, timestamp '2008-01-01 09:00', 'ACCEPTED', 'SECOND'),
	(6, 7, timestamp '2020-09-10 22:35', 'ACCEPTED', 'FIRST');
	
insert into messages(id_from_user, id_to_user, message_body, sent_date) values
	(1, 2, 'salut!', timestamp '2023-01-01 10:00'),
	(1, 2, 'ce faci!', timestamp '2023-01-01 10:01'),
	(2, 1, 'bine, tu?!', timestamp '2023-01-01 20:00'),
	(3, 4, 'hey', timestamp '2023-01-05 12:00'),
	(3, 4, 'cum mai esti?', timestamp '2023-01-05 12:01');