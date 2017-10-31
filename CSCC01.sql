create database if not exists cscc01;
use cscc01;
create table if not exists student (sid varchar(10), 
									fname varchar(20), 
									lname varchar(20), 
                                    utroid varchar(8), 
                                    primary key (sid));
create table if not exists class (cid varchar(9),
								  class varchar(10),
                                  primary key (cid));
create table if not exists assignment (cid varchar(9),
									   aid varchar(1),
                                       assignment varchar(20),
                                       primary key (cid, aid),
                                       foreign key (cid) 
                                       references class (cid));
create table if not exists question (cid varchar(9),
								     aid varchar(1),
                                     qid varchar(2),
                                     question varchar(30),
                                     primary key (cid, aid, qid),
                                     foreign key (cid, aid) references assignment (cid, aid));
create table if not exists student_assignment (sid varchar(10),
										       cid varchar(9),
                                               aid varchar(1),
                                               mark decimal(5,2) default 0.00,
                                               primary key (sid, cid, aid),
                                               foreign key (sid) references student (sid),
                                               foreign key (cid, aid) references assignment (cid, aid));