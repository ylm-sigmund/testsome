package com.diy.sigmund.aspect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author ylm-sigmund
 * @since 2020/10/9 21:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AspectConfig.class)
public class ConferenceServiceTest {

    @Autowired
    private ConferenceService conferenceService;
    @Test
    public void conference() {
        conferenceService.conference();
    }
}