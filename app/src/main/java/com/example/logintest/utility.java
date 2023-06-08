package com.example.logintest;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class utility {
    //todo doc string dis
    /**
     *
     * @param manager
     * @param id
     * @param fragment
     * @param tag
     */
    public static void replaceFragment(FragmentManager manager, int id, Fragment fragment,String tag){
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(id,fragment,tag);
        fragmentTransaction.commit();
    }
}
