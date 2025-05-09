/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package diskscheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DiskScheduling {
    private static final int DISK_SIZE = 5000; // Cylinders from 0 to 4999
    private static final int NUM_REQUESTS = 1000; // Number of cylinder requests

    public static void main(String[] args) {
        int initialHeadPosition = 2500; // Default initial position (middle of disk)
        
        if (args.length >= 1) {
            try {
                initialHeadPosition = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid initial head position. Using default value of " + initialHeadPosition);
            }
        } else {
            System.out.println("No initial head position provided. Using default value of " + initialHeadPosition);
        }
        
        try {
            
            if (initialHeadPosition < 0 || initialHeadPosition >= DISK_SIZE) {
                System.out.println("Initial head position must be between 0 and " + (DISK_SIZE - 1));
                System.exit(1);
            }
            
            // Generate random cylinder requests
            List<Integer> requests = generateRequests(NUM_REQUESTS);
            
            // Apply each algorithm and calculate total head movement
            int fcfsMovement = fcfs(requests, initialHeadPosition);
            int scanMovement = scan(requests, initialHeadPosition);
            int cscanMovement = cscan(requests, initialHeadPosition);
            
            // Print results
            System.out.println("Initial head position: " + initialHeadPosition);
            System.out.println("Total head movement using FCFS: " + fcfsMovement + " cylinders");
            System.out.println("Total head movement using SCAN: " + scanMovement + " cylinders");
            System.out.println("Total head movement using C-SCAN: " + cscanMovement + " cylinders");
            
        } catch (NumberFormatException e) {
            System.out.println("Initial head position must be an integer");
            System.exit(1);
        }
    }
    
    /**
     * Generates a list of random cylinder requests
     * @param count Number of requests to generate
     * @return List of cylinder numbers
     */
    private static List<Integer> generateRequests(int count) {
        List<Integer> requests = new ArrayList<>();
        Random random = new Random();
        
        System.out.println("Generated cylinder requests:");
        for (int i = 0; i < count; i++) {
            int request = random.nextInt(DISK_SIZE);
            requests.add(request);
            
            // Print first 10 requests and last 10 requests
            if (i < 10 || i >= count - 10) {
                System.out.print(request + " ");
                if (i == 9 && count > 20) {
                    System.out.print("... ");
                }
            }
        }
        System.out.println();
        
        return requests;
    }
    
    /**
     * First-Come, First-Served (FCFS) disk scheduling algorithm
     * @param requests List of cylinder requests
     * @param initialHeadPosition Initial position of disk head
     * @return Total head movement
     */
    private static int fcfs(List<Integer> requests, int initialHeadPosition) {
        int totalMovement = 0;
        int currentPosition = initialHeadPosition;
        
        for (int request : requests) {
            totalMovement += Math.abs(currentPosition - request);
            currentPosition = request;
        }
        
        return totalMovement;
    }
    
    /**
     * SCAN disk scheduling algorithm (also known as Elevator algorithm)
     * @param requests List of cylinder requests
     * @param initialHeadPosition Initial position of disk head
     * @return Total head movement
     */
    private static int scan(List<Integer> requests, int initialHeadPosition) {
        int totalMovement = 0;
        int currentPosition = initialHeadPosition;
        
        // Copy requests to avoid modifying the original list
        List<Integer> requestsCopy = new ArrayList<>(requests);
        
        // Add the initial position to the list
        requestsCopy.add(currentPosition);
        
        // Add disk boundaries for convenience
        requestsCopy.add(0);
        requestsCopy.add(DISK_SIZE - 1);
        
        // Sort all positions
        Collections.sort(requestsCopy);
        
        // Find where the initial head position is in the sorted list
        int startIndex = requestsCopy.indexOf(currentPosition);
        
        // First go towards the higher cylinder numbers (right)
        for (int i = startIndex + 1; i < requestsCopy.size(); i++) {
            totalMovement += (requestsCopy.get(i) - currentPosition);
            currentPosition = requestsCopy.get(i);
        }
        
        // Then reverse direction and go towards the lower cylinder numbers (left)
        for (int i = startIndex - 1; i >= 0; i--) {
            totalMovement += (currentPosition - requestsCopy.get(i));
            currentPosition = requestsCopy.get(i);
        }
        
        return totalMovement;
    }
    
    /**
     * C-SCAN disk scheduling algorithm (Circular SCAN)
     * @param requests List of cylinder requests
     * @param initialHeadPosition Initial position of disk head
     * @return Total head movement
     */
    private static int cscan(List<Integer> requests, int initialHeadPosition) {
        int totalMovement = 0;
        int currentPosition = initialHeadPosition;
        
        // Copy requests to avoid modifying the original list
        List<Integer> requestsCopy = new ArrayList<>(requests);
        
        // Add the initial position to the list
        requestsCopy.add(currentPosition);
        
        // Add disk boundaries for convenience
        requestsCopy.add(0);
        requestsCopy.add(DISK_SIZE - 1);
        
        // Sort all positions
        Collections.sort(requestsCopy);
        
        // Find where the initial head position is in the sorted list
        int startIndex = requestsCopy.indexOf(currentPosition);
        
        // First go towards the higher cylinder numbers (right)
        for (int i = startIndex + 1; i < requestsCopy.size(); i++) {
            totalMovement += (requestsCopy.get(i) - currentPosition);
            currentPosition = requestsCopy.get(i);
        }
        
        // Jump from the highest cylinder to the lowest cylinder
        // (this movement is not counted as per C-SCAN definition)
        currentPosition = requestsCopy.get(0);
        
        // Then continue from the lowest cylinder up to the initial position
        for (int i = 1; i < startIndex; i++) {
            totalMovement += (requestsCopy.get(i) - currentPosition);
            currentPosition = requestsCopy.get(i);
        }
        
        return totalMovement;
    }
}