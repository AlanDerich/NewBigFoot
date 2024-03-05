package com.derich.bigfoot

import com.derich.bigfoot.ui.common.composables.CommonVariables
import org.junit.Assert.assertEquals
import org.junit.Test

class CommonVariablesTest {
    private val classUnderTest = CommonVariables

    @Test
    fun calculateContributionsDiffTest(){
        assertEquals(-175, classUnderTest.calculateContributionsDifference(30420))
    }

    @Test
    fun calculateResultingDateTest(){
        assertEquals("05/03/2024", classUnderTest.calculateResultingDate(30595))
    }
}