--role
INSERT INTO role (`role_name`) VALUES (1,'HR');
INSERT INTO role (`role_name`) VALUES (2,'CANDIDATE');
INSERT INTO role (`role_name`) VALUES (3,'EXPERT');
INSERT INTO role (`role_name`) VALUES (4,'ADMIN');

--expert

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Pham Viet Thuan Thien',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','thien.pham@aiesec.net','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Truong Van Thanh',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','vanthanh@gmail.com','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Le Chi Bao',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','chibao@gmail.com','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Pham Viet Huu Thang',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','huuthang@gmail.com','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Nguyen Van Thanh',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','vanthanh@gmail.com','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Lam Van Tien',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','vantien@gmail.com','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Ho Chi Tai',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Saigon','chitai@gmail.com','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,3,NULL,'MoMo','Manager',NULL,2,NULL,NULL,150,1,NULL);

--candidate

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Pham Thuan Thien',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','phamvietthuanthien@gmail.com','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Linh Manh Khoi',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','manhkhoi@gmail.com','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Le Quoc Viet',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','quocviet@gmail.com','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Pham Huu Thang',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','huutam@gmail.com','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Nguyen Tien Thanh',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','tienthanh@gmail.com','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Nguyen Khuc Thuy An',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','thuyan@gmail.com','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL);

