package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Worker {
    authenticationServiceWorker service;
    Task task ;
    ExecutedReq executedReq;
    Worker() {

        service = new authenticationServiceWorker();
        task = new Task();
        executedReq =  new ExecutedReq();
    }

    public boolean signup(String name, String userName, String password, String address) {
        return service.signup(name, userName, password, address);
    }

    public int login(String username, String password) {
        return service.login(username, password);
    }

    public void getSpeciality(String speciality) {
        service.getSpeciality(speciality);
    }

    public void getSlot(String slot) {
        service.getSlot(slot);
    }

    public void showOffers(int workerID) {
        task.showOffers(workerID);
    }
    public void insertInitialExecutedRequest(int requestID, int workerID) {
        executedReq.insertInitialExecutedRequest(requestID, workerID);
    }
    public void ShowExecutedRequests(int workerID){
        executedReq.ShowExecutedRequests(workerID);
    }
    public void printClientByRequestID(int reqID){
        task.printClientByRequestID(reqID);
    }
    public void updateRequestStatus(int reqID, String status){
        executedReq.updateRequestStatus(reqID, status);
    }


    public void updateWorkerRating(int reqID, double rating){
        executedReq.updateWorkerRating(reqID, rating);
    }



    public void updateWorkerFeedback(int reqID, String feedback){
        executedReq.updateWorkerFeedback(reqID, feedback);
    }

    public void updateActualTime(int reqID, int time){
        executedReq.updateActualTime(reqID, time);
    }
}
