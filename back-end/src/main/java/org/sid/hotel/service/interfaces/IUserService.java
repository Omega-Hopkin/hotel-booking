package org.sid.hotel.service.interfaces;

import org.sid.hotel.dto.LoginRequest;
import org.sid.hotel.entity.User;
import org.sid.hotel.dto.Response;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
