DROP TABLE IF EXISTS USERS CASCADE;
CREATE TABLE USERS (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

DROP TABLE IF EXISTS REQUESTS CASCADE;
CREATE TABLE REQUESTS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(512) NOT NULL,
    requester_id BIGINT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS ITEMS CASCADE;
CREATE TABLE ITEMS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    is_available BOOLEAN DEFAULT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_to_requests FOREIGN KEY(request_id) REFERENCES requests(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS BOOKINGS CASCADE;
CREATE TABLE BOOKINGS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(25),
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_to_users FOREIGN KEY(booker_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT check_bookings_status CHECK (status::text IN ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED'))
);

DROP TABLE IF EXISTS COMMENTS CASCADE;
CREATE TABLE COMMENTS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(512) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE
);