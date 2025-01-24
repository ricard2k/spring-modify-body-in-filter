package com.peironcely.springmodifybodyinfilter.controller;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    @GetMapping(path="/hello", produces="application/json")
    @Nonnull
    public HelloResponse hello() {
        return HelloResponse.builder().message("Hello world").build();
    }

    @Builder
    @JsonDeserialize(builder = HelloResponse.HelloResponseBuilder.class)
    public record HelloResponse(@Nonnull String message) {
    }
}
