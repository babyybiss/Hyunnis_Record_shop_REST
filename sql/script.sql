use hyunnisRecordShop;

-- Create User table
CREATE TABLE tbl_User (
                          user_code INTEGER PRIMARY KEY AUTO_INCREMENT COMMENT '회원코드',
                          user_id VARCHAR(255) NOT NULL COMMENT '회원아이디',
                          user_name VARCHAR(255) NOT NULL COMMENT '회원이름',
                          email VARCHAR(255) NOT NULL COMMENT '회원이메일',
                          password VARCHAR(255) NOT NULL COMMENT '회원비밀번호',
                          user_role VARCHAR(255) DEFAULT 'ROLE_USER' NOT NULL COMMENT '유저권한'
);

-- Create Genre table
CREATE TABLE tbl_Genre (
                           genre_code INTEGER PRIMARY KEY AUTO_INCREMENT,
                           genre_name VARCHAR(255) NOT NULL
);

-- Create Artist table
CREATE TABLE tbl_Artist (
                            artist_code INTEGER PRIMARY KEY AUTO_INCREMENT COMMENT '아티스트코드',
                            artist_name VARCHAR(255) NOT NULL COMMENT '아티스트이름',
                            genre_code INTEGER,
                            FOREIGN KEY (genre_code) REFERENCES tbl_Genre(genre_code)
);


CREATE TABLE `tbl_album_file`(
                                 `album_file_code`    INTEGER NOT NULL AUTO_INCREMENT COMMENT '앨범커버파일코드',
                                 `file_origin_name`    VARCHAR(255) NOT NULL COMMENT '원본파일명',
                                 `file_origin_path`    VARCHAR(255) NOT NULL COMMENT '원본경로',
                                 `file_save_name`    VARCHAR(255) NOT NULL COMMENT '저장파일명',
                                 `file_save_path`    VARCHAR(255) NOT NULL COMMENT '저장경로',
                                 `file_extension`    VARCHAR(255) NOT NULL COMMENT '파일확장자',
                                 `album_code`    INTEGER NOT NULL COMMENT '앨범코드',
                                 PRIMARY KEY ( `album_file_code` )
) COMMENT = '앨범커버파일';


-- Create Album table
CREATE TABLE tbl_Album (
                           album_code INTEGER PRIMARY KEY AUTO_INCREMENT,
                           title VARCHAR(255) NOT NULL,
                           release_date DATE NOT NULL,
                           album_file_code INTEGER NOT NULL,
                           artist_code INTEGER,
                           FOREIGN KEY (artist_code) REFERENCES tbl_Artist(artist_code),
                           FOREIGN KEY (album_file_code) REFERENCES tbl_album_file(album_file_code)
);


-- Create Track table
CREATE TABLE tbl_Track (
                           track_code INTEGER PRIMARY KEY AUTO_INCREMENT,
                           title VARCHAR(255) NOT NULL,
                           duration TIME,
                           track_number INTEGER,
                           album_code INTEGER,
                           FOREIGN KEY (album_code) REFERENCES tbl_Album(album_code)
);

-- Create Order table
CREATE TABLE tbl_Order (
                           order_code INTEGER PRIMARY KEY AUTO_INCREMENT,
                           user_code INTEGER NOT NULL,
                           order_date DATE NOT NULL,
                           total_price DECIMAL(10, 2) NOT NULL,
                           FOREIGN KEY (user_code) REFERENCES tbl_User(user_code)
);

-- Create OrderItem table
CREATE TABLE tbl_OrderItem (
                               order_item_code INTEGER PRIMARY KEY AUTO_INCREMENT,
                               order_code INTEGER,
                               item_type VARCHAR(50) NOT NULL,
                               item_code INTEGER,
                               quantity INTEGER,
                               price DECIMAL(10, 2),
                               FOREIGN KEY (order_code) REFERENCES tbl_Order(order_code),
                               FOREIGN KEY (item_code) REFERENCES tbl_Album(album_code) ON DELETE CASCADE,
                               FOREIGN KEY (item_code) REFERENCES tbl_Track(track_code) ON DELETE CASCADE
);

-- Indexes for better performance (optional)
CREATE INDEX idx_artist_code ON tbl_Album(artist_code);
CREATE INDEX idx_album_code ON tbl_Track(album_code);
