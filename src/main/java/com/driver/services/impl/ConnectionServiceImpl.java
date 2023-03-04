package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception {
        User user = userRepository2.findById(userId).get();
        if (user.getConnected()) throw new Exception("Already connected");
        if (user.getOriginalCountry().toString().equals(countryName)) return user;
        for (ServiceProvider serviceProvider: user.getServiceProviderList()) {
            for (Country country: serviceProvider.getCountryList()) {
                if (country.getCountryName().toString().equals(countryName)) {
                    country.setUser(user);
                    serviceProvider.getUsers().add(user);
                    user.setConnected(true);
                    user.setMaskedIp(CountryName.valueOf(countryName).toCode());
                    Connection connection = new Connection(user, serviceProvider);
                    serviceProvider.getConnectionList().add(connection);
                    return user;
                }
            }
        }
        throw new Exception("Unable to connect");
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user = userRepository2.findById(userId).get();
        if (!user.getConnected()) throw new Exception("Already disconnected");
        user.setConnected(false);
        user.setMaskedIp(null);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender = userRepository2.findById(senderId).get();
        User reciever = userRepository2.findById(receiverId).get();
        //for later
        return sender;
    }
}