--hr
INSERT INTO users (`name`,`account_balance`,`address`,`avatar`,`country`,`email`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`role_id`,`about`,`company`,`job_title`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`)
VALUES ('Le Van Thanh',1,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','Ha Noi','thienpvtse160542@fpt.edu.vn','/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

--evalue
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (1,'Short Bullet Points', 'Each bullet point should be a full line length. ', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (2,'Punctuated Bullet Points', 'Capitalize the first letter and end with a period for each bullet point.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (3,'Number of Bullet Points', 'Include 3-6 bullet points for each experience. ', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (4,'Personal Pronoun', 'Avoid using personal pronoun when you describe your experience.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (5,'Filler Words', 'Avoid using filler words when you describe your experience.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (6,'Quantified Bullet Points', 'Add metrics to each bullet point when possible. ', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (7,'Spelling and grammar', 'Be careful in using appropriate words.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (8,'Passive voice', 'Using passive voice in your resume can make it harder for employers to identify your specific contributions.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (9,'Your location number is missing', 'Add a valid location to be contacted easily.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (10,'Your date number is missing', 'Add valid dates for achievements to be validated.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (11,'Your Phone number is missing', 'Add a valid phone number to be contacted easily.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (12,'Your Linkedin url is', 'Your Linkedin URL should be abbreviated like in/charles-bloomberg.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (13,'Your resume has', 'Your resume should contain between 400-800 words.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (14,'Your Summary is missing', 'Please do not leave summary blank.', 5.0, 10.0,5,5);
INSERT INTO evaluate (id, title, description, score, max_score, condition1, condition2)
VALUES (15,'Your resume is not tailored for a specific job description', 'Add specific keywords from a targeted job description to optimize your resume. ', 5.0, 10.0,5,5);



INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Pham Viet Thuan Thien','Dev at VNG123',301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','111','Saigon','thienpvtse160542@fpt.edu.vn','Dev','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,1,1,NULL,150,0,NULL,1,NULL,NULL,NULL,NULL,NULL,1,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Pham Viet Thien','1',29401,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','MoMo','Saigon','thien.pham@aiesec.net','Manager','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,NULL,2,NULL,150,0,NULL,3,NULL,NULL,NULL,NULL,NULL,28,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Le Chi Bao',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','MoMo','Saigon','damanhbangthpt@gmail.com','Manager','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,NULL,2,NULL,150,0,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Pham Viet Huu Thang',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','MoMo','Saigon','huuthang@gmail.com','Manager','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,NULL,2,NULL,150,0,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Nguyen Van Thanh',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','MoMo','Saigon','vanthanh@gmail.com','Manager','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,NULL,2,NULL,150,0,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Lam Van Tien',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','MoMo','Saigon','vantien@gmail.com','Manager','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,NULL,2,NULL,150,0,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Ho Chi Tai',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c','MoMo','Saigon','chitai@gmail.com','Manager','/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,NULL,2,NULL,150,0,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Pham Viet Thuan Thien',NULL,984600,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Saigon','phamvietthuanthien@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,'0788431898','ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Linh Manh Khoi',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Ha Noi','manhkhoi@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Le Quoc Viet',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Ha Noi','quocviet@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Pham Huu Thang',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Ha Noi','huutam@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Nguyen Tien Thanh',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Ha Noi','tienthanh@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Nguyen Khuc Thuy An',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Ha Noi','thuyan@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Thien',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIiLohYkD4rDr2uBzWkSPV6PSaXUjbM2MvCUXR5x53kan0C=s96-c',NULL,'Ha Noi','hukhho1@gmail.com',NULL,'/thien-pham-august/',NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'des','Thu duc','https://firebasestorage.googleapis.com/v0/b/cvbuilder-dc116.appspot.com/o/3e1bb2d5-0deb-4b00-9b66-445f1357dd57.png?alt=media&token=3e1bb2d5-0deb-4b00-9b66-445f1357dd57.png','fpt',1,NULL,'2019-01-21');
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Hùng',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocL56Vduh0BuDC3uOy9OYDgZJe0lK2LWIEc-58J29taiMA=s96-c',NULL,NULL,'hukhho111111@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Hùng','About',9982999,NULL,'https://lh3.googleusercontent.com/a/ACg8ocL56Vduh0BuDC3uOy9OYDgZJe0lK2LWIEc-58J29taiMA=s96-c','Google','1','hukhho@gmail.com','Java','1',NULL,'1','0387788906','ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Toan',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocIqVQOpG1k10WR4zceMVRPzbU0AaLrcPVOZZRB4Ihws=s96-c',NULL,NULL,'hukhho11@gmail.com',NULL,NULL,NULL,NULL,'0387788906','ACTIVE',NULL,1,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Admin','amin',29301,NULL,NULL,NULL,NULL,'sdkfjl','fjdskl','skdjf',NULL,NULL,NULL,'ACTIVE',NULL,NULL,NULL,NULL,NULL,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Pham Tran Quang',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocL15DyI44EYORyLM68GbKGeLbbZ_o07hwcfR7PB3Aro=s96-c',NULL,NULL,'minhptqse140653@fpt.edu.vn',NULL,NULL,NULL,NULL,NULL,'ACTIVE',NULL,0,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Méline Ng',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocID-57ruh0xyP4rckDpfwbXSXjb6QQbW733IjpGJ4UEhw=s96-c',NULL,'Hanoi','doanhdoanduoc2021@gmail.com',NULL,'in/melineng',NULL,NULL,'012345678','ACTIVE',NULL,0,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Quang minh',NULL,29301,NULL,'https://lh3.googleusercontent.com/a/ACg8ocLtCeoIr87xpX3qRJ9PsIKRKFfmNCK04pSNRy4Jovw=s96-c',NULL,NULL,'quangminhphamtran@gmail.com',NULL,NULL,NULL,NULL,NULL,'ACTIVE',NULL,0,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO users (`name`,`about`,`account_balance`,`address`,`avatar`,`company`,`country`,`email`,`job_title`,`linkin`,`password`,`personal_website`,`phone`,`status`,`vip`,`publish`,`experience`,`number_review`,`price`,`punish`,`punish_date`,`role_id`,`company_description`,`company_location`,`company_logo`,`company_name`,`subscription`,`cv_id`,`expired_day`)
VALUES ('Chi',NULL,0,NULL,'https://lh3.googleusercontent.com/a/ACg8ocICsT6j75NrikCZzVVeJgmJ1SAWbmeebjhY57SXpKzF=s96-c',NULL,NULL,'vinhnhan1002.sub@gmail.com',NULL,NULL,NULL,NULL,NULL,'ACTIVE',NULL,0,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL);



SELECT * FROM capstone.users;

INSERT INTO `capstone`.`users` (`name`, `account_balance`, `address`, `ban`, `email`, `phone`, `status`, `role_id`)
VALUES ('Admin', '10000', '27/5 le chi tam', 0, 'cvbuilder.ai@gmail.com', '0999999999', 'ACTIVE', 4);

INSERT INTO `capstone`.`users` (`name`, `account_balance`, `address`, `ban`, `email`, `phone`, `status`, `publish`, `role_id`)
VALUES ('Le Van Cuong', '10000', 'Ha Noi', 0, 'cvbuildercandidate@gmail.com', '0888888888', 'ACTIVE', 1, 2);

INSERT INTO `capstone`.`users` (`name`, `account_balance`, `address`, `ban`, `email`, `phone`, `status`, `experience`, `price`, `punish`, `role_id`)
VALUES ('Vo Van Phat', '10000', 'HCM', 0, 'cvbuilderexpert@gmail.com', '0777777777', 'ACTIVE', 1, '0.0', 0, 3);

INSERT INTO `capstone`.`users` (`name`, `account_balance`, `address`, `ban`, `email`, `company_description`, `company_location`, `company_logo`, `company_name`, `expired_day`, `subscription`, `vip`, `role_id`)
VALUES ('Nguyen Tan Hung', '10000', 'Da Nang', 0, 'cvbuilderhr@gmail.com', 'Whether you are working on a new cutting edge app or simply ramping up on new technology, Java documentation has all the information you need to make your project a smashing success. Use the rich set of code samples, tutorials, developer guides, API documentation, and more to quickly develop your prototype and scale it up to a real world application.', '2/7 Hung Tan Phat, HCM', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fpngtree.com%2Ffree-logo-png%2Fcompany-logo&psig=AOvVaw3m92Lk5xCrZij1_0zuHtaM&ust=1702950872858000&source=images&cd=vfe&ved=0CBIQjRxqFwoTCIDT3cHwl4MDFQAAAAAdAAAAABAE', 'Google', '2023-10-10', 0, 1, 1);


INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (1, 1, 'Each bullet point should be a full line length.', 5, 'Short Bullet Points');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (2, 1, 'Capitalize the first letter and end with a period for each bullet point.', 5, 'Punctuated Bullet Points');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (3, 1, 'Include 3-6 bullet points for each experience.', 5, 'Number of Bullet Points');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (4, 1, 'Avoid using personal pronoun when you describe your experience.', 5, 'Personal Pronoun');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (5, 1, 'Avoid using filler words when you describe your experience.', 5, 'Filler Words');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (6, 1, 'Add metrics to each bullet point when possible.', 5, 'Quantified Bullet Points');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (7, 1, 'Be careful in using appropriate words.', 5, 'Spelling and grammar');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (8, 1, 'Using passive voice in your resume can make it harder for employers to identify your specific contributions.', 5, 'Passive voice');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (9, 1, 'Add a valid location to be contacted easily.', 5, 'Your location number is missing');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (10, 1, 'Add valid dates for achievements to be validated.', 5, 'Your date number is missing');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (11, 1, 'Add a valid phone number to be contacted easily.', 5, 'Your Phone number is missing');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (12, 1, 'Your Linkedin URL should be abbreviated like in/charles-bloomberg.', 5, 'Your Linkedin url is');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (13, 1, 'Your resume should contain between 400-800 words.', 5, 'Your resume has');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (14, 1, 'Please do not leave summary blank.', 5, 'Your Summary is missing');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (15, 1, 'Add specific keywords from a targeted job description to optimize your resume.', 5, 'Your resume is not tailored for a specific job description');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (16, 1, 'Use templates to make your cv more professional.', 5, 'Template cv');
INSERT INTO `capstone`.`evaluate` (`id`, `critical`, `description`, `score`, `title`)
VALUES (17, 1, 'Appropriate font size in your CV makes it easy to see.', 5, 'Font size');
