package ru.otus.homework_1;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        eventBus.register(new Listener());

        Arrays.stream(args).forEach(eventBus::post);
    }

    private static class Listener {
        @Subscribe
        public void argsCame(String arg) {
            System.out.println(String.format("Arg: - %s has been came", arg));
        }
    }
}
