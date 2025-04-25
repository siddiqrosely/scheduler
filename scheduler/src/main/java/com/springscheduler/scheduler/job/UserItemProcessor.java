package com.springscheduler.scheduler.job;

import com.springscheduler.scheduler.model.Users;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<Users,Users> {


    @Override
    public Users process(Users users) throws Exception {
        users.setName(users.getName().toUpperCase());
        return users;
    }
}
