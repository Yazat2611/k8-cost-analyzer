CREATE TABLE pod_data(
    id BIGSERIAl PRIMARY KEY ,
    pod_name VARCHAR(255) NOT NULL ,
    namespace VARCHAR(255) NOT NULL ,
    cpu_usage DOUBLE PRECISION ,
    memory_usage BIGINT,
    cpu_requested DOUBLE PRECISION,
    memory_requested BIGINT,
    timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_pod_namespace ON pod_data(pod_name, namespace);
CREATE INDEX idx_timestamp ON pod_data(timestamp);