package com.example.weatherapp.common.validation

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConfirmPasswordValidationTest {
    private lateinit var helper: ValidationHelper

    @Before
    fun setup() {
        helper = ValidationHelper
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `validate confirm password with strings empty expected false`() {
        val result = helper.validateRepeatedPassword(pass = "", repeatedPass = "")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate confirm password with repeated input string  empty expected false`() {
        val result = helper.validateRepeatedPassword(pass = "abc", repeatedPass = "")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate confirm password with repeated input string not same with pass input  expected false`() {
        val result = helper.validateRepeatedPassword(pass = "abc", repeatedPass = "abcd")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate confirm password with repeated input string same as pass input expected true`() {
        val result = helper.validateRepeatedPassword(pass = "abc", repeatedPass = "abc")
        assertEquals(true, result.isValid)
    }
}