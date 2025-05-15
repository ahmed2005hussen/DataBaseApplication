package org.example;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class Client {
    private authenticationServiceUser service;
    private Request request;
   private Task task ;
   private ExecutedReq executedReq;

    Client() {
        service = new authenticationServiceUser();
        request = new  Request();
        task = new  Task();
        executedReq= new  ExecutedReq();
    }

    public int login(String username, String password){
        return service.login(username, password);
    }

    public boolean signup(String name, String userName, String password, String address, String email, String paymentInfo){
        return service.signup(name, userName, password, address, email, paymentInfo);
    }

    public void printSpecialties(){
        request.printAllSpecialties();
    }

    public void printTimeSlotsForSpecialty(int specialtyId){request.printTimeSlotsForSpecialty(specialtyId);}

    public int makeTask(int clientID, int requiredSpecialty){
      return task.makeTask(clientID, requiredSpecialty);
    }
    public void makeRequest(int clientID, String address, int preferSlot, int taskID) {
        request.makeRequest(clientID,address,preferSlot,taskID);
    }
    public boolean printExecutedReqForClient(int clientID){
       return request.printExecutedReqForClient(clientID);
    }


    public void updateClientRating(int reqID, double rating) {
        executedReq.updateClientRating(reqID, rating);
    }

    public void updateClientFeedback(int reqID, String feedback){
        executedReq.updateClientFeedback(reqID, feedback);
    }


}

