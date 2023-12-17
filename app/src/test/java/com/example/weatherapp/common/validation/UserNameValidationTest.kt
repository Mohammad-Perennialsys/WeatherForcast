package com.example.weatherapp.common.validation

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserNameValidationTest {

    private lateinit var helper: ValidationHelper

    @Before
    fun setup() {
        helper = ValidationHelper
    }

    @Test
    fun `validate UserName with string empty expected false`() {
        val result = helper.validateUserName("")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate UserName with string less then 5 char  expected false`() {
        val result = helper.validateUserName("123")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate UserName with string start with number  expected false`() {
        val result = helper.validateUserName("1Abcd")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate UserName with string containing spaces expected false`() {
        val result = helper.validateUserName("Ab c d")
        assertEquals(false, result.isValid)
    }

    @Test
    fun `validate UserName with correct string expected true`() {
        val result = helper.validateUserName("abcA@1")
        assertEquals(true, result.isValid)
    }
}