package com.sjdroid.screenshotblocker

import android.app.Activity
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for WindowSecurePolicy implementations
 */
class WindowSecurePolicyTest {
    
    private val mockActivity = mock<Activity>()
    
    @Test
    fun `AlwaysSecurePolicy should always return true`() {
        val policy = AlwaysSecurePolicy()
        assertTrue(policy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `NeverSecurePolicy should always return false`() {
        val policy = NeverSecurePolicy()
        assertFalse(policy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `ConditionalSecurePolicy should secure matching activity by full class name`() {
        val policy = ConditionalSecurePolicy("com.example.SecureActivity")
        
        whenever(mockActivity.javaClass.name).thenReturn("com.example.SecureActivity")
        assertTrue(policy.shouldSecure(mockActivity))
        
        whenever(mockActivity.javaClass.name).thenReturn("com.example.MainActivity")
        assertFalse(policy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `ConditionalSecurePolicy should secure matching activity by simple class name`() {
        val policy = ConditionalSecurePolicy("SecureActivity")
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("SecureActivity")
        assertTrue(policy.shouldSecure(mockActivity))
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("MainActivity")
        assertFalse(policy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `ConditionalSecurePolicy with multiple activities should work correctly`() {
        val policy = ConditionalSecurePolicy("LoginActivity", "PaymentActivity", "SecureActivity")
        
        // Test matching activities
        whenever(mockActivity.javaClass.simpleName).thenReturn("LoginActivity")
        assertTrue(policy.shouldSecure(mockActivity))
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("PaymentActivity")
        assertTrue(policy.shouldSecure(mockActivity))
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("SecureActivity")
        assertTrue(policy.shouldSecure(mockActivity))
        
        // Test non-matching activity
        whenever(mockActivity.javaClass.simpleName).thenReturn("MainActivity")
        assertFalse(policy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `AnnotationBasedSecurePolicy should secure annotated activities`() {
        val policy = AnnotationBasedSecurePolicy()
        
        // Create a real class that can be annotated for testing
        @SecureScreen
        class AnnotatedActivity : Activity()
        
        class NonAnnotatedActivity : Activity()
        
        val annotatedActivity = AnnotatedActivity()
        val nonAnnotatedActivity = NonAnnotatedActivity()
        
        assertTrue(policy.shouldSecure(annotatedActivity))
        assertFalse(policy.shouldSecure(nonAnnotatedActivity))
    }
    
    @Test
    fun `AndSecurePolicy should require all policies to return true`() {
        val alwaysTrue = AlwaysSecurePolicy()
        val alwaysFalse = NeverSecurePolicy()
        val conditional = ConditionalSecurePolicy("TestActivity")
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("TestActivity")
        
        // All true policies
        val allTruePolicy = AndSecurePolicy(alwaysTrue, conditional)
        assertTrue(allTruePolicy.shouldSecure(mockActivity))
        
        // Mix of true and false
        val mixedPolicy = AndSecurePolicy(alwaysTrue, alwaysFalse, conditional)
        assertFalse(mixedPolicy.shouldSecure(mockActivity))
        
        // All false policies
        val allFalsePolicy = AndSecurePolicy(alwaysFalse)
        assertFalse(allFalsePolicy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `OrSecurePolicy should require at least one policy to return true`() {
        val alwaysTrue = AlwaysSecurePolicy()
        val alwaysFalse = NeverSecurePolicy()
        val conditional = ConditionalSecurePolicy("TestActivity")
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("OtherActivity")
        
        // At least one true policy
        val mixedPolicy = OrSecurePolicy(alwaysFalse, alwaysTrue, conditional)
        assertTrue(mixedPolicy.shouldSecure(mockActivity))
        
        // All false policies
        val allFalsePolicy = OrSecurePolicy(alwaysFalse, conditional)
        assertFalse(allFalsePolicy.shouldSecure(mockActivity))
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("TestActivity")
        
        // Conditional becomes true
        val conditionalTruePolicy = OrSecurePolicy(alwaysFalse, conditional)
        assertTrue(conditionalTruePolicy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `AndSecurePolicy with vararg constructor should work correctly`() {
        val policy1 = AlwaysSecurePolicy()
        val policy2 = ConditionalSecurePolicy("TestActivity")
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("TestActivity")
        
        val andPolicy = AndSecurePolicy(policy1, policy2)
        assertTrue(andPolicy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `OrSecurePolicy with vararg constructor should work correctly`() {
        val policy1 = NeverSecurePolicy()
        val policy2 = ConditionalSecurePolicy("TestActivity")
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("TestActivity")
        
        val orPolicy = OrSecurePolicy(policy1, policy2)
        assertTrue(orPolicy.shouldSecure(mockActivity))
    }
    
    @Test
    fun `Complex policy combinations should work correctly`() {
        val annotationPolicy = AnnotationBasedSecurePolicy()
        val conditionalPolicy = ConditionalSecurePolicy("PaymentActivity")
        val neverPolicy = NeverSecurePolicy()
        
        // OR(annotation, conditional) AND NOT(never)
        val complexPolicy = AndSecurePolicy(
            OrSecurePolicy(annotationPolicy, conditionalPolicy),
            neverPolicy // This will always be false, so AND will be false
        )
        
        whenever(mockActivity.javaClass.simpleName).thenReturn("PaymentActivity")
        assertFalse(complexPolicy.shouldSecure(mockActivity)) // AND with NeverSecurePolicy = false
        
        // OR(annotation, conditional) - without the AND
        val simpleOrPolicy = OrSecurePolicy(annotationPolicy, conditionalPolicy)
        assertTrue(simpleOrPolicy.shouldSecure(mockActivity)) // PaymentActivity matches conditional
    }
} 