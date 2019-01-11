create table genres
(
	id bigserial not null
		constraint genre_pkey
			primary key,
	name varchar(30) not null
);

create unique index genre_name_uindex
	on genres (name);

alter sequence if exists genres_id_seq restart with 13;

create table "user"
(
	id bigserial not null
		constraint user_pkey
			primary key,
	surname varchar(40),
	givenname varchar(30),
	login varchar(20) not null,
	password varchar(120) not null
);

create unique index user_login_uindex
	on "user" (login);
	
alter sequence if exists user_id_seq restart with 1;

create table persons
(
	id bigserial not null
		constraint persons_pkey
			primary key,
	surname varchar(60) not null,
	givenname varchar(40),
	image_path varchar(80),
	birthday date
);

alter sequence if exists persons_id_seq restart with 25;

create table films
(
	id bigserial not null
		constraint films_pkey
			primary key,
	title varchar(50),
	rating numeric(2,1),
	image_path varchar(120),
	summary text,
	film_director bigint
		constraint films_persons_id_fk
			references persons,
	release_date date
);

alter sequence if exists films_id_seq restart with 7;

create table review
(
	id bigserial not null
		constraint review_pkey
			primary key,
	film_id bigint not null
		constraint review_films_id_fk
			references films,
	user_id bigint not null
		constraint review_user_id_fk
			references "user",
	article text,
	datte timestamp default now() not null
);

alter sequence if exists review_id_seq restart with 1;


create table play
(
	id bigserial not null
	    constraint play_pkey
	        primary key,
	film_id bigint not null
		constraint play_films_id_fk
			references films,
	person_id bigint not null
		constraint play___fk_person
			references persons
				on update cascade on delete cascade,
	rank integer not null,
	name varchar(50) not null
);

alter sequence if exists play_id_seq restart with 1;

create table film_genre
(
	genre_id bigint not null
		constraint film_genre_genre_id_fk
			references genres,
	film_id integer not null
		constraint film_genre_films_id_fk
			references films,
	constraint film_genre_pk
		primary key (film_id, genre_id)
);






INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (2, 'Galabru', 'Michel', 'p0002.jpg', '1922-10-27');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (24, 'Zwick', 'Edward', 'p0020.jpg', '1952-10-08');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (21, 'Marshall', 'Garry', 'p0098.jpg', '1934-11-13');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (23, 'Spielberg', 'Steven', 'p0019.jpg', '1946-12-18');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (20, 'Lucas', 'Georges', 'p0045.jpg', '1944-05-14');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (9, 'Bellamy', 'Ralph', 'p0009.jpg', '1904-06-17');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (19, 'Tavernier', 'Bertrand', 'p0110.jpg', '1941-04-25');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (15, 'Wallace Stone', 'Dee', 'p0015.jpg', '1948-12-14');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (16, 'Heston', 'Charlton', 'p0016.jpg', '1923-10-04');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (7, 'Gere', 'Richard', 'p0007.jpg', '1949-08-31');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (6, 'Fisher', 'Carrie', 'p0006.jpg', '1956-10-21');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (1, 'Noiret', 'Phillipe', 'p0001.jpg', '1930-10-01');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (13, 'Thomas', 'Henry', 'p0013.jpg', '1971-09-09');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (10, 'Hopkins', 'Anthony', 'p0010.jpg', '1937-12-31');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (3, 'Huppert', 'Isabelle', 'p0003.jpg', '1953-03-16');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (17, 'Boyd', 'Stephen', 'p0017.jpg', '1931-07-04');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (12, 'Quinn', 'Aidan', 'p0012.jpg', '1959-03-08');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (18, 'Hawkins', 'Jack', 'p0018.jpg', '1910-09-14');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (22, 'Wyler', 'William', 'p0044.jpg', '1902-07-01');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (11, 'Pitt', 'William Bradley', 'p0011.jpg', '1963-12-18');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (14, 'Barrymore', 'Drew Bythe', 'p0014.jpg', '1975-02-22');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (8, 'Roberts', 'Julia', 'p0008.jpg', '1967-10-28');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (5, 'Ford', 'Harrison', 'p0005.jpg', '1942-07-13');
INSERT INTO persons (id, surname, givenname, image_path, birthday) VALUES (4, 'Hamill', 'Mark', 'p0004.jpg', '1951-09-25');

