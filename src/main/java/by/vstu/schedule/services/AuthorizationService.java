package by.vstu.schedule.services;

import by.vstu.schedule.models.DTO.Token;

public interface AuthorizationService {

    Token authorize(String username, String password, String clientUsername, String clientPassword);
}
