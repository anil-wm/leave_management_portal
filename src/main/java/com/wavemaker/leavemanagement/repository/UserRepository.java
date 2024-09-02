package com.wavemaker.leavemanagement.repository;

public interface UserRepository {

    boolean getUserByEmailId(String emailId);

    String getPasswordByEmailId(String emailId);
}
