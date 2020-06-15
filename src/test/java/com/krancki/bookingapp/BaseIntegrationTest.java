package com.krancki.bookingapp;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class BaseIntegrationTest {
}
