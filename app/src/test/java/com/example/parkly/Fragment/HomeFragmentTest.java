package com.example.parkly.Fragment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by donvel on 2018-03-20.
 */
public class HomeFragmentTest {
    @Test
    public void estimatedPrice() throws Exception {
        String input1 = "Red";
        int input2 = 1;
        int input3 = 15;
        String output;
        String expected = "1.50" + " \u20ac";

        HomeFragment homeFragment = new HomeFragment();
        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Blue";
        input2 = 0;
        input3 = 30;
        expected = "0.30" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------

        input1 = "Yellow";
        input2 = 10;
        input3 = 0;
        expected = "20.00" + " \u20ac";

        output = homeFragment.estimatedPrice(input1, input2, input3);

        assertEquals(expected, output);

        //---------------------------------------------------------------------------
    }

    @Test
    public void estimatedTime() throws Exception {
        //Paklausti
    }

    @Test
    public void  showPriceAndParkingEnding(){
        //Paklausti
    }



}