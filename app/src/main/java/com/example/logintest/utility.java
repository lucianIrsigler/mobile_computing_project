package com.example.logintest;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class utility {
    //todo doc string dis

    /**
     * @param manager fragment
     * @param id id to switch
     * @param fragment fragment to switch
     * @param tag tag of new fragment
     */
    public static void replaceFragment(FragmentManager manager, int id, Fragment fragment,String tag){
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }
    /**
     * Replaces the current fragment with a new fragment and adds it to the back stack.
     *
     * @param manager the FragmentManager instance
     * @param id the container view ID
     * @param fragment the new fragment to replace the current fragment
     * @param goBackToFragment the tag of the fragment to go back to when this fragment is popped from the back stack
     * @param tag the tag for the new fragment
     */
    public static void replaceFragmentWithBackStack(FragmentManager manager, int id,
                                                    Fragment fragment,
                                                    String goBackToFragment,
                                                    String tag){
        FragmentTransaction fragmentTransaction = manager.beginTransaction().addToBackStack(goBackToFragment);
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }
    /**
     * Generates a random 7-digit ID.
     *
     * @return the randomly generated 7-digit ID
     */
    public static int generateRandomID() {
        Random random = new Random();
        int min = 1000000; // Minimum 7-digit number
        int max = 9999999; // Maximum 7-digit number
        return random.nextInt(max - min + 1) + min;
    }
    /**
     * Creates and returns a map of month values to their corresponding abbreviations.
     *
     * @return the map of month values and abbreviations
     */
    public static Map<Integer, String> createMonthMap() {
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "JAN");
        monthMap.put(2, "FEB");
        monthMap.put(3, "MAR");
        monthMap.put(4, "APR");
        monthMap.put(5, "MAY");
        monthMap.put(6, "JUN");
        monthMap.put(7, "JUL");
        monthMap.put(8, "AUG");
        monthMap.put(9, "SEP");
        monthMap.put(10, "OCT");
        monthMap.put(11, "NOV");
        monthMap.put(12, "DEC");
        return monthMap;
    }
}
