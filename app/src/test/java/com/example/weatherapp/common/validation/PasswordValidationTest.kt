package com.example.weatherapp.common.validation

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PasswordValidationTest {
    private lateinit var helper: ValidationHelper

    @Before
    fun setup() {
        helper = ValidationHelper
    }

    @After
    fun tearDown(){
    }

    @Test
    fun `validate Password with string empty expected false`() {
        val result = helper.validatePassword("")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate Password with string less then 6 char  expected false`() {
        val result = helper.validatePassword("aA")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate Password with string without one capital letter expected false`() {
        val result = helper.validatePassword("aabb12")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate Password with string without one digit expected false`() {
        val result = helper.validatePassword("aaBBCC")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate Password with string without spacial character expected false`() {
        val result = helper.validatePassword("aaBB12")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate Password with string with containing space expected false`() {
        val result = helper.validatePassword("aaBB12 @")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate Password with valid string expected true`() {
        val result = helper.validatePassword("abc@Abc123")
        assertEquals(true, result.isValid)
    }
}