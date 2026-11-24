insert into experience (title, sport, city, level, price_eur) values
 ('Initiation escalade en salle', 'escalade', 'Lyon', 'BEGINNER', 25),
 ('Sortie trail dans les Monts d''Or', 'trail', 'Lyon', 'INTERMEDIATE', 15),
 ('Stage de surf à Hossegor', 'surf', 'Hossegor', 'BEGINNER', 120),
 ('Match de padel entre amis', 'padel', 'Lyon', 'INTERMEDIATE', 12),
 ('Via ferrata en Chartreuse', 'escalade', 'Grenoble', 'ADVANCED', 60);
insert into experience_tag (experience_id, tag) values
 (1,'indoor'),(1,'groupe'),(1,'découverte'),
 (2,'outdoor'),(2,'nature'),(2,'endurance'),
 (3,'outdoor'),(3,'océan'),(3,'découverte'),
 (4,'indoor'),(4,'groupe'),(4,'raquette'),
 (5,'outdoor'),(5,'montagne'),(5,'adrénaline');
