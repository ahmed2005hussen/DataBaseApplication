package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome in Task Worker Matching");
            System.out.println("-------------------------------");

            System.out.println("Who Are You? ");
            System.out.println("1- Worker ");
            System.out.println("2- Client ");
            System.out.println("3. Exit ");
            System.out.println("Enter your choice (1-3)");
            int Choice = new Scanner(System.in).nextInt();
            if (Choice == 1) {
                while (true) {
                    Worker worker = new Worker();
                    System.out.println("What do  you want to do?");
                    System.out.println("1. Login ");
                    System.out.println("2. Signup");
                    System.out.println("3. Exit ");
                    System.out.println("Enter your choice (1-3)");
                    int choice = new Scanner(System.in).nextInt();

                    if (choice == 1) {
                        System.out.println("Enter your username: ");
                        String username = new Scanner(System.in).next();
                        System.out.println("Enter your password: ");
                        String password = new Scanner(System.in).next();
                        int workerID = worker.login(username, password);
                        if (workerID != 0) {

                            System.out.println("Login Successful");

                            while(true) {
                                System.out.println("What do you want to do?");
                                System.out.println("1. Assign To Task  ");
                                System.out.println("2. Show Tasks ");
                                System.out.println("3. Edit Task  ");
                                System.out.println("4. Exit  ");
                                System.out.println("Enter your choice (1-3)");

                                break ;
                            }

                        } else {
                            System.out.println("Login Failed");
                            System.out.println("");
                            System.out.println("Try again");
                        }

                    } else if (choice == 2) {

                        System.out.println("Enter Your Name");
                        String name = new Scanner(System.in).next();
                        System.out.println("Enter Your username");
                        String userName = new Scanner(System.in).next();
                        System.out.println("Enter Your password");
                        String password = new Scanner(System.in).next();
                        while (password.length() < 6) {
                            System.out.println("Password too short");
                            System.out.println("Enter your Password : ");
                            password = new Scanner(System.in).next();
                        }

                        int count, counter;
                        String speciality;
                        System.out.println("How many Speciality do you wort at ? ");
                        count = new Scanner(System.in).nextInt();
                        counter = 1;
                        while (count != 0) {
                            System.out.println("Enter Speciality " + counter++ + ": ");
                            speciality = new Scanner(System.in).next();
                            worker.getSpeciality(speciality);
                            count--;
                        }

                        System.out.println("How many Slots do you wort at ? ");
                        count = new Scanner(System.in).nextInt();
                        counter = 1;
                        String slot;
                        while (count != 0) {
                            System.out.println("Enter slot " + counter++ + ": ");
                            slot = new Scanner(System.in).next();
                            worker.getSlot(slot);
                            count--;
                        }

                        System.out.println("Enter Your Address : ");
                        String Address = new Scanner(System.in).next();

                        if (worker.signup(name, userName, password, Address)) {
                            System.out.println("signup successful");
                        } else {
                            while (worker.signup(name, userName, password, Address) == false) {
                                System.out.println("Username is reserved");
                                System.out.println("Enter Another Username");
                                userName = new Scanner(System.in).next();
                            }
                            System.out.println("signup successful");
                        }

                    } else if (choice == 3) {
                        break;
                    }

                }


            }


            else if (Choice == 2) {
                while (true) {
                    Client user = new Client();
                    System.out.println("What do  you want to do?");
                    System.out.println("1. Login ");
                    System.out.println("2. Signup");
                    System.out.println("3. Exit ");
                    System.out.println("Enter your choice (1-3)");
                    int choice = new Scanner(System.in).nextInt();

                    if (choice == 1) {
                        System.out.println("Enter your username: ");
                        String username = new Scanner(System.in).next();
                        System.out.println("Enter your password: ");
                        String password = new Scanner(System.in).next();

                        int clientID = user.login(username, password);
                        int requiredSpecialty ;
                        int preferSlot ;
                        int taskID;

                        if (clientID != 0) {
                            System.out.println("Login Successful");
                            while(true) {
                                System.out.println("What do you want to do?");
                                System.out.println("1. Request Order ");
                                System.out.println("2. Show Status Order ");
                                System.out.println("3. Exit ");
                                System.out.println("Enter your choice (1-3)");
                                int choice2 = new Scanner(System.in).nextInt();
                                if (choice2 == 1) {
                                user.printSpecialties();
                                System.out.println("Enter Required Specialty for your task or 0 if it does not exist : ");
                                 requiredSpecialty = new Scanner(System.in).nextInt() ;
                                 if(requiredSpecialty == 0) {
                                     System.out.println("Sorry :( , we will add it soon  : ");
                                     continue;
                                 }
                                 user.printTimeSlotsForSpecialty(requiredSpecialty);
                                 System.out.println("Enter SlotID You prefer or 0 if it does not exist :  : ");
                                 preferSlot = new  Scanner(System.in).nextInt();
                                    if(preferSlot == 0) {
                                        System.out.println("Sorry :( , we will add it soon  : ");
                                        continue;
                                    }
                                    System.out.println("Enter Your Address : ");
                                    String Address = new Scanner(System.in).next();
                                    taskID = user.makeTask(clientID,requiredSpecialty);
                                    user.makeRequest(clientID,Address,preferSlot,taskID);
                                    System.out.println("Request successful");
                                }

                                else if (choice2 == 2) {
                                    // هنتبع ال id بتاع التاسك و الحاله بتاعتها و مين العامل اللي ماسكها و اتعملت ولا لا
                                }
                                else if (choice2 == 3) {
                                    System.out.println();
                                    break;
                                }
                            }

                        } else {
                            System.out.println("Login Failed");
                            System.out.println("");
                            System.out.println("Try again");
                        }

                    } else if (choice == 2) {

                        System.out.println("Enter Your Name");
                        String name = new Scanner(System.in).next();
                        System.out.println("Enter Your username");
                        String userName = new Scanner(System.in).next();
                        System.out.println("Enter Your password");
                        String password = new Scanner(System.in).next();
                        while (password.length() < 6) {
                            System.out.println("Password too short");
                            System.out.println("Enter your Password : ");
                            password = new Scanner(System.in).next();
                        }
                        System.out.println("Enter Your email : ");
                        String email = new Scanner(System.in).next();

                        System.out.println("Enter Your Address : ");
                        String address = new Scanner(System.in).next();

                        System.out.println("Enter Your paymentInfo : ");
                        String paymentInfo = new Scanner(System.in).next();

                        if (user.signup(name, userName, password, address, email, paymentInfo)) {
                            System.out.println("signup successful");
                        } else {
                            while (user.signup(name, userName, password, address, email, paymentInfo) == false) {
                                System.out.println("Username is reserved");
                                System.out.println("Enter Another Username");
                                userName = new Scanner(System.in).next();
                            }
                            System.out.println("signup successful");
                        }

                    } else if (choice == 3) {
                        break;
                    }

                }
            }

            else if (Choice == 3) {
                break;
            }


        }

    }
}





