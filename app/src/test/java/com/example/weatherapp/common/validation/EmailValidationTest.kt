package com.example.weatherapp.common.validation

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EmailValidationTest {
    private lateinit var helper: ValidationHelper

    @Before
    fun setup() {
        helper = ValidationHelper
    }

    @After
    fun tearDown(){
    }

    @Test
    fun `validate email with string empty expected false`() {
        val result = helper.validateEmail("")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate email with string invalid expected false`() {
        val result = helper.validateEmail("abc.com")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate email with string valid expected true`() {
        val result = helper.validateEmail("test@test.com")
        assertEquals(true, result.isValid)
    }
}