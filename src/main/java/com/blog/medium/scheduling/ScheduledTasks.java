package com.blog.medium.scheduling;

import com.blog.medium.model.ConfirmationToken;
import com.blog.medium.model.User;
import com.blog.medium.repository.ConfirmationTokenRepository;
import com.blog.medium.repository.PostRepository;
import com.blog.medium.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class ScheduledTasks {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

   @Autowired
   ConfirmationTokenRepository confirmationTokenRepository;

    private static final Logger log =  LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final Integer tenMinutes = 600000;
    private static final Integer oneMinute = 1000*60;

    @Scheduled(fixedRate = 1000*30)
    @Transactional
    public void removeTokens(){
        List<ConfirmationToken> confirmationTokenList = confirmationTokenRepository.findAll();
        confirmationTokenList.forEach(token->{
            if( new Date().getTime() - token.getCreatedDate().getTime() >= tenMinutes ){
                log.debug("Deleting token with id: " + token.getTokenid());
                User user = token.getUser();
                confirmationTokenRepository.deleteConfirmationTokenByTokenid(token.getTokenid());
                if(!user.getIsEnabled()) {
                    userRepository.deleteUserByUsername(user.getUsername());
                }
            }
        });
    }
}
