package org.example;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class Client {
    private authenticationServiceUser service;
    private Request request;
   private Task task ;

    Client() {
        service = new authenticationServiceUser();
        request = new  Request();
        task = new  Task();
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
}

