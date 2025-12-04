package org.java.design.pattern.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

// Observer interface - defines contract for observers
interface Observer {
    void update(String message);
}

// Subject interface - defines contract for subject
interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers();
}

// Concrete subject - the object being observed
class WeatherStation implements Subject {
    private String weatherCondition;
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer attached");
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("Observer detached");
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(weatherCondition);
        }
    }

    public void setWeatherCondition(String condition) {
        System.out.println("\nWeather changed to: " + condition);
        this.weatherCondition = condition;
        notifyObservers();
    }
}

// Concrete observers
class PhoneDisplay implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Phone Display: Weather is " + message);
    }
}

class WebsiteDisplay implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Website Display: Current conditions - " + message);
    }
}

class MobileApp implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Mobile App: Alert! Weather is now " + message);
    }
}

/**
 * Observer Pattern Demo
 * In this example, we have a WeatherStation (subject) that notifies multiple displays (observers)
 * whenever the weather condition changes. Observers can attach and detach themselves from the subject.
 * When the weather changes, all attached observers are automatically notified.
 */
public class ObserverPatternDemo {
    public static void main(String[] args) {
        // Create subject (weather station)
        WeatherStation station = new WeatherStation();

        // Create observers (different displays)
        PhoneDisplay phoneDisplay = new PhoneDisplay();
        WebsiteDisplay websiteDisplay = new WebsiteDisplay();
        MobileApp mobileApp = new MobileApp();

        // Attach observers
        System.out.println("=== Attaching Observers ===");
        station.attach(phoneDisplay);
        station.attach(websiteDisplay);
        station.attach(mobileApp);

        // Subject changes state - all observers are notified automatically
        System.out.println("\n=== Weather Updates ===");
        station.setWeatherCondition("Sunny");

        station.setWeatherCondition("Rainy");

        // Detach one observer
        System.out.println("\n=== Detaching Phone Display ===");
        station.detach(phoneDisplay);

        // Only remaining observers get notified
        station.setWeatherCondition("Cloudy");
    }
}
