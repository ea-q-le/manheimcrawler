create database manheim_crawler;

use manheim_crawler;

create table vehicles (
	id 				bigint 			not null auto_increment primary key,
    year 			int 			null,
    make_model 		varchar(128) 	null,
    vin 			varchar(32) 	null,
    odometer 		int 			null,
    auction 		varchar(32) 	null,
    lane 			varchar(16) 	null,
    run_date 		datetime 		null,
    announcements 	varchar(256) 	null,
    available 		bool 			not null,
    found_date 		datetime 		not null
);