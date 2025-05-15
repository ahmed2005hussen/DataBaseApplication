package org.example;

public class Worker {
    authenticationServiceWorker service;

    Worker() {
        service = new authenticationServiceWorker();
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
}
