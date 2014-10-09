package com.nortal.service;

import com.nortal.model.PattyUser;
import java.util.List;

public interface UserService {
    List<PattyUser> search(String keyword);
}