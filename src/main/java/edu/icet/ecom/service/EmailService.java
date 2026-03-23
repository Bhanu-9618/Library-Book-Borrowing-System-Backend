package edu.icet.ecom.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}