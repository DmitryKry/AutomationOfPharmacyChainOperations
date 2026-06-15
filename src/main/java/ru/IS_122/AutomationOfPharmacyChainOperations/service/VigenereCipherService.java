package ru.IS_122.AutomationOfPharmacyChainOperations.service;

import org.springframework.stereotype.Service;

@Service
public class VigenereCipherService {

    private static final String SECRET_KEY = "PHARMACY2025";

    public String encrypt(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            char keyChar = SECRET_KEY.charAt(i % SECRET_KEY.length());
            result.append((char) (inputChar + keyChar));
        }
        return result.toString();
    }

    // Расшифровка (все символы)
    public String decrypt(String input) {
        if (input == null || input.isEmpty()) return input;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            char keyChar = SECRET_KEY.charAt(i % SECRET_KEY.length());
            result.append((char) (inputChar - keyChar));
        }
        return result.toString();
    }
}