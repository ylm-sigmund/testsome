package com.diy.sigmund.aspect;

import org.springframework.stereotype.Service;

/**
 * @author ylm-sigmund
 * @since 2020/10/9 21:35
 */
@Service
public class ConferenceService implements IConferenceService {

    @Override
    public void conference() {
        System.out.println("开会...");
    }
}