INSERT INTO genres (id, name) VALUES (1, 'Comédie');
INSERT INTO genres (id, name) VALUES (2, 'Policier');
INSERT INTO genres (id, name) VALUES (3, 'Drame');
INSERT INTO genres (id, name) VALUES (4, 'Romance');
INSERT INTO genres (id, name) VALUES (5, 'Science Fiction');
INSERT INTO genres (id, name) VALUES (6, 'Fantastique');
INSERT INTO genres (id, name) VALUES (7, 'Thriller');
INSERT INTO genres (id, name) VALUES (8, 'Aventure');
INSERT INTO genres (id, name) VALUES (9, 'Histoire');
INSERT INTO genres (id, name) VALUES (10, 'Famille');
INSERT INTO genres (id, name) VALUES (11, 'Action');
INSERT INTO genres (id, name) VALUES (12, 'Guerre');

INSERT INTO films (id, title, rating, image_path, summary, film_director, release_date) VALUES (4, 'BEN-HUR', 4.0, 'f0006.jpg', 'Judas Ben-Hur, prince de Judée, retrouve son ami d''enfance Messala, venu prendre la tête de la garnison de Jérusalem. Mais leur amitié ne peut résister à leurs caractères différents. Alors qu''une pierre tombe du balcon de la maison familiale de Ben-Hur, manquant de tuer le gouverneur qui paradait plus bas, Messala trahit son ami qu''il sait innocent en l''envoyant aux galères et en jetant en prison sa mère et sa sœur. Ben-Hur jure alors de reconquérir sa liberté et prépare sa vengeance.', 22, '1959-11-18');
INSERT INTO films (id, title, rating, image_path, summary, film_director, release_date) VALUES (5, 'E.T. l''extra-terrestre', 4.2, 'f0005.jpg', 'Une soucoupe volante atterrit en pleine nuit près de Los Angeles. Quelques extraterrestres, envoyés sur Terre en mission d''exploration botanique, sortent de l''engin, mais un des leurs s''aventure au-delà de la clairière où se trouve la navette. Celui-ci se dirige alors vers la ville. C''est sa première découverte de la civilisation humaine. Bientôt traquée par des militaires et abandonnée par les siens, cette petite créature apeurée se nommant E.T. se réfugie dans une résidence de banlieue. Elliot, un garçon de dix ans, le découvre et lui construit un abri dans son armoire. Rapprochés par un échange télépathique, les deux êtres ne tardent pas à devenir amis. Aidé par sa soeur Gertie et son frère aîné Michael, Elliot va alors tenter de garder la présence d''E.T. secrète.', 23, '1982-05-26');
INSERT INTO films (id, title, rating, image_path, summary, film_director, release_date) VALUES (3, 'Pretty woman', 3.5, 'f0003.jpg', 'Edward Lewis, homme d''affaires performant, rencontre par hasard Vivian Ward, beaute fatale qui arpente chaque nuit les trottoirs d''Hollywood Boulevard. La jeune femme ne fera qu''une bouchee du brillant PDG.', 21, '1990-03-23');
INSERT INTO films (id, title, rating, image_path, summary, film_director, release_date) VALUES (1, 'Le juge et l''assassin', 3.8, 'f0001.jpg', 'Fin du XIXème, Joseph Bouvier est révoqué de l''armée à cause de ses excès de violence. Suite à ce renvoi, l''homme s''attaque à sa fiancée et tente de se suicider, en vain. Après un séjour en hôpital psychiatrique, Joseph ressort de cet endroit encore plus enragé et décide de se venger sur toutes les personnes qui croiseront son chemin en Ardèche. Non loin de là, le juge Rousseau, passionné par l''affaire, prend part à l''investigation et se met sur les traces de Bouvier. Bien décidé à le mettre sous les verrous, c''est le début d''une chasse à l''homme...', 19, '1976-03-10');
INSERT INTO films (id, title, rating, image_path, summary, film_director, release_date) VALUES (2, 'La guerre des étoiles', 4.4, 'f0002.jpg', 'Il y a bien longtemps, dans une galaxie très lointaine... La guerre civile fait rage entre l''Empire galactique et l''Alliance rebelle. Capturée par les troupes de choc de l''Empereur menées par le sombre et impitoyable Dark Vador, la princesse Leia Organa dissimule les plans de l''Etoile Noire, une station spatiale invulnérable, à son droïde R2-D2 avec pour mission de les remettre au Jedi Obi-Wan Kenobi. Accompagné de son fidèle compagnon, le droïde de protocole C-3PO, R2-D2 s''échoue sur la planète Tatooine et termine sa quête chez le jeune Luke Skywalker. Rêvant de devenir pilote mais confiné aux travaux de la ferme, ce dernier se lance à la recherche de ce mystérieux Obi-Wan Kenobi, devenu ermite au coeur des montagnes désertiques de Tatooine...', 20, '1977-05-25');
INSERT INTO films (id, title, rating, image_path, summary, film_director, release_date) VALUES (6, 'Légendes d''automne', 3.9, 'f0004.jpg', 'Au cœur des contrées sauvages du Montana, trois frères, Alfred, Tristan et Samuel sont élevés par leur père, le colonel William Ludlow et ses amis indiens. Un jour, Samuel, le plus fragile, présente sa ravissante fiancée, Susannah à sa famille. Les trois jeunes hommes s’engagent dans la première Guerre Mondiale et Samuel décède sur le champ de bataille. A leur retour, Tristan et Alfred se battent pour conquérir le cœur de la belle veuve.', 24, '1994-12-23');

