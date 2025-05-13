CREATE DATABASE Task_Worker_Matching
USE Task_Worker_Matching

CREATE TABLE Location (
    locationID INT PRIMARY KEY IDENTITY(1,1),
    address VARCHAR(255) NOT NULL,
    workerID INT,
    locID VARCHAR(50)
);

CREATE TABLE Client (
    clientID INT PRIMARY KEY IDENTITY(1,1),
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50) NOT NULL,
	userName VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL CHECK (LEN(password) > 6),
    email VARCHAR(100),
    address VARCHAR(255),
    paymentInfo VARCHAR(255)
);

CREATE TABLE Worker (
    workerID INT PRIMARY KEY IDENTITY(1,1),
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50) NOT NULL,
	userName VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL CHECK (LEN(password) > 6),
    rating DECIMAL(3,2),
    workerID_alt VARCHAR(50) UNIQUE
);

ALTER TABLE Worker
ADD fullName AS (fname + ' ' + lname);

CREATE TABLE Specialty (
    specialtyID INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100) NOT NULL UNIQUE,  
    workerID INT,
    avgFee DECIMAL(10,2)
);

CREATE TABLE TimeSlot (
    slotID INT PRIMARY KEY IDENTITY(1,1),
    workerID INT NOT NULL,
    period VARCHAR(50) NOT NULL,
    FOREIGN KEY (workerID) REFERENCES Worker(workerID)
);

CREATE TABLE Task (
    taskID INT PRIMARY KEY IDENTITY(1,1),
    taskName VARCHAR(100) NOT NULL,
    avgTime INT, 
    requiredSpecialty INT,  
    FOREIGN KEY (requiredSpecialty) REFERENCES Specialty(specialtyID) 
);

CREATE TABLE Request (
    requestID INT PRIMARY KEY IDENTITY(1,1),
    clientID INT NOT NULL,
    reqAddress VARCHAR(255),
    preferredTimeSlot DATETIME,
    reqPlacementTime DATETIME DEFAULT GETDATE(),
    taskID INT NOT NULL,
    FOREIGN KEY (clientID) REFERENCES Client(clientID),
    FOREIGN KEY (taskID) REFERENCES Task(taskID)
);

CREATE TABLE ExecutedReq (
    reqID INT PRIMARY KEY,
    workerID INT NOT NULL,
    clientRating DECIMAL(3,2),
    workerRating DECIMAL(3,2),
    actualTime INT,
    reqStatus VARCHAR(20) CHECK (reqStatus IN ('Pending', 'In Progress', 'Completed', 'Cancelled')) DEFAULT 'Pending',
    workerFeedback NVARCHAR(MAX),  
    clientFeedback NVARCHAR(MAX),
    FOREIGN KEY (reqID) REFERENCES Request(requestID),
    FOREIGN KEY (workerID) REFERENCES Worker(workerID)
);

CREATE TABLE WorksIn (
    workerID INT NOT NULL,
    locationID INT NOT NULL,
    PRIMARY KEY (workerID, locationID),
    FOREIGN KEY (workerID) REFERENCES Worker(workerID),
    FOREIGN KEY (locationID) REFERENCES Location(locationID)
);

CREATE TABLE WorksAt (
    workerID INT NOT NULL,
    specialtyID INT NOT NULL,
    PRIMARY KEY (workerID, specialtyID),
    FOREIGN KEY (workerID) REFERENCES Worker(workerID),
    FOREIGN KEY (specialtyID) REFERENCES Specialty(specialtyID)
);

CREATE TABLE Has (
    workerID INT NOT NULL,
    timeSlotID INT NOT NULL,
    PRIMARY KEY (workerID, timeSlotID),
    FOREIGN KEY (workerID) REFERENCES Worker(workerID),
    FOREIGN KEY (timeSlotID) REFERENCES TimeSlot(slotID)
);

CREATE TABLE ExecutionOf (
    requestID INT NOT NULL,
    executedReqID INT NOT NULL,
    PRIMARY KEY (requestID, executedReqID),
    FOREIGN KEY (requestID) REFERENCES Request(requestID),
    FOREIGN KEY (executedReqID) REFERENCES ExecutedReq(reqID)
);

CREATE TABLE Requires (
    taskID INT NOT NULL,
    specialtyID INT NOT NULL,
    PRIMARY KEY (taskID, specialtyID),
    FOREIGN KEY (taskID) REFERENCES Task(taskID),
    FOREIGN KEY (specialtyID) REFERENCES Specialty(specialtyID)
);

CREATE TABLE IsFor (
    requestID INT NOT NULL,
    taskID INT NOT NULL,
    PRIMARY KEY (requestID, taskID),
    FOREIGN KEY (requestID) REFERENCES Request(requestID),
    FOREIGN KEY (taskID) REFERENCES Task(taskID)
);

CREATE TABLE Place (
    clientID INT NOT NULL,
    requestID INT NOT NULL,
    PRIMARY KEY (clientID, requestID),
    FOREIGN KEY (clientID) REFERENCES Client(clientID),
    FOREIGN KEY (requestID) REFERENCES Request(requestID)
);

CREATE TABLE [Execute] (
    workerID INT NOT NULL,
    executedReqID INT NOT NULL,
    PRIMARY KEY (workerID, executedReqID),
    FOREIGN KEY (workerID) REFERENCES Worker(workerID),
    FOREIGN KEY (executedReqID) REFERENCES ExecutedReq(reqID)
);

CREATE INDEX idx_worker_rating ON Worker(rating);
CREATE INDEX idx_request_client ON Request(clientID);
CREATE INDEX idx_request_task ON Request(taskID);
CREATE INDEX idx_executedreq_worker ON ExecutedReq(workerID);
CREATE INDEX idx_executedreq_status ON ExecutedReq(reqStatus)