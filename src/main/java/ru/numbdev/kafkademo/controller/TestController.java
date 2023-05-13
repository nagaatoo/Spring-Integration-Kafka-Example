package ru.numbdev.kafkademo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.numbdev.kafkademo.service.TestService;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.type", havingValue = "producer", matchIfMissing = true)
public class TestController {

    private final TestService testService;

    @PostMapping("/test/{id}")
    public void test(@PathVariable("id") Integer id) {
        testService.doTest(id);
    }
}