INSERT INTO film_genre (genre_id, film_id) VALUES (8, 4);
INSERT INTO film_genre (genre_id, film_id) VALUES (3, 4);
INSERT INTO film_genre (genre_id, film_id) VALUES (9, 4);
INSERT INTO film_genre (genre_id, film_id) VALUES (10, 5);
INSERT INTO film_genre (genre_id, film_id) VALUES (5, 5);
INSERT INTO film_genre (genre_id, film_id) VALUES (1, 3);
INSERT INTO film_genre (genre_id, film_id) VALUES (4, 3);
INSERT INTO film_genre (genre_id, film_id) VALUES (2, 1);
INSERT INTO film_genre (genre_id, film_id) VALUES (3, 1);
INSERT INTO film_genre (genre_id, film_id) VALUES (9, 1);
INSERT INTO film_genre (genre_id, film_id) VALUES (8, 2);
INSERT INTO film_genre (genre_id, film_id) VALUES (11, 2);
INSERT INTO film_genre (genre_id, film_id) VALUES (6, 2);
INSERT INTO film_genre (genre_id, film_id) VALUES (3, 6);
INSERT INTO film_genre (genre_id, film_id) VALUES (4, 6);
INSERT INTO film_genre (genre_id, film_id) VALUES (12, 6);

INSERT INTO play (film_id, person_id, rank, name) VALUES (1, 1, 1, 'Le juge Rousseau');
INSERT INTO play (film_id, person_id, rank, name) VALUES (1, 2, 2, 'Joseph Bouvier');
INSERT INTO play (film_id, person_id, rank, name) VALUES (1, 3, 3, 'Rose');
INSERT INTO play (film_id, person_id, rank, name) VALUES (2, 4, 1, 'Luke Skywalker');
INSERT INTO play (film_id, person_id, rank, name) VALUES (2, 5, 2, 'Han Solo');
INSERT INTO play (film_id, person_id, rank, name) VALUES (2, 6, 3, 'Princesse Leia Organa');
INSERT INTO play (film_id, person_id, rank, name) VALUES (3, 7, 1, 'Edward Lewis');
INSERT INTO play (film_id, person_id, rank, name) VALUES (3, 8, 2, 'Vivian Ward');
INSERT INTO play (film_id, person_id, rank, name) VALUES (3, 9, 3, 'James');
INSERT INTO play (film_id, person_id, rank, name) VALUES (6, 10, 1, 'Colonel William Ludlow');
INSERT INTO play (film_id, person_id, rank, name) VALUES (6, 11, 2, 'Tristan Ludlow');
INSERT INTO play (film_id, person_id, rank, name) VALUES (6, 12, 3, 'Alfred Ludlow');
INSERT INTO play (film_id, person_id, rank, name) VALUES (6, 13, 4, 'Samuel Ludlow');
INSERT INTO play (film_id, person_id, rank, name) VALUES (5, 13, 1, 'Eliot');
INSERT INTO play (film_id, person_id, rank, name) VALUES (5, 14, 2, 'Gertie');
INSERT INTO play (film_id, person_id, rank, name) VALUES (5, 15, 3, 'Mary');
INSERT INTO play (film_id, person_id, rank, name) VALUES (4, 16, 1, 'Judas Ben-Hur');
INSERT INTO play (film_id, person_id, rank, name) VALUES (4, 17, 2, 'Messala');
INSERT INTO play (film_id, person_id, rank, name) VALUES (4, 18, 3, 'Quintus Arrius